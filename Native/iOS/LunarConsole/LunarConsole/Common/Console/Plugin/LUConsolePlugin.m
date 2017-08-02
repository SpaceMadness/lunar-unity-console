//
//  LUConsolePlugin.m
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2017 Alex Lementuev, SpaceMadness.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

#import "LUConsolePlugin.h"

#import "Lunar.h"
#import "LUConsolePluginImp.h"
#import "LUConsoleEditorSettings.h"

NSString * const LUConsoleCheckFullVersionNotification = @"LUConsoleCheckFullVersionNotification";
NSString * const LUConsoleCheckFullVersionNotificationSource = @"source";

static const NSTimeInterval kWindowAnimationDuration = 0.4f;
static const CGFloat kWarningHeight = 45.0f;

static NSString * const kScriptMessageConsoleOpen    = @"console_open";
static NSString * const kScriptMessageConsoleClose   = @"console_close";
static NSString * const kScriptMessageTrackEvent     = @"track_event";

static NSString * const kSettingsFilename          = @"com.spacemadness.lunarmobileconsole.settings.bin";

@interface LUConsolePlugin () <LUConsoleControllerDelegate, LUExceptionWarningControllerDelegate>
{
    LUConsolePluginImp      * _pluginImp;
    LUUnityScriptMessenger  * _scriptMessenger;
    UIGestureRecognizer     * _gestureRecognizer;
    LUConsoleGesture          _gesture;
    NSArray<NSString *>     * _emails;
}

@end

@implementation LUConsolePlugin

- (instancetype)initWithTargetName:(NSString *)targetName
                        methodName:(NSString *)methodName
                           version:(NSString *)version
                          capacity:(NSUInteger)capacity
                         trimCount:(NSUInteger)trimCount
                       gestureName:(NSString *)gestureName
                      settingsJson:(NSString *)settingsJson
{
    self = [super init];
    if (self)
    {
        if (!LU_IOS_MIN_VERSION_AVAILABLE)
        {
            NSLog(@"Console is not initialized. Mininum iOS version required: %d", LU_SYSTEM_VERSION_MIN);
            
            self = nil;
            return nil;
        }
        
        NSData *settingsData = [settingsJson dataUsingEncoding:NSUTF8StringEncoding];
        NSDictionary *settingsDict = [NSJSONSerialization JSONObjectWithData:settingsData options:0 error:nil];
        
        LUConsoleEditorSettings *editorSettings = [[LUConsoleEditorSettings alloc] initWithDictionary:settingsDict];
        
        _pluginImp = [[LUConsolePluginImp alloc] initWithPlugin:self];
        _scriptMessenger = [[LUUnityScriptMessenger alloc] initWithTargetName:targetName methodName:methodName];
        _version = version;
        _console = [[LUConsole alloc] initWithCapacity:capacity trimCount:trimCount];
        _actionRegistry = [[LUActionRegistry alloc] init];
        _actionRegistry.actionSortingEnabled = editorSettings.isActionSortingEnabled;
        _actionRegistry.variableSortingEnabled = editorSettings.variableSortingEnabled;
        
        _gesture = [self gestureFromString:gestureName];
        _emails = editorSettings.emails;
        
        _settings = [[LUConsolePluginSettings alloc] initWithFilename:kSettingsFilename];
        _settings.enableExceptionWarning = editorSettings.isExceptionWarningEnabled;
        _settings.enableTransparentLogOverlay = editorSettings.isTransparentLogOverlayEnabled;
        
        LUConsolePluginSettings *existing = [LUConsolePluginSettings loadFromFile:kSettingsFilename
                                                                      initDefault:NO];
        if (existing != nil)
        {
            _settings.enableExceptionWarning = existing.enableExceptionWarning;
            _settings.enableTransparentLogOverlay = existing.enableTransparentLogOverlay;
        }
    }
    return self;
}

- (void)dealloc
{
    [self disableGestureRecognition];
}

#pragma mark -
#pragma mark Public interface

- (void)start
{
    [self enableGestureRecognition];
    
    if (_settings.enableTransparentLogOverlay)
    {
        dispatch_async(dispatch_get_main_queue(), ^{
            [self showOverlay];
        });
    }
}

- (void)stop
{
    [self removeConsole];
    [self hideOverlay];
    [self hideWarning];
    [self disableGestureRecognition];
    [self unregisterNotifications];
}

- (void)showConsole
{
    [self hideOverlay];
    [self hideWarning];
    
    if (_consoleWindow == nil)
    {
        LUConsoleController *controller = [LUConsoleController controllerWithPlugin:self];
        controller.emails = _emails;
        controller.delegate = self;
        
        CGRect windowFrame = LUGetScreenBounds();
        CGRect windowInitialFrame = windowFrame;
        windowInitialFrame.origin.y -= CGRectGetHeight(windowFrame);
        
        _consoleWindow = [[LUWindow alloc] initWithFrame:windowInitialFrame];
        _consoleWindow.rootViewController = controller;
        _consoleWindow.opaque = YES;
        _consoleWindow.backgroundColor = [UIColor clearColor];
        _consoleWindow.hidden = NO;
        
        [UIView animateWithDuration:kWindowAnimationDuration animations:^{
            _consoleWindow.frame = windowFrame;
        }];
        
        [self registerNotifications];
        [self disableGestureRecognition];
    }
}

- (void)hideConsole
{
    if (_consoleWindow != nil)
    {
        [self unregisterNotifications];
        
        CGRect windowFrame = _consoleWindow.frame;
        windowFrame.origin.y -= CGRectGetHeight(windowFrame);
        
        LUWindow * window = _consoleWindow; // don't capture self in the block
        [UIView animateWithDuration:kWindowAnimationDuration animations:^{
            window.frame = windowFrame;
        } completion:^(BOOL finished) {
            window.hidden = YES;
            [self enableGestureRecognition];
        }];
        
        _consoleWindow = nil;
		
        [_scriptMessenger sendMessageName:kScriptMessageConsoleClose];
    }
    
    if (_settings.enableTransparentLogOverlay)
    {
        [self showOverlay];
    }
}

- (void)removeConsole
{
    _consoleWindow.hidden = YES;
    _consoleWindow = nil;
}

- (void)showOverlay
{
    [_pluginImp showOverlay];
}

- (void)hideOverlay
{
    [_pluginImp hideOverlay];
}

- (void)logMessage:(NSString *)message stackTrace:(NSString *)stackTrace type:(LUConsoleLogType)type
{
    if (LU_IS_CONSOLE_LOG_TYPE_ERROR(type) && _consoleWindow == nil)
    {
        [self showWarningWithMessage:message];
    }
    
    // TODO: use batching
    lunar_dispatch_main(^{
        [_console logMessage:message stackTrace:stackTrace type:type];
    });
}

- (void)clear
{
    [_console clear];
}

#pragma mark -
#pragma mark Quick actions

- (void)registerActionWithId:(int)actionId name:(NSString *)name
{
    [_actionRegistry registerActionWithId:actionId name:name];
}

- (void)unregisterActionWithId:(int)actionId
{
    [_actionRegistry unregisterActionWithId:actionId];
}

- (LUCVar *)registerVariableWithId:(int)entryId name:(NSString *)name type:(NSString *)type value:(NSString *)value
{
    return [self registerVariableWithId:entryId name:name type:type value:value defaultValue:value];
}

- (LUCVar *)registerVariableWithId:(int)entryId name:(NSString *)name type:(NSString *)type value:(NSString *)value defaultValue:(NSString *)defaultValue
{
    return [_actionRegistry registerVariableWithId:entryId name:name typeName:type value:value defaultValue:defaultValue];
}

- (void)setValue:(NSString *)value forVariableWithId:(int)variableId
{
    [_actionRegistry setValue:value forVariableWithId:variableId];
}

#pragma mark -
#pragma mark Warnings

- (BOOL)showWarningWithMessage:(NSString *)message
{
    if (_warningWindow == nil && _settings.enableExceptionWarning)
    {
        CGSize screenSize = LUGetScreenBounds().size;
        
        CGRect windowFrame = CGRectMake(0, screenSize.height - kWarningHeight, screenSize.width, kWarningHeight);
        _warningWindow = [[LUWindow alloc] initWithFrame:windowFrame];
        _warningWindow.clipsToBounds = YES;
        
        LUExceptionWarningController *controller = [[LUExceptionWarningController alloc] initWithMessage:message];
        controller.view.frame = _warningWindow.bounds;
        controller.delegate = self;
        _warningWindow.rootViewController = controller;
        
        _warningWindow.hidden = NO;
        
        return YES;
    }
    
    return NO;
}

- (void)hideWarning
{
    if (_warningWindow)
    {
        _warningWindow.hidden = YES;
        _warningWindow = nil;
    }
}

#pragma mark -
#pragma mark Notifications

- (void)registerNotifications
{
    [self registerNotificationName:LUConsoleCheckFullVersionNotification
                          selector:@selector(checkFullVersionNotification:)];
}

- (void)checkFullVersionNotification:(NSNotification *)notification
{
    NSString *source = [notification.userInfo objectForKey:LUConsoleCheckFullVersionNotificationSource];
    LUAssert(source);
    
    if (source)
    {
        NSDictionary *params = @{
            @"category" : @"Full Version",
            @"action"   : [NSString stringWithFormat:@"full_version_%@", source]
        };
        [_scriptMessenger sendMessageName:kScriptMessageTrackEvent params:params];
    }
}

#pragma mark -
#pragma mark LUConsoleLogControllerEntrySource

- (NSInteger)consoleControllerNumberOfEntries:(LUConsoleLogController *)controller
{
    return _console.entriesCount;
}

- (LUConsoleLogEntry *)consoleController:(LUConsoleLogController *)controller entryAtIndex:(NSUInteger)index
{
    return [_console entryAtIndex:index];
}

#pragma mark -
#pragma mark LUConsoleControllerDelegate

- (void)consoleControllerDidOpen:(LUConsoleController *)controller
{
    [_scriptMessenger sendMessageName:kScriptMessageConsoleOpen];
    
    if ([_delegate respondsToSelector:@selector(consolePluginDidOpenController:)]) {
        [_delegate consolePluginDidOpenController:self];
    }
}

- (void)consoleControllerDidClose:(LUConsoleController *)controller
{
    [self hideConsole];
    
    if ([_delegate respondsToSelector:@selector(consolePluginDidCloseController:)]) {
        [_delegate consolePluginDidCloseController:self];
    }
}

#pragma mark -
#pragma mark LUExceptionWarningControllerDelegate

- (void)exceptionWarningControllerDidShow:(LUExceptionWarningController *)controller
{
    [self hideWarning];
    [self showConsole];
}

- (void)exceptionWarningControllerDidDismiss:(LUExceptionWarningController *)controller
{
    [self hideWarning];
}

#pragma mark -
#pragma mark Gesture Recognition

- (void)enableGestureRecognition
{
    LUAssert(_gestureRecognizer == nil);
    if (!_gestureRecognizer && _gesture != LUConsoleGestureNone) // TODO: handle other gesture types
    {
        UISwipeGestureRecognizer *gr = [[UISwipeGestureRecognizer alloc] initWithTarget:self
                                                                                 action:@selector(handleGesture:)];
        gr.numberOfTouchesRequired = 2;
        gr.direction = UISwipeGestureRecognizerDirectionDown;
        
        [[self keyWindow] addGestureRecognizer:gr];
        
        _gestureRecognizer = gr;
    }
}

- (void)disableGestureRecognition
{
    if (_gestureRecognizer != nil)
    {
        [[self keyWindow] removeGestureRecognizer:_gestureRecognizer];
        _gestureRecognizer = nil;
    }
}

- (void)handleGesture:(UIGestureRecognizer *)gr
{
    if (gr.state == UIGestureRecognizerStateEnded)
    {
        [self showConsole];
    }
}

- (LUConsoleGesture)gestureFromString:(NSString *)gestureName
{
    if ([gestureName isEqualToString:@"SwipeDown"])
    {
        return LUConsoleGestureSwipe;
    }
    
    return LUConsoleGestureNone;
}

#pragma mark -
#pragma mark Script Messanger

- (void)sendScriptMessageName:(NSString *)name
{
    [_scriptMessenger sendMessageName:name];
}

- (void)sendScriptMessageName:(NSString *)name params:(NSDictionary *)params
{
    [_scriptMessenger sendMessageName:name params:params];
}

#pragma mark -
#pragma mark Properties

- (UIWindow *)keyWindow
{
    return [UIApplication sharedApplication].keyWindow;
}

- (NSInteger)capacity
{
    return _console.capacity;
}

- (void)setCapacity:(NSInteger)capacity
{
    NSInteger trimCount = _console.trimmedCount;
    
    _console = [[LUConsole alloc] initWithCapacity:capacity trimCount:trimCount];
}

- (NSInteger)trim
{
    return _console.trimCount;
}

- (void)setTrim:(NSInteger)trim
{
    NSInteger capacity = _console.capacity;
    _console = [[LUConsole alloc] initWithCapacity:capacity trimCount:trim];
}

@end

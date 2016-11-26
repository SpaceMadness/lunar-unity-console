//
//  LUConsolePlugin.m
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2016 Alex Lementuev, SpaceMadness.
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

static const NSTimeInterval kWindowAnimationDuration = 0.4f;
static const CGFloat kWarningHeight = 45.0f;

static NSString * const kScriptMessageConsoleOpen  = @"console_open";
static NSString * const kScriptMessageConsoleClose = @"console_close";
static NSString * const kSettingsFilename          = @"com.spacemadness.LunarMobileConsoleSettings.bin";

@interface LUConsolePlugin () <LUConsoleLogControllerDelegate, LUExceptionWarningControllerDelegate>
{
    LUUnityScriptMessenger  * _scriptMessenger;
    NSString                * _version;
    UIGestureRecognizer     * _gestureRecognizer;
    LUConsoleGesture          _gesture;
}

@end

@implementation LUConsolePlugin

- (instancetype)initWithTargetName:(NSString *)targetName
                        methodName:(NSString *)methodName
                           version:(NSString *)version
                       capacity:(NSUInteger)capacity
                      trimCount:(NSUInteger)trimCount
                    gestureName:(NSString *)gestureName
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
        
        _scriptMessenger = [[LUUnityScriptMessenger alloc] initWithTargetName:targetName methodName:methodName];
        _version = version;
        _console = [[LUConsole alloc] initWithCapacity:capacity trimCount:trimCount];
        _gesture = [self gestureFromString:gestureName];
        
        NSArray *searchPaths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
        NSString *documentPath = [searchPaths objectAtIndex:0];
        NSString *settingsFile = [documentPath stringByAppendingPathComponent:kSettingsFilename];
        _settings = [LUConsolePluginSettings settingsWithContentsOfFile:settingsFile];
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

- (void)show
{
    [self hideOverlay];
    
    if (_consoleWindow == nil)
    {
        LUConsoleLogController *controller = [LUConsoleLogController controllerWithPlugin:self];
        controller.version = _version;
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

- (void)hideOverlay
{
    if (_overlayWindow != nil)
    {
        _overlayWindow.rootViewController = nil;
        _overlayWindow.hidden = YES;
        _overlayWindow = nil;
    }
}

- (void)showOverlay
{
    if (_overlayWindow == nil)
    {
        LUConsoleOverlayControllerSettings *settings = [LUConsoleOverlayControllerSettings settings];
        LUConsoleOverlayController *controller = [LUConsoleOverlayController controllerWithConsole:_console
                                                                                          settings:settings];
        
        CGRect windowFrame = LUGetScreenBounds();
        _overlayWindow = [[LUWindow alloc] initWithFrame:windowFrame];
        _overlayWindow.userInteractionEnabled = NO;
        _overlayWindow.rootViewController = controller;
        _overlayWindow.opaque = YES;
        _overlayWindow.hidden = NO;
    }
}

- (void)hide
{
    if (_consoleWindow != nil)
    {
        [self unregisterNotifications];
        [self enableGestureRecognition];
        
        CGRect windowFrame = _consoleWindow.frame;
        windowFrame.origin.y -= CGRectGetHeight(windowFrame);
        
        LUWindow * window = _consoleWindow; // don't capture self in the block
        [UIView animateWithDuration:kWindowAnimationDuration animations:^{
            window.frame = windowFrame;
        } completion:^(BOOL finished) {
            window.hidden = YES;
        }];
        
        _consoleWindow = nil;
    }
    
    if (_settings.enableTransparentLogOverlay)
    {
        [self showOverlay];
    }
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
    [self registerNotificationName:UIDeviceOrientationDidChangeNotification
                          selector:@selector(deviceOrientationDidChangeNotification:)];
}

- (void)deviceOrientationDidChangeNotification:(NSNotification *)notification
{
    // TODO: resize window
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
#pragma mark LUConsoleLogControllerDelegate

- (void)consoleControllerDidOpen:(LUConsoleLogController *)controller
{
    [_scriptMessenger sendMessageName:kScriptMessageConsoleOpen];
}

- (void)consoleControllerDidClose:(LUConsoleLogController *)controller
{
    [self hide];
    
    [_scriptMessenger sendMessageName:kScriptMessageConsoleClose];
}

#pragma mark -
#pragma mark LUExceptionWarningControllerDelegate

- (void)exceptionWarningControllerDidShow:(LUExceptionWarningController *)controller
{
    [self hideWarning];
    [self show];
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
        [self show];
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

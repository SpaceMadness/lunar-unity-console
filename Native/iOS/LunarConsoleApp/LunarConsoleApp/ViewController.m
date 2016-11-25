//
//  ViewController.m
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

#import "ViewController.h"

#import "Lunar.h"
#import "FakeLogEntry.h"

static const NSUInteger kConsoleCapacity  = 4096;
static const NSUInteger kConsoleTrimCount = 512;

static LU_WEAK LUConsolePlugin * _pluginInstance;

void UnitySendMessage(const char *objectName, const char *methodName, const char *message)
{
    NSLog(@"Send native message: %s.%s(%s)", objectName, methodName, message);
}

@interface ViewController () <UITextFieldDelegate>
{
    NSUInteger _index;
}

@property (nonatomic, assign) IBOutlet UITextField * messageText;
@property (nonatomic, assign) IBOutlet UITextField * capacityText;
@property (nonatomic, assign) IBOutlet UITextField * trimText;

@property (nonatomic, strong) NSArray * logEntries;

@end

@implementation ViewController

- (void)dealloc
{
    _pluginInstance = nil;
    [_plugin release];
    [super dealloc];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    _plugin = [[LUConsolePlugin alloc] initWithTargetName:@"LunarConsole"
                                               methodName:@"OnNativeMessage"
                                                  version:@"0.0.0b"
                                                 capacity:kConsoleCapacity
                                                trimCount:kConsoleTrimCount
                                              gestureName:@"SwipeDown"];
    
    _capacityText.text = [NSString stringWithFormat:@"%ld", (long) kConsoleCapacity];
    _trimText.text = [NSString stringWithFormat:@"%ld", (long) kConsoleTrimCount];
    
    _pluginInstance = _plugin;
    
}

- (BOOL)prefersStatusBarHidden
{
    return YES;
}

#pragma mark -
#pragma mark Actions

- (IBAction)onToggleLogger:(id)sender
{
    [self showConsoleController];
    
    _index = 0;
    self.logEntries = [self loadLogEntries];
    
    [self logNextMessage];
}

- (IBAction)onShowController:(id)sender
{
    [self showConsoleController];
}

- (IBAction)onShowOverlay:(id)sender
{
    [self showOverlay];
}

- (IBAction)onShowAlert:(id)sender
{
    [self showAlert];
}

- (IBAction)onLogDebug:(id)sender
{
    [self logMessageLevel:LUConsoleLogTypeLog];
}

- (IBAction)onLogWarning:(id)sender
{
    [self logMessageLevel:LUConsoleLogTypeWarning];
}

- (IBAction)onLogError:(id)sender
{
    [self logMessageLevel:LUConsoleLogTypeError];
}

- (IBAction)onSetCapacity:(id)sender
{
    NSInteger capacity = [_capacityText.text integerValue];
    if (capacity > 0)
    {
        _plugin.capacity = capacity;
    }
}

- (IBAction)onSetTrim:(id)sender
{
    NSInteger trim = [_trimText.text integerValue];
    if (trim > 0)
    {
        _plugin.trim = trim;
    }
}

#pragma mark -
#pragma mark Helpers

- (void)showConsoleController
{
    [_plugin show];
}

- (void)showOverlay
{
    [_plugin showOverlay];
}

- (void)showAlert
{
    NSString *message = @"Exception: Error!";
    NSString *stackTrace = @"Logger.LogError () (at Assets/Logger.cs:15)\n \
    UnityEngine.Events.InvokableCall.Invoke (System.Object[] args)\n \
    UnityEngine.Events.InvokableCallList.Invoke (System.Object[] parameters)\n \
    UnityEngine.Events.UnityEventBase.Invoke (System.Object[] parameters)\n \
    UnityEngine.Events.UnityEvent.Invoke ()\n \
    UnityEngine.UI.Button.Press () (at /Users/builduser/buildslave/unity/build/Extensions/guisystem/UnityEngine.UI/UI/Core/Button.cs:35)\n \
    UnityEngine.UI.Button.OnPointerClick (UnityEngine.EventSystems.PointerEventData eventData) (at /Users/builduser/buildslave/unity/build/Extensions/guisystem/UnityEngine.UI/UI/Core/Button.cs:44)\n \
    UnityEngine.EventSystems.ExecuteEvents.Execute (IPointerClickHandler handler, UnityEngine.EventSystems.BaseEventData eventData) (at /Users/builduser/buildslave/unity/build/Extensions/guisystem/UnityEngine.UI/EventSystem/ExecuteEvents.cs:52)\n \
    UnityEngine.EventSystems.ExecuteEvents.Execute[IPointerClickHandler] (UnityEngine.GameObject target, UnityEngine.EventSystems.BaseEventData eventData, UnityEngine.EventSystems.EventFunction`1 functor) (at /Users/builduser/buildslave/unity/build/Extensions/guisystem/UnityEngine.UI/EventSystem/ExecuteEvents.cs:269)\n \
    UnityEngine.EventSystems.EventSystem:Update()";
    
    [_plugin logMessage:message stackTrace:stackTrace type:LUConsoleLogTypeException];
}

- (void)logNextMessage
{
    FakeLogEntry *entry = [_logEntries objectAtIndex:_index];
    
    [_plugin logMessage:entry.message
             stackTrace:entry.stacktrace
                   type:entry.type];
    
    _index = (_index + 1) % _logEntries.count;
    
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.2 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [self logNextMessage];
    });
}

- (void)logMessageLevel:(LUConsoleLogType)logType
{
    [self logMessage:_messageText.text stackTrace:nil type:logType];
}

- (void)logMessage:(NSString *)message stackTrace:(NSString *)stackTrace type:(LUConsoleLogType)type
{
    [_plugin logMessage:message stackTrace:stackTrace type:type];
}

#pragma mark -
#pragma mark Log entries

- (NSArray *)loadLogEntries
{
    NSString *path = [[NSBundle mainBundle] pathForResource:@"input" ofType:@"txt"];
    NSData *data = [NSData dataWithContentsOfFile:path];
    NSArray *objects = [NSJSONSerialization JSONObjectWithData:data options:0 error:nil];
    
    NSMutableArray *entries = [NSMutableArray array];
    for (NSDictionary *object in objects)
    {
        NSString *level = [object objectForKey:@"level"];
        NSString *message = [object objectForKey:@"message"];
        NSString *stacktrace = [object objectForKey:@"stacktrace"];
        
        LUConsoleLogType type = LUConsoleLogTypeLog;
        if ([level isEqualToString:@"ERROR"]) type = LUConsoleLogTypeError;
        else if ([level isEqualToString:@"WARNING"]) type = LUConsoleLogTypeWarning;
        
        FakeLogEntry *entry = [[FakeLogEntry alloc] initWithType:type message:message stackTrace:stacktrace];
        [entries addObject:entry];
        [entry release];
    }
    return entries;
}

#pragma mark -
#pragma mark UITextFieldDelegate

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    return NO;
}

#pragma mark -
#pragma mark Properties

+ (LUConsolePlugin *)pluginInstance
{
    return _pluginInstance;
}

@end

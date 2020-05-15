//
//  LUConsoleTest.m
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2019 Alex Lementuev, SpaceMadness.
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

#import "TestCase.h"

@interface LUConsole (Testing)

- (void)logText:(NSString *)message stackTrace:(NSString *)stackTrace type:(LUConsoleLogType)type;

@end

@implementation LUConsole (Testing)

- (void)logText:(NSString *)message stackTrace:(NSString *)stackTrace type:(LUConsoleLogType)type
{
    [self logMessage:[[LULogMessage alloc] initWithText:message attributedText:nil] stackTrace:stackTrace type:type];
}

@end

@interface LUConsoleTest : TestCase <LunarConsoleDelegate>

@end

@implementation LUConsoleTest

- (void)testAddEntry
{
    LUConsole *console = [[LUConsole alloc] initWithCapacity:100 trimCount:10];
    console.delegate = self;
    
    [console logText:@"1" stackTrace:@"s1" type:LUConsoleLogTypeLog];
    [console logText:@"2" stackTrace:@"s2" type:LUConsoleLogTypeLog];
    [console logText:@"3" stackTrace:@"s3" type:LUConsoleLogTypeLog];
    
    [self assertResult:@"1", @"2", @"3", nil];
    
    [console release];
}

- (void)testTrim
{
    LUConsole *console = [[LUConsole alloc] initWithCapacity:5 trimCount:3];
    console.delegate = self;
    
    [console logText:@"1" stackTrace:@"s1" type:LUConsoleLogTypeLog];
    [console logText:@"2" stackTrace:@"s2" type:LUConsoleLogTypeLog];
    [console logText:@"3" stackTrace:@"s3" type:LUConsoleLogTypeLog];
    [console logText:@"4" stackTrace:@"s4" type:LUConsoleLogTypeLog];
    [console logText:@"5" stackTrace:@"s5" type:LUConsoleLogTypeLog];
    [console logText:@"6" stackTrace:@"s6" type:LUConsoleLogTypeLog];
    [console logText:@"7" stackTrace:@"s7" type:LUConsoleLogTypeLog];
    
    [self assertResult:@"4", @"5", @"6", @"7", nil];
    
    [console release];
}

- (void)testClear
{
    LUConsole *console = [[LUConsole alloc] initWithCapacity:10 trimCount:3];
    console.delegate = self;
    
    [console logText:@"1" stackTrace:@"s1" type:LUConsoleLogTypeLog];
    [console logText:@"2" stackTrace:@"s2" type:LUConsoleLogTypeLog];
    [console logText:@"3" stackTrace:@"s3" type:LUConsoleLogTypeLog];
    [console logText:@"4" stackTrace:@"s4" type:LUConsoleLogTypeLog];
    [console logText:@"5" stackTrace:@"s5" type:LUConsoleLogTypeLog];
    
    [console clear];
    
    [self assertResult:nil];
    
    [console release];
}

#pragma mark -
#pragma mark LunarConsoleDelegate

- (void)lunarConsole:(LUConsole *)console didAddEntryAtIndex:(NSInteger)index trimmedCount:(NSUInteger)trimmedCount
{
    [self addResult:[console entryAtIndex:index].message.text];
}

- (void)lunarConsole:(LUConsole *)console didUpdateEntryAtIndex:(NSInteger)index trimmedCount:(NSUInteger)trimmedCount
{
    // FIXME: handle this!
}

- (void)lunarConsole:(LUConsole *)console didRemoveRange:(NSRange )range
{
    [self.result removeObjectsInRange:range];
}

- (void)lunarConsoleDidClearEntries:(LUConsole *)console
{
    [self.result removeAllObjects];
}

@end

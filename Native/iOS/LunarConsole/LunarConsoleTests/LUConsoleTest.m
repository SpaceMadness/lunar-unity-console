//
//  LUConsoleTest.m
//  LunarConsole
//
//  Created by Alex Lementuev on 10/22/15.
//  Copyright (c) 2015 Space Madness. All rights reserved.
//

#import "TestCase.h"

@interface LUConsoleTest : TestCase <LunarConsoleDelegate>

@end

@implementation LUConsoleTest

- (void)testAddEntry
{
    LUConsole *console = [[LUConsole alloc] initWithCapacity:100 trimCount:10];
    console.delegate = self;
    
    [console logMessage:@"1" stackTrace:@"s1" type:LUConsoleLogTypeLog];
    [console logMessage:@"2" stackTrace:@"s2" type:LUConsoleLogTypeLog];
    [console logMessage:@"3" stackTrace:@"s3" type:LUConsoleLogTypeLog];
    
    [self assertResult:@"1", @"2", @"3", nil];
    
    LU_RELEASE(console);
}

- (void)testTrim
{
    LUConsole *console = [[LUConsole alloc] initWithCapacity:5 trimCount:3];
    console.delegate = self;
    
    [console logMessage:@"1" stackTrace:@"s1" type:LUConsoleLogTypeLog];
    [console logMessage:@"2" stackTrace:@"s2" type:LUConsoleLogTypeLog];
    [console logMessage:@"3" stackTrace:@"s3" type:LUConsoleLogTypeLog];
    [console logMessage:@"4" stackTrace:@"s4" type:LUConsoleLogTypeLog];
    [console logMessage:@"5" stackTrace:@"s5" type:LUConsoleLogTypeLog];
    [console logMessage:@"6" stackTrace:@"s6" type:LUConsoleLogTypeLog];
    [console logMessage:@"7" stackTrace:@"s7" type:LUConsoleLogTypeLog];
    
    [self assertResult:@"4", @"5", @"6", @"7", nil];
    
    LU_RELEASE(console);
}

- (void)testClear
{
    LUConsole *console = [[LUConsole alloc] initWithCapacity:10 trimCount:3];
    console.delegate = self;
    
    [console logMessage:@"1" stackTrace:@"s1" type:LUConsoleLogTypeLog];
    [console logMessage:@"2" stackTrace:@"s2" type:LUConsoleLogTypeLog];
    [console logMessage:@"3" stackTrace:@"s3" type:LUConsoleLogTypeLog];
    [console logMessage:@"4" stackTrace:@"s4" type:LUConsoleLogTypeLog];
    [console logMessage:@"5" stackTrace:@"s5" type:LUConsoleLogTypeLog];
    
    [console clear];
    
    [self assertResult:nil];
    
    LU_RELEASE(console);
}

#pragma mark -
#pragma mark LunarConsoleDelegate

- (void)lunarConsole:(LUConsole *)console didAddEntry:(LUConsoleEntry *)entry filtered:(BOOL)filtered
{
    [self addResult:entry.message];
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

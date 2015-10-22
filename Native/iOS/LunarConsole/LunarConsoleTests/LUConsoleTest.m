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
    LUConsole *console = [[LUConsole alloc] initWithCapacity:100];
    console.delegate = self;
    
    [console logMessage:@"1" stackTrace:@"s1" type:LUConsoleLogTypeLog];
    [console logMessage:@"2" stackTrace:@"s2" type:LUConsoleLogTypeLog];
    [console logMessage:@"3" stackTrace:@"s3" type:LUConsoleLogTypeLog];
    
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
}

- (void)lunarConsoleDidClearEntries:(LUConsole *)console
{
}

@end

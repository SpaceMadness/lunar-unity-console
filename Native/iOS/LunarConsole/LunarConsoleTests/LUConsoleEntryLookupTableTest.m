//
//  LUConsoleEntryLookupTableTest.m
//  LunarConsole
//
//  Created by Alex Lementuev on 1/27/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import <XCTest/XCTest.h>

#import "Lunar.h"

@interface LUConsoleEntryLookupTableTest : XCTestCase
{
    LUConsoleEntryLookupTable * _table;
}

@end

@implementation LUConsoleEntryLookupTableTest

- (void)setUp
{
    [super setUp];
    _table = [LUConsoleEntryLookupTable new];
}

- (void)tearDown
{
    LU_RELEASE(_table);
    [super tearDown];
}

- (void)testAddEntry
{
    LUConsoleCollapsedEntry *entry = [self addEntryMessage:@"message1"];
    XCTAssertEqual(1, entry.count);
    
    entry = [self addEntryMessage:@"message1"];
    XCTAssertEqual(2, entry.count);
    
    entry = [self addEntryMessage:@"message2"];
    XCTAssertEqual(1, entry.count);
}

#pragma mark -
#pragma mark Helpers

- (LUConsoleCollapsedEntry *)addEntryMessage:(NSString *)message
{
    return [self addEntryType:LUConsoleLogTypeLog message:message stackTrace:@""];
}

- (LUConsoleCollapsedEntry *)addEntryType:(LUConsoleLogType)type
                                  message:(NSString *)message
                               stackTrace:(NSString *)stackTrace
{
    LUConsoleEntry *entry = [LUConsoleEntry entryWithType:type
                                                  message:message
                                               stackTrace:stackTrace];
    return [_table addEntry:entry];
}

@end

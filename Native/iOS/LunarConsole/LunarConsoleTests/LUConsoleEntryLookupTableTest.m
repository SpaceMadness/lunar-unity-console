//
//  LUConsoleEntryLookupTableTest.m
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

#import <XCTest/XCTest.h>

#import "Lunar.h"

@interface LUConsoleEntryLookupTableTest : XCTestCase
{
    LUConsoleLogEntryLookupTable * _table;
}

@end

@implementation LUConsoleEntryLookupTableTest

- (void)setUp
{
    [super setUp];
    _table = [LUConsoleLogEntryLookupTable new];
}

- (void)tearDown
{
    [_table release];
    [super tearDown];
}

- (void)testAddEntry
{
    LUConsoleCollapsedLogEntry *entry = [self addEntryMessage:@"message1"];
    XCTAssertEqual(1, entry.count);
    
    entry = [self addEntryMessage:@"message1"];
    XCTAssertEqual(2, entry.count);
    
    entry = [self addEntryMessage:@"message2"];
    XCTAssertEqual(1, entry.count);
}

#pragma mark -
#pragma mark Helpers

- (LUConsoleCollapsedLogEntry *)addEntryMessage:(NSString *)message
{
    return [self addEntryType:LUConsoleLogTypeLog message:message stackTrace:@""];
}

- (LUConsoleCollapsedLogEntry *)addEntryType:(LUConsoleLogType)type
                                  message:(NSString *)message
                               stackTrace:(NSString *)stackTrace
{
    LUConsoleLogEntry *entry = [LUConsoleLogEntry entryWithType:type
                                                  message:message
                                               stackTrace:stackTrace];
    return [_table addEntry:entry];
}

@end

//
//  LUConsoleEntryListTest.m
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

#import <UIKit/UIKit.h>
#import <XCTest/XCTest.h>

#import "Lunar.h"

const static NSUInteger kDefaultCapacity = 100;
const static NSUInteger kDefaultTrim = 1;

static LUConsoleEntryList *createEntryListWithMessages(NSUInteger capacity, NSUInteger trimCount, NSString *first, ...) NS_REQUIRES_NIL_TERMINATION;
static LUConsoleEntryList *createEntryListWithEntries(NSUInteger capacity, NSUInteger trimCount, LUConsoleEntry *first, ...) NS_REQUIRES_NIL_TERMINATION;

@interface LUConsoleEntry (Testing)

+ (instancetype)entryWithType:(LUConsoleLogType)type message:(NSString *)message;
+ (instancetype)entryWithMessage:(NSString *)message;

@end

@interface LUConsoleEntryList (Testing)

- (LUConsoleCollapsedEntry *)collapsedEntryAtIndex:(NSInteger)index;

@end

@interface LUConsoleEntryListTest : XCTestCase

@end

@implementation LUConsoleEntryListTest

#pragma mark -
#pragma mark Filter by text

- (void)testFilteringByText
{
    LUConsoleEntryList *list = createEntryListWithMessages(kDefaultCapacity, kDefaultTrim,
        @"line1",
        @"line11",
        @"line111",
        @"line1111",
        @"foo", nil);
    
    XCTAssert(!list.isFiltering);
    
    XCTAssert([list setFilterByText:@"l"]);
    [self listAssertMessages:list, @"line1", @"line11", @"line111", @"line1111", nil];
    XCTAssert(list.isFiltering);
    
    XCTAssert(![list setFilterByText:@"l"]);
    [self listAssertMessages:list, @"line1", @"line11", @"line111", @"line1111", nil];
    XCTAssert(list.isFiltering);
    
    XCTAssert([list setFilterByText:@"li"]);
    [self listAssertMessages:list, @"line1", @"line11", @"line111", @"line1111", nil];
    XCTAssert(list.isFiltering);
    
    XCTAssert([list setFilterByText:@"lin"]);
    [self listAssertMessages:list, @"line1", @"line11", @"line111", @"line1111", nil];
    XCTAssert(list.isFiltering);
    
    XCTAssert([list setFilterByText:@"line"]);
    [self listAssertMessages:list, @"line1", @"line11", @"line111", @"line1111", nil];
    XCTAssert(list.isFiltering);
    
    XCTAssert([list setFilterByText:@"line1"]);
    [self listAssertMessages:list, @"line1", @"line11", @"line111", @"line1111", nil];
    XCTAssert(list.isFiltering);
    
    XCTAssert([list setFilterByText:@"line11"]);
    [self listAssertMessages:list, @"line11", @"line111", @"line1111", nil];
    XCTAssert(list.isFiltering);
    
    XCTAssert([list setFilterByText:@"line111"]);
    [self listAssertMessages:list, @"line111", @"line1111", nil];
    XCTAssert(list.isFiltering);
    
    XCTAssert([list setFilterByText:@"line1111"]);
    [self listAssertMessages:list, @"line1111", nil];
    XCTAssert(list.isFiltering);
    
    XCTAssert([list setFilterByText:@"line11111"]);
    [self listAssertMessages:list, nil];
    XCTAssert(list.isFiltering);
    
    XCTAssert([list setFilterByText:@"line1111"]);
    [self listAssertMessages:list, @"line1111", nil];
    XCTAssert(list.isFiltering);
    
    XCTAssert([list setFilterByText:@"line111"]);
    [self listAssertMessages:list, @"line111", @"line1111", nil];
    XCTAssert(list.isFiltering);
    
    XCTAssert([list setFilterByText:@"line11"]);
    [self listAssertMessages:list, @"line11", @"line111", @"line1111", nil];
    XCTAssert(list.isFiltering);
    
    XCTAssert([list setFilterByText:@"line1"]);
    [self listAssertMessages:list, @"line1", @"line11", @"line111", @"line1111", nil];
    XCTAssert(list.isFiltering);
    
    XCTAssert([list setFilterByText:@"line"]);
    [self listAssertMessages:list, @"line1", @"line11", @"line111", @"line1111", nil];
    XCTAssert(list.isFiltering);
    
    XCTAssert([list setFilterByText:@"lin"]);
    [self listAssertMessages:list, @"line1", @"line11", @"line111", @"line1111", nil];
    XCTAssert(list.isFiltering);
    
    XCTAssert([list setFilterByText:@"li"]);
    [self listAssertMessages:list, @"line1", @"line11", @"line111", @"line1111", nil];
    XCTAssert(list.isFiltering);
    
    XCTAssert([list setFilterByText:@"l"]);
    [self listAssertMessages:list, @"line1", @"line11", @"line111", @"line1111", nil];
    XCTAssert(list.isFiltering);
    
    XCTAssert([list setFilterByText:@""]);
    [self listAssertMessages:list, @"line1", @"line11", @"line111", @"line1111", @"foo", nil];
    XCTAssert(!list.isFiltering);
}

#pragma mark -
#pragma mark Filter by log type

- (void)testFilteringByLogType
{
    LUConsoleEntryList *list = createEntryListWithEntries(kDefaultCapacity, kDefaultTrim,
       [LUConsoleEntry entryWithType:LUConsoleLogTypeError message:@"error1"],
       [LUConsoleEntry entryWithType:LUConsoleLogTypeError message:@"error2"],
       [LUConsoleEntry entryWithType:LUConsoleLogTypeAssert message:@"assert1"],
       [LUConsoleEntry entryWithType:LUConsoleLogTypeAssert message:@"assert2"],
       [LUConsoleEntry entryWithType:LUConsoleLogTypeWarning message:@"warning1"],
       [LUConsoleEntry entryWithType:LUConsoleLogTypeWarning message:@"warning2"],
       [LUConsoleEntry entryWithType:LUConsoleLogTypeLog message:@"log1"],
       [LUConsoleEntry entryWithType:LUConsoleLogTypeLog message:@"log2"],
       [LUConsoleEntry entryWithType:LUConsoleLogTypeException message:@"exception1"],
       [LUConsoleEntry entryWithType:LUConsoleLogTypeException message:@"exception2"], nil);
    
    XCTAssert(!list.isFiltering);
    
    XCTAssert([list setFilterByLogType:LUConsoleLogTypeError disabled:YES]);
    [self listAssertMessages:list,
        @"assert1", @"assert2",
        @"warning1", @"warning2",
        @"log1", @"log2",
        @"exception1", @"exception2", nil];
    XCTAssert(list.isFiltering);
    
    XCTAssert(![list setFilterByLogType:LUConsoleLogTypeError disabled:YES]);
    [self listAssertMessages:list,
        @"assert1", @"assert2",
        @"warning1", @"warning2",
        @"log1", @"log2",
        @"exception1", @"exception2", nil];
    XCTAssert(list.isFiltering);
    
    XCTAssert([list setFilterByLogType:LUConsoleLogTypeAssert disabled:YES]);
    [self listAssertMessages:list,
        @"warning1", @"warning2",
        @"log1", @"log2",
        @"exception1", @"exception2", nil];
    XCTAssert(list.isFiltering);
    
    XCTAssert([list setFilterByLogType:LUConsoleLogTypeWarning disabled:YES]);
    [self listAssertMessages:list,
        @"log1", @"log2",
        @"exception1", @"exception2", nil];
    XCTAssert(list.isFiltering);
    
    XCTAssert([list setFilterByLogType:LUConsoleLogTypeLog disabled:YES]);
    [self listAssertMessages:list,
        @"exception1", @"exception2", nil];
    XCTAssert(list.isFiltering);
    
    XCTAssert([list setFilterByLogType:LUConsoleLogTypeException disabled:YES]);
    [self listAssertMessages:list, nil];
    XCTAssert(list.isFiltering);
    
    XCTAssert([list setFilterByLogType:LUConsoleLogTypeException disabled:NO]);
    [self listAssertMessages:list,
        @"exception1", @"exception2", nil];
    XCTAssert(list.isFiltering);
    
    XCTAssert([list setFilterByLogType:LUConsoleLogTypeLog disabled:NO]);
    [self listAssertMessages:list,
        @"log1", @"log2",
        @"exception1", @"exception2", nil];
    XCTAssert(list.isFiltering);
    
    XCTAssert([list setFilterByLogType:LUConsoleLogTypeWarning disabled:NO]);
    [self listAssertMessages:list,
        @"warning1", @"warning2",
        @"log1", @"log2",
        @"exception1", @"exception2", nil];
    XCTAssert(list.isFiltering);
    
    XCTAssert([list setFilterByLogType:LUConsoleLogTypeAssert disabled:NO]);
    [self listAssertMessages:list,
        @"assert1", @"assert2",
        @"warning1", @"warning2",
        @"log1", @"log2",
        @"exception1", @"exception2", nil];
    XCTAssert(list.isFiltering);
    
    XCTAssert(![list setFilterByLogType:LUConsoleLogTypeAssert disabled:NO]);
    [self listAssertMessages:list,
        @"assert1", @"assert2",
        @"warning1", @"warning2",
        @"log1", @"log2",
        @"exception1", @"exception2", nil];
    XCTAssert(list.isFiltering);
    
    XCTAssert([list setFilterByLogType:LUConsoleLogTypeError disabled:NO]);
    [self listAssertMessages:list,
        @"error1", @"error2",
        @"assert1", @"assert2",
        @"warning1", @"warning2",
        @"log1", @"log2",
        @"exception1", @"exception2", nil];
    XCTAssert(!list.isFiltering);
}

#pragma mark -
#pragma mark Filter by log type

- (void)testFilteringByLogTypeMask
{
    LUConsoleEntryList *list = createEntryListWithEntries(kDefaultCapacity, kDefaultTrim,
      [LUConsoleEntry entryWithType:LUConsoleLogTypeError message:@"error1"],
      [LUConsoleEntry entryWithType:LUConsoleLogTypeError message:@"error2"],
      [LUConsoleEntry entryWithType:LUConsoleLogTypeAssert message:@"assert1"],
      [LUConsoleEntry entryWithType:LUConsoleLogTypeAssert message:@"assert2"],
      [LUConsoleEntry entryWithType:LUConsoleLogTypeWarning message:@"warning1"],
      [LUConsoleEntry entryWithType:LUConsoleLogTypeWarning message:@"warning2"],
      [LUConsoleEntry entryWithType:LUConsoleLogTypeLog message:@"log1"],
      [LUConsoleEntry entryWithType:LUConsoleLogTypeLog message:@"log2"],
      [LUConsoleEntry entryWithType:LUConsoleLogTypeException message:@"exception1"],
      [LUConsoleEntry entryWithType:LUConsoleLogTypeException message:@"exception2"], nil);
    
    XCTAssert(!list.isFiltering);
    
    LUConsoleLogTypeMask mask = LU_CONSOLE_LOG_TYPE_MASK(LUConsoleLogTypeError) |
                                LU_CONSOLE_LOG_TYPE_MASK(LUConsoleLogTypeException) |
                                LU_CONSOLE_LOG_TYPE_MASK(LUConsoleLogTypeAssert);
    
    XCTAssert([list setFilterByLogTypeMask:mask disabled:YES]);
    [self listAssertMessages:list,
        @"warning1", @"warning2",
        @"log1", @"log2", nil];
    XCTAssert(list.isFiltering);
    
    mask = LU_CONSOLE_LOG_TYPE_MASK(LUConsoleLogTypeError) |
           LU_CONSOLE_LOG_TYPE_MASK(LUConsoleLogTypeAssert);
    
    XCTAssert([list setFilterByLogTypeMask:mask disabled:NO]);
    [self listAssertMessages:list,
        @"error1", @"error2",
        @"assert1", @"assert2",
        @"warning1", @"warning2",
        @"log1", @"log2", nil];
    XCTAssert(list.isFiltering);
    
    XCTAssert(![list setFilterByLogTypeMask:mask disabled:NO]);
    [self listAssertMessages:list,
        @"error1", @"error2",
        @"assert1", @"assert2",
        @"warning1", @"warning2",
        @"log1", @"log2", nil];
    XCTAssert(list.isFiltering);
    
    mask = LU_CONSOLE_LOG_TYPE_MASK(LUConsoleLogTypeError) |
           LU_CONSOLE_LOG_TYPE_MASK(LUConsoleLogTypeException) |
           LU_CONSOLE_LOG_TYPE_MASK(LUConsoleLogTypeAssert);
    
    XCTAssert([list setFilterByLogTypeMask:mask disabled:NO]);
    [self listAssertMessages:list,
        @"error1", @"error2",
        @"assert1", @"assert2",
        @"warning1", @"warning2",
        @"log1", @"log2",
        @"exception1", @"exception2", nil];
    XCTAssert(!list.isFiltering);
}

#pragma mark -
#pragma mark Collapsing

- (void)testCollapseEntries
{
    LUConsoleEntryList *list = createEntryListWithEntries(kDefaultCapacity, kDefaultTrim,
        [LUConsoleEntry entryWithMessage:@"message1"],
        [LUConsoleEntry entryWithMessage:@"message1"],
        [LUConsoleEntry entryWithMessage:@"message1"],
        [LUConsoleEntry entryWithMessage:@"message12"],
        [LUConsoleEntry entryWithMessage:@"message12"], nil);
    
    list.collapsed = YES;
    XCTAssert(list.isFiltering);
    
    [self listAssertMessages:list, @"message1", @"message12", nil];
    
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:3];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:2];
    
    list.collapsed = NO;
    XCTAssert(!list.isFiltering);
    
    [self listAssertMessages:list, @"message1", @"message1", @"message1", @"message12", @"message12", nil];
}

- (void)testCollapseAddEntries
{
    LUConsoleEntryList *list = createEntryListWithEntries(kDefaultCapacity, kDefaultTrim,
        [LUConsoleEntry entryWithMessage:@"message1"],
        [LUConsoleEntry entryWithMessage:@"message1"],
        [LUConsoleEntry entryWithMessage:@"message1"], nil);
    
    list.collapsed = YES;
    XCTAssert(list.isFiltering);

    [self listAssertMessages:list, @"message1", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:3];
    
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message1"]];
    [self listAssertMessages:list, @"message1", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:4];
    
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message12"]];
    [self listAssertMessages:list, @"message1", @"message12", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:4];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:1];
    
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message2"]];
    [self listAssertMessages:list, @"message1", @"message12", @"message2", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:4];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:1];
    [self list:list assertEntryAt:2 expectedMessage:@"message2" expectedCount:1];
    
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message1"]];
    [self listAssertMessages:list, @"message1", @"message12", @"message2", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:5];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:1];
    [self list:list assertEntryAt:2 expectedMessage:@"message2" expectedCount:1];
    
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message12"]];
    [self listAssertMessages:list, @"message1", @"message12", @"message2", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:5];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:2];
    [self list:list assertEntryAt:2 expectedMessage:@"message2" expectedCount:1];
    
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message2"]];
    [self listAssertMessages:list, @"message1", @"message12", @"message2", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:5];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:2];
    [self list:list assertEntryAt:2 expectedMessage:@"message2" expectedCount:2];
    
    list.collapsed = NO;
    XCTAssert(!list.isFiltering);
    
    [self listAssertMessages:list,
     @"message1",
     @"message1",
     @"message1",
     @"message1",
     @"message12",
     @"message2",
     @"message1",
     @"message12",
     @"message2", nil];
}

- (void)testCollapseAddEntriesOverflow
{
    LUConsoleEntryList *list = createEntryListWithEntries(3, 1,
      [LUConsoleEntry entryWithMessage:@"message1"],
      [LUConsoleEntry entryWithMessage:@"message1"],
      [LUConsoleEntry entryWithMessage:@"message1"], nil);
    
    list.collapsed = YES;
    XCTAssert(list.isFiltering);
    
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message1"]];
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message1"]];
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message1"]];
    
    [self listAssertMessages:list, @"message1", nil];
    
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:6];
    
    list.collapsed = NO;
    XCTAssert(!list.isFiltering);
    
    [self listAssertMessages:list, @"message1", @"message1", @"message1", nil];
}

- (void)testCollapseAddEntriesOverflowDistinctive
{
    LUConsoleEntryList *list = createEntryListWithEntries(3, 1,
      [LUConsoleEntry entryWithMessage:@"message1"],
      [LUConsoleEntry entryWithMessage:@"message1"],
      [LUConsoleEntry entryWithMessage:@"message1"], nil);
    
    list.collapsed = YES;
    XCTAssert(list.isFiltering);
    
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message12"]];
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message12"]];
    
    [self listAssertMessages:list, @"message1", @"message12", nil];
    
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:3];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:2];
    
    list.collapsed = NO;
    XCTAssert(!list.isFiltering);
    
    [self listAssertMessages:list, @"message1", @"message12", @"message12", nil];
}

- (void)testCollapseFilteredEntries
{
    LUConsoleEntryList *list = createEntryListWithEntries(kDefaultCapacity, kDefaultTrim,
      [LUConsoleEntry entryWithMessage:@"message1"],
      [LUConsoleEntry entryWithMessage:@"message12"],
      [LUConsoleEntry entryWithMessage:@"message2"],
      [LUConsoleEntry entryWithMessage:@"message1"],
      [LUConsoleEntry entryWithMessage:@"message12"],
      [LUConsoleEntry entryWithMessage:@"message2"],
      [LUConsoleEntry entryWithMessage:@"message1"],
      [LUConsoleEntry entryWithMessage:@"message12"],
      [LUConsoleEntry entryWithMessage:@"message2"], nil);
    
    [list setFilterByText:@"message1"];
    XCTAssert(list.isFiltering);
    
    list.collapsed = YES;
    XCTAssert(list.isFiltering);
    
    [self listAssertMessages:list, @"message1", @"message12", nil];
    
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:3];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:3];
    
    list.collapsed = NO;
    XCTAssert(list.isFiltering);
    
    [self listAssertMessages:list, @"message1", @"message12", @"message1", @"message12", @"message1", @"message12", nil];
}

- (void)testCollapseAddFilteredEntries
{
    LUConsoleEntryList *list = createEntryListWithEntries(kDefaultCapacity, kDefaultTrim,
      [LUConsoleEntry entryWithMessage:@"message1"],
      [LUConsoleEntry entryWithMessage:@"message1"],
      [LUConsoleEntry entryWithMessage:@"message1"], nil);
    
    [list setFilterByText:@"message1"];
    XCTAssert(list.isFiltering);
    
    list.collapsed = YES;
    XCTAssert(list.isFiltering);
    
    [self listAssertMessages:list, @"message1", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:3];
    
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message1"]];
    [self listAssertMessages:list, @"message1", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:4];
    
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message12"]];
    [self listAssertMessages:list, @"message1", @"message12", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:4];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:1];
    
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message2"]];
    [self listAssertMessages:list, @"message1", @"message12", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:4];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:1];
    
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message1"]];
    [self listAssertMessages:list, @"message1", @"message12", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:5];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:1];
    
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message12"]];
    [self listAssertMessages:list, @"message1", @"message12", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:5];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:2];
    
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message2"]];
    [self listAssertMessages:list, @"message1", @"message12", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:5];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:2];
    
    list.collapsed = NO;
    XCTAssert(list.isFiltering);
    
    [self listAssertMessages:list,
     @"message1",
     @"message1",
     @"message1",
     @"message1",
     @"message12",
     @"message1",
     @"message12", nil];
    
    [list setFilterByText:@""];
    XCTAssert(!list.isFiltering);
    
    [self listAssertMessages:list,
     @"message1",
     @"message1",
     @"message1",
     @"message1",
     @"message12",
     @"message2",
     @"message1",
     @"message12",
     @"message2", nil];
}

- (void)testCollapseAddFilteredEntriesOverflow
{
    LUConsoleEntryList *list = createEntryListWithEntries(3, 1,
      [LUConsoleEntry entryWithMessage:@"message1"],
      [LUConsoleEntry entryWithMessage:@"message1"],
      [LUConsoleEntry entryWithMessage:@"message1"], nil);
    
    [list setFilterByText:@"message1"];
    XCTAssert(list.isFiltering);
    
    list.collapsed = YES;
    XCTAssert(list.isFiltering);
    
    [self listAssertMessages:list, @"message1", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:3];
    
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message1"]];
    [self listAssertMessages:list, @"message1", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:4];
    
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message12"]];
    [self listAssertMessages:list, @"message1", @"message12", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:4];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:1];
    
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message2"]];
    [self listAssertMessages:list, @"message1", @"message12", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:4];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:1];
    
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message1"]];
    [self listAssertMessages:list, @"message1", @"message12", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:5];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:1];
    
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message12"]];
    [self listAssertMessages:list, @"message1", @"message12", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:5];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:2];
    
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message2"]];
    [self listAssertMessages:list, @"message1", @"message12", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:5];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:2];
    
    list.collapsed = NO;
    XCTAssert(list.isFiltering);
    
    [self listAssertMessages:list,
     @"message1",
     @"message12", nil];
    
    [list setFilterByText:@""];
    XCTAssert(!list.isFiltering);
    
    [self listAssertMessages:list,
     @"message1",
     @"message12",
     @"message2", nil];
}

- (void)testFilterCollapsedEntries
{
    LUConsoleEntryList *list = createEntryListWithEntries(kDefaultCapacity, kDefaultTrim,
      [LUConsoleEntry entryWithMessage:@"message1"],
      [LUConsoleEntry entryWithMessage:@"message12"],
      [LUConsoleEntry entryWithMessage:@"message2"],
      [LUConsoleEntry entryWithMessage:@"message1"],
      [LUConsoleEntry entryWithMessage:@"message12"],
      [LUConsoleEntry entryWithMessage:@"message2"],
      [LUConsoleEntry entryWithMessage:@"message1"],
      [LUConsoleEntry entryWithMessage:@"message12"],
      [LUConsoleEntry entryWithMessage:@"message2"], nil);
    
    list.collapsed = YES;
    XCTAssert(list.isFiltering);
    
    [list setFilterByText:@"message1"];
    XCTAssert(list.isFiltering);
    
    [self listAssertMessages:list, @"message1", @"message12", nil];
    
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:3];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:3];
    
    [list setFilterByText:@""];
    XCTAssert(list.isFiltering);
    
    [self listAssertMessages:list, @"message1", @"message12", @"message2", nil];
    
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:3];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:3];
    [self list:list assertEntryAt:2 expectedMessage:@"message2" expectedCount:3];
    
    list.collapsed = NO;
    XCTAssert(!list.isFiltering);
    
    [self listAssertMessages:list,
     @"message1",
     @"message12",
     @"message2",
     @"message1",
     @"message12",
     @"message2",
     @"message1",
     @"message12",
     @"message2", nil];
}

- (void)testFilterCollapsedEntriesAndAddEntries
{
    LUConsoleEntryList *list = createEntryListWithEntries(kDefaultCapacity, kDefaultTrim,
      [LUConsoleEntry entryWithMessage:@"message1"],
      [LUConsoleEntry entryWithMessage:@"message1"],
      [LUConsoleEntry entryWithMessage:@"message1"], nil);
    
    list.collapsed = YES;
    XCTAssert(list.isFiltering);
    
    [list setFilterByText:@"message1"];
    XCTAssert(list.isFiltering);
    
    [self listAssertMessages:list, @"message1", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:3];
    
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message1"]];
    [self listAssertMessages:list, @"message1", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:4];
    
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message12"]];
    [self listAssertMessages:list, @"message1", @"message12", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:4];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:1];
    
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message2"]];
    [self listAssertMessages:list, @"message1", @"message12", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:4];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:1];
    
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message1"]];
    [self listAssertMessages:list, @"message1", @"message12", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:5];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:1];
    
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message12"]];
    [self listAssertMessages:list, @"message1", @"message12", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:5];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:2];
    
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message2"]];
    [self listAssertMessages:list, @"message1", @"message12", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:5];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:2];
    
    [list setFilterByText:@""];
    XCTAssert(list.isFiltering);
    
    [self listAssertMessages:list, @"message1", @"message12", @"message2", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:5];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:2];
    [self list:list assertEntryAt:2 expectedMessage:@"message2" expectedCount:2];
    
    list.collapsed = NO;
    XCTAssert(!list.isFiltering);
    
    [self listAssertMessages:list,
     @"message1",
     @"message1",
     @"message1",
     @"message1",
     @"message12",
     @"message2",
     @"message1",
     @"message12",
     @"message2", nil];
}

- (void)testFilterCollapsedEntriesAndAddEntriesOverflow
{
    LUConsoleEntryList *list = createEntryListWithEntries(3, 1,
      [LUConsoleEntry entryWithMessage:@"message1"],
      [LUConsoleEntry entryWithMessage:@"message1"],
      [LUConsoleEntry entryWithMessage:@"message1"], nil);
    
    list.collapsed = YES;
    XCTAssert(list.isFiltering);
    
    [list setFilterByText:@"message1"];
    XCTAssert(list.isFiltering);
    
    [self listAssertMessages:list, @"message1", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:3];
    
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message1"]];
    [self listAssertMessages:list, @"message1", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:4];
    
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message12"]];
    [self listAssertMessages:list, @"message1", @"message12", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:4];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:1];
    
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message2"]];
    [self listAssertMessages:list, @"message1", @"message12", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:4];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:1];
    
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message1"]];
    [self listAssertMessages:list, @"message1", @"message12", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:5];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:1];
    
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message12"]];
    [self listAssertMessages:list, @"message1", @"message12", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:5];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:2];
    
    [list addEntry:[LUConsoleEntry entryWithMessage:@"message2"]];
    [self listAssertMessages:list, @"message1", @"message12", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:5];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:2];
    
    [list setFilterByText:@""];
    XCTAssert(list.isFiltering);
    
    [self listAssertMessages:list, @"message1", @"message12", @"message2", nil];
    [self list:list assertEntryAt:0 expectedMessage:@"message1" expectedCount:1];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:1];
    [self list:list assertEntryAt:1 expectedMessage:@"message12" expectedCount:1];
    
    list.collapsed = NO;
    XCTAssert(!list.isFiltering);
    
    [self listAssertMessages:list, @"message1", @"message12", @"message2", nil];
}

#pragma mark -
#pragma mark Helpers

- (void)listAssertMessages:(LUConsoleEntryList *)list, ...
{
    va_list ap;
    va_start(ap, list);
    NSMutableArray *expected = [NSMutableArray array];
    for (NSString *message = va_arg(ap, NSString *); message != nil; message = va_arg(ap, NSString *))
    {
        [expected addObject:message];
    }
    va_end(ap);
    
    NSMutableArray *actual = [NSMutableArray array];
    for (int i = 0; i < list.count; ++i)
    {
        [actual addObject:[list entryAtIndex:i].message];
    }
    
    XCTAssertEqual(expected.count, list.count, @"Expected [%@] but was [%@]", [expected componentsJoinedByString:@","], [actual componentsJoinedByString:@","]);
    if (expected.count == actual.count)
    {
        for (int i = 0; i < MIN(expected.count, actual.count); ++i)
        {
            XCTAssertEqualObjects([expected objectAtIndex:i], [actual objectAtIndex:i], @"Expected [%@] but was [%@]", [expected componentsJoinedByString:@","], [actual componentsJoinedByString:@","]);
        }
    }
}

- (void)list:(LUConsoleEntryList *)list assertEntryAt:(NSInteger)index
                                      expectedMessage:(NSString *)expectedMessage
                                        expectedCount:(NSInteger)expectedCount
{
    return [self list:list assertEntryAt:index expectedMessage:expectedMessage expectedCount:expectedCount expectedIndex:index];
}


- (void)list:(LUConsoleEntryList *)list assertEntryAt:(NSInteger)index
                                      expectedMessage:(NSString *)expectedMessage
                                        expectedCount:(NSInteger)expectedCount
                                        expectedIndex:(NSInteger)expectedIndex
{
    LUConsoleCollapsedEntry *entry = [list collapsedEntryAtIndex:index];
    XCTAssertEqualObjects(expectedMessage, entry.message, @"Expected '%@' but was '%@", expectedMessage, entry.message);
    XCTAssertEqual(expectedCount, entry.count);
    XCTAssertEqual(expectedIndex, entry.index);
}

@end

@implementation LUConsoleEntry (Testing)

+ (instancetype)entryWithType:(LUConsoleLogType)type message:(NSString *)message
{
    return [self entryWithType:type message:message stackTrace:nil];
}

+ (instancetype)entryWithMessage:(NSString *)message
{
    return [self entryWithType:LUConsoleLogTypeLog message:message];
}

@end

@implementation LUConsoleEntryList (Testing)

- (LUConsoleCollapsedEntry *)collapsedEntryAtIndex:(NSInteger)index
{
    id entry = [self entryAtIndex:index];
    return [entry isKindOfClass:[LUConsoleCollapsedEntry class]] ? entry : nil;
}

@end

LUConsoleEntryList *createEntryListWithMessages(NSUInteger capacity, NSUInteger trimCount, NSString *first, ...)
{
    LUConsoleEntryList *list = [LUConsoleEntryList listWithCapacity:capacity trimCount:trimCount];
    
    va_list ap;
    va_start(ap, first);
    for (NSString *message = first; message != nil; message = va_arg(ap, NSString *))
    {
        [list addEntry:[LUConsoleEntry entryWithType:LUConsoleLogTypeLog message:message]];
    }
    va_end(ap);
    
    return list;
}

LUConsoleEntryList *createEntryListWithEntries(NSUInteger capacity, NSUInteger trimCount, LUConsoleEntry *first, ...)
{
    LUConsoleEntryList *list = [LUConsoleEntryList listWithCapacity:capacity trimCount:trimCount];
    
    va_list ap;
    va_start(ap, first);
    for (LUConsoleEntry *entry = first; entry != nil; entry = va_arg(ap, LUConsoleEntry *))
    {
        [list addEntry:entry];
    }
    va_end(ap);
    
    return list;
}
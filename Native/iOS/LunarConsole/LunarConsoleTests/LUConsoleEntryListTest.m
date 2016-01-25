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

static LUConsoleEntryList *createEntryListWithMessages(NSString *first, ...) NS_REQUIRES_NIL_TERMINATION;
static LUConsoleEntryList *createEntryListWithEntries(LUConsoleEntry *first, ...) NS_REQUIRES_NIL_TERMINATION;

@interface LUConsoleEntry (Initializer)

+ (instancetype)entryWithType:(LUConsoleLogType)type message:(NSString *)message;

@end

@interface LUConsoleEntryFilteredListTest : XCTestCase

@end

@implementation LUConsoleEntryFilteredListTest

- (void)testFilteringByText
{
    LUConsoleEntryList *list = createEntryListWithMessages(
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

- (void)testFilteringByLogType
{
    LUConsoleEntryList *list = createEntryListWithEntries(
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

- (void)testFilteringByLogTypeMask
{
    LUConsoleEntryList *list = createEntryListWithEntries(
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
#pragma mark Helpers

- (void)listAssertMessages:(LUConsoleEntryList *)list, ...
{
    va_list ap;
    va_start(ap, list);
    NSMutableArray *lines = [NSMutableArray array];
    for (NSString *message = va_arg(ap, NSString *); message != nil; message = va_arg(ap, NSString *))
    {
        [lines addObject:message];
    }
    va_end(ap);
    
    XCTAssertEqual(lines.count, list.count);
    for (int i = 0; i < lines.count; ++i)
    {
        LUConsoleEntry *entry = (LUConsoleEntry *)[list entryAtIndex:i];
        XCTAssertEqual([lines objectAtIndex:i], entry.message);
    }
}

@end

@implementation LUConsoleEntry (Initializer)

+ (instancetype)entryWithType:(LUConsoleLogType)type message:(NSString *)message
{
    return [self entryWithType:type message:message stackTrace:nil];
}

@end

LUConsoleEntryList *createEntryListWithMessages(NSString *first, ...)
{
    LUConsoleEntryList *list = [LUConsoleEntryList listWithCapacity:100 trimCount:1];
    
    va_list ap;
    va_start(ap, first);
    for (NSString *message = first; message != nil; message = va_arg(ap, NSString *))
    {
        [list addEntry:[LUConsoleEntry entryWithType:LUConsoleLogTypeLog message:message]];
    }
    va_end(ap);
    
    return list;
}

LUConsoleEntryList *createEntryListWithEntries(LUConsoleEntry *first, ...)
{
    LUConsoleEntryList *list = [LUConsoleEntryList listWithCapacity:100 trimCount:1];
    
    va_list ap;
    va_start(ap, first);
    for (LUConsoleEntry *entry = first; entry != nil; entry = va_arg(ap, LUConsoleEntry *))
    {
        [list addEntry:entry];
    }
    va_end(ap);
    
    return list;
}
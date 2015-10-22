//
//  LULimitSizeListTest.m
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2015 Alex Lementuev, SpaceMadness.
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

@interface LULimitSizeListTest : XCTestCase

@end

@implementation LULimitSizeListTest

- (void)testAddElements
{
    LULimitSizeList *list = [LULimitSizeList listWithCapacity:10 trimCount:1];
    [list addObject:@"1"];
    [list addObject:@"2"];
    [list addObject:@"3"];

    [self listAssertObjects:list, @"1", @"2", @"3", nil];
}

- (void)testTrimElements
{
    LULimitSizeList *list = [LULimitSizeList listWithCapacity:3 trimCount:2];
    XCTAssertEqual(0, list.count);
    XCTAssertEqual(0, list.totalCount);
    XCTAssertEqual(0, list.trimmedCount);
    XCTAssert(!list.isTrimmed);
    
    [list addObject:@"1"];
    XCTAssertEqual(1, list.count);
    XCTAssertEqual(1, list.totalCount);
    XCTAssertEqual(0, list.trimmedCount);
    XCTAssert(!list.isTrimmed);
    
    [list addObject:@"2"];
    XCTAssertEqual(2, list.count);
    XCTAssertEqual(2, list.totalCount);
    XCTAssertEqual(0, list.trimmedCount);
    XCTAssert(!list.isTrimmed);
    
    [list addObject:@"3"];
    XCTAssertEqual(3, list.count);
    XCTAssertEqual(3, list.totalCount);
    XCTAssertEqual(0, list.trimmedCount);
    XCTAssert(!list.isTrimmed);
    
    [list addObject:@"4"];
    XCTAssertEqual(2, list.count);
    XCTAssertEqual(4, list.totalCount);
    XCTAssertEqual(2, list.trimmedCount);
    XCTAssert(list.isTrimmed);
    
    [self listAssertObjects:list, @"3", @"4", nil];
    
    [list addObject:@"5"];
    XCTAssertEqual(3, list.count);
    XCTAssertEqual(5, list.totalCount);
    XCTAssertEqual(2, list.trimmedCount);
    XCTAssert(list.isTrimmed);
    
    [self listAssertObjects:list, @"3", @"4", @"5", nil];
    
    [list addObject:@"6"];
    XCTAssertEqual(2, list.count);
    XCTAssertEqual(6, list.totalCount);
    XCTAssertEqual(4, list.trimmedCount);
    XCTAssert(list.isTrimmed);
    
    [self listAssertObjects:list, @"5", @"6", nil];
    
    [list addObject:@"7"];
    XCTAssertEqual(3, list.count);
    XCTAssertEqual(7, list.totalCount);
    XCTAssertEqual(4, list.trimmedCount);
    XCTAssert(list.isTrimmed);
    
    [self listAssertObjects:list, @"5", @"6", @"7", nil];
}

- (void)testTrimElementsAndClear
{
    LULimitSizeList *list = [LULimitSizeList listWithCapacity:3 trimCount:2];
    [list addObject:@"1"];
    [list addObject:@"2"];
    [list addObject:@"3"];
    [list addObject:@"4"];
    
    [list removeAllObjects];
    XCTAssertEqual(0, list.count);
    XCTAssertEqual(0, list.totalCount);
    XCTAssertEqual(0, list.trimmedCount);
    XCTAssert(!list.isTrimmed);

    [list addObject:@"5"];
    XCTAssertEqual(1, list.count);
    XCTAssertEqual(1, list.totalCount);
    XCTAssertEqual(0, list.trimmedCount);
    XCTAssert(!list.isTrimmed);
    
    [self listAssertObjects:list, @"5", nil];
    
    [list addObject:@"6"];
    XCTAssertEqual(2, list.count);
    XCTAssertEqual(2, list.totalCount);
    XCTAssertEqual(0, list.trimmedCount);
    XCTAssert(!list.isTrimmed);
    
    [self listAssertObjects:list, @"5", @"6", nil];
    
    [list addObject:@"7"];
    XCTAssertEqual(3, list.count);
    XCTAssertEqual(3, list.totalCount);
    XCTAssertEqual(0, list.trimmedCount);
    XCTAssert(!list.isTrimmed);
    
    [self listAssertObjects:list, @"5", @"6", @"7", nil];
    
    [list addObject:@"8"];
    XCTAssertEqual(2, list.count);
    XCTAssertEqual(4, list.totalCount);
    XCTAssertEqual(2, list.trimmedCount);
    XCTAssert(list.isTrimmed);
    
    [self listAssertObjects:list, @"7", @"8", nil];
}

- (void)listAssertObjects:(LULimitSizeList *)list, ... NS_REQUIRES_NIL_TERMINATION
{
    va_list ap;
    va_start(ap, list);
    
    NSMutableArray *expected = [NSMutableArray array];
    id obj;
    while ((obj = va_arg(ap, id)))
    {
        [expected addObject:obj];
    }
    
    va_end(ap);
    
    XCTAssertEqual(expected.count, list.count);
    for (int i = 0; i < expected.count; ++i)
    {
        XCTAssertEqual(expected[i], list[i]);
    }
}

@end

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

- (void)testAddElementsOverCapacity
{
    LULimitSizeList *list = [LULimitSizeList listWithCapacity:3 trimCount:1];
    XCTAssertEqual(0, list.totalCount);
    XCTAssert(!list.isTrimmed);
    
    [list addObject:@"1"];
    XCTAssertEqual(1, list.totalCount);
    XCTAssert(!list.isTrimmed);
    
    [list addObject:@"2"];
    XCTAssertEqual(2, list.totalCount);
    XCTAssert(!list.isTrimmed);
    
    [list addObject:@"3"];
    XCTAssertEqual(3, list.totalCount);
    XCTAssert(!list.isTrimmed);
    
    [list addObject:@"4"];
    XCTAssertEqual(4, list.totalCount);
    XCTAssert(list.isTrimmed);
    
    [self listAssertObjects:list, @"2", @"3", @"4", nil];
}

- (void)testAddElementsWayOverCapacity
{
    LULimitSizeList *list = [LULimitSizeList listWithCapacity:3 trimCount:1];
    XCTAssertEqual(0, list.totalCount);
    XCTAssert(!list.isTrimmed);
    
    [list addObject:@"1"];
    XCTAssertEqual(1, list.totalCount);
    XCTAssert(!list.isTrimmed);
    
    [list addObject:@"2"];
    XCTAssertEqual(2, list.totalCount);
    XCTAssert(!list.isTrimmed);
    
    [list addObject:@"3"];
    XCTAssertEqual(3, list.totalCount);
    XCTAssert(!list.isTrimmed);
    
    [list addObject:@"4"];
    XCTAssertEqual(4, list.totalCount);
    XCTAssert(list.isTrimmed);
    
    [list addObject:@"5"];
    XCTAssertEqual(5, list.totalCount);
    XCTAssert(list.isTrimmed);
    
    [list addObject:@"6"];
    XCTAssertEqual(6, list.totalCount);
    XCTAssert(list.isTrimmed);
    
    [list addObject:@"7"];
    XCTAssertEqual(7, list.totalCount);
    XCTAssert(list.isTrimmed);
    
    [self listAssertObjects:list, @"5", @"6", @"7", nil];
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

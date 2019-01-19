//
//  LUMutableArrayTest.m
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

#import <UIKit/UIKit.h>
#import <XCTest/XCTest.h>

#import "Lunar.h"

@interface LUMutableArrayTest : XCTestCase

@end

@implementation LUMutableArrayTest

- (void)testAddElements
{
    LUMutableArray *array = [LUMutableArray listWithCapacity:10 trimCount:1];
    [array addObject:@"1"];
    [array addObject:@"2"];
    [array addObject:@"3"];

    [self assertMutableArray:array, @"1", @"2", @"3", nil];
}

- (void)testTrimElements
{
    LUMutableArray *array = [LUMutableArray listWithCapacity:3 trimCount:2];
    XCTAssertEqual(0, array.count);
    XCTAssertEqual(0, array.totalCount);
    XCTAssertEqual(0, array.trimmedCount);
    XCTAssert(!array.isTrimmed);
    
    [array addObject:@"1"];
    XCTAssertEqual(1, array.count);
    XCTAssertEqual(1, array.totalCount);
    XCTAssertEqual(0, array.trimmedCount);
    XCTAssert(!array.isTrimmed);
    
    [array addObject:@"2"];
    XCTAssertEqual(2, array.count);
    XCTAssertEqual(2, array.totalCount);
    XCTAssertEqual(0, array.trimmedCount);
    XCTAssert(!array.isTrimmed);
    
    [array addObject:@"3"];
    XCTAssertEqual(3, array.count);
    XCTAssertEqual(3, array.totalCount);
    XCTAssertEqual(0, array.trimmedCount);
    XCTAssert(!array.isTrimmed);
    
    [array addObject:@"4"];
    XCTAssertEqual(2, array.count);
    XCTAssertEqual(4, array.totalCount);
    XCTAssertEqual(2, array.trimmedCount);
    XCTAssert(array.isTrimmed);
    
    [self assertMutableArray:array, @"3", @"4", nil];
    
    [array addObject:@"5"];
    XCTAssertEqual(3, array.count);
    XCTAssertEqual(5, array.totalCount);
    XCTAssertEqual(2, array.trimmedCount);
    XCTAssert(array.isTrimmed);
    
    [self assertMutableArray:array, @"3", @"4", @"5", nil];
    
    [array addObject:@"6"];
    XCTAssertEqual(2, array.count);
    XCTAssertEqual(6, array.totalCount);
    XCTAssertEqual(4, array.trimmedCount);
    XCTAssert(array.isTrimmed);
    
    [self assertMutableArray:array, @"5", @"6", nil];
    
    [array addObject:@"7"];
    XCTAssertEqual(3, array.count);
    XCTAssertEqual(7, array.totalCount);
    XCTAssertEqual(4, array.trimmedCount);
    XCTAssert(array.isTrimmed);
    
    [self assertMutableArray:array, @"5", @"6", @"7", nil];
}

- (void)testTrimElementsAndClear
{
    LUMutableArray *array = [LUMutableArray listWithCapacity:3 trimCount:2];
    [array addObject:@"1"];
    [array addObject:@"2"];
    [array addObject:@"3"];
    [array addObject:@"4"];
    
    [array removeAllObjects];
    XCTAssertEqual(0, array.count);
    XCTAssertEqual(0, array.totalCount);
    XCTAssertEqual(0, array.trimmedCount);
    XCTAssert(!array.isTrimmed);

    [array addObject:@"5"];
    XCTAssertEqual(1, array.count);
    XCTAssertEqual(1, array.totalCount);
    XCTAssertEqual(0, array.trimmedCount);
    XCTAssert(!array.isTrimmed);
    
    [self assertMutableArray:array, @"5", nil];
    
    [array addObject:@"6"];
    XCTAssertEqual(2, array.count);
    XCTAssertEqual(2, array.totalCount);
    XCTAssertEqual(0, array.trimmedCount);
    XCTAssert(!array.isTrimmed);
    
    [self assertMutableArray:array, @"5", @"6", nil];
    
    [array addObject:@"7"];
    XCTAssertEqual(3, array.count);
    XCTAssertEqual(3, array.totalCount);
    XCTAssertEqual(0, array.trimmedCount);
    XCTAssert(!array.isTrimmed);
    
    [self assertMutableArray:array, @"5", @"6", @"7", nil];
    
    [array addObject:@"8"];
    XCTAssertEqual(2, array.count);
    XCTAssertEqual(4, array.totalCount);
    XCTAssertEqual(2, array.trimmedCount);
    XCTAssert(array.isTrimmed);
    
    [self assertMutableArray:array, @"7", @"8", nil];
    
    [array addObject:@"9"];
    XCTAssertEqual(3, array.count);
    XCTAssertEqual(5, array.totalCount);
    XCTAssertEqual(2, array.trimmedCount);
    XCTAssert(array.isTrimmed);
    
    [self assertMutableArray:array, @"7", @"8", @"9", nil];
    
    [array addObject:@"10"];
    XCTAssertEqual(2, array.count);
    XCTAssertEqual(6, array.totalCount);
    XCTAssertEqual(4, array.trimmedCount);
    XCTAssert(array.isTrimmed);
    
    [self assertMutableArray:array, @"9", @"10", nil];
    
    [array addObject:@"11"];
    XCTAssertEqual(3, array.count);
    XCTAssertEqual(7, array.totalCount);
    XCTAssertEqual(4, array.trimmedCount);
    XCTAssert(array.isTrimmed);
    
    [self assertMutableArray:array, @"9", @"10", @"11", nil];
}

- (void)assertMutableArray:(LUMutableArray *)actual, ... NS_REQUIRES_NIL_TERMINATION
{
    va_list ap;
    va_start(ap, actual);
    
    NSMutableArray *expected = [NSMutableArray array];
    id obj;
    while ((obj = va_arg(ap, id)))
    {
        [expected addObject:obj];
    }
    
    va_end(ap);
    
    XCTAssertEqual(expected.count, actual.count);
    for (int i = 0; i < expected.count; ++i)
    {
        XCTAssertEqual(expected[i], actual[i]);
    }
}

@end

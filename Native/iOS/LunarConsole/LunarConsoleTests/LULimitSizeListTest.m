//
//  LULimitedListTest.m
//  LunarConsole
//
//  Created by Alex Lementuev on 8/7/15.
//  Copyright (c) 2015 Space Madness. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <XCTest/XCTest.h>

#import "Lunar.h"

@interface LULimitSizeListTest : XCTestCase

@end

@implementation LULimitSizeListTest

- (void)testAddElements
{
    LULimitSizeList *list = [LULimitSizeList listWithCapacity:10];
    [list addObject:@"1"];
    [list addObject:@"2"];
    [list addObject:@"3"];

    [self listAssertObjects:list, @"1", @"2", @"3", nil];
}

- (void)testAddElementsOverCapacity
{
    LULimitSizeList *list = [LULimitSizeList listWithCapacity:3];
    XCTAssertEqual(0, list.totalCount);
    XCTAssert(!list.isOverfloating);
    
    [list addObject:@"1"];
    XCTAssertEqual(1, list.totalCount);
    XCTAssert(!list.isOverfloating);
    
    [list addObject:@"2"];
    XCTAssertEqual(2, list.totalCount);
    XCTAssert(!list.isOverfloating);
    
    [list addObject:@"3"];
    XCTAssertEqual(3, list.totalCount);
    XCTAssert(!list.isOverfloating);
    
    [list addObject:@"4"];
    XCTAssertEqual(4, list.totalCount);
    XCTAssert(list.isOverfloating);
    
    [self listAssertObjects:list, @"2", @"3", @"4", nil];
}

- (void)testAddElementsWayOverCapacity
{
    LULimitSizeList *list = [LULimitSizeList listWithCapacity:3];
    XCTAssertEqual(0, list.totalCount);
    XCTAssert(!list.isOverfloating);
    
    [list addObject:@"1"];
    XCTAssertEqual(1, list.totalCount);
    XCTAssert(!list.isOverfloating);
    
    [list addObject:@"2"];
    XCTAssertEqual(2, list.totalCount);
    XCTAssert(!list.isOverfloating);
    
    [list addObject:@"3"];
    XCTAssertEqual(3, list.totalCount);
    XCTAssert(!list.isOverfloating);
    
    [list addObject:@"4"];
    XCTAssertEqual(4, list.totalCount);
    XCTAssert(list.isOverfloating);
    
    [list addObject:@"5"];
    XCTAssertEqual(5, list.totalCount);
    XCTAssert(list.isOverfloating);
    
    [list addObject:@"6"];
    XCTAssertEqual(6, list.totalCount);
    XCTAssert(list.isOverfloating);
    
    [list addObject:@"7"];
    XCTAssertEqual(7, list.totalCount);
    XCTAssert(list.isOverfloating);
    
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

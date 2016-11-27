//
//  LUSortedListTest.m
//  LunarConsole
//
//  Created by Alex Lementuev on 3/6/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "Lunar.h"

#import <XCTest/XCTest.h>

@interface LUSortedListTest : XCTestCase
{
    LUSortedList * _list;
}

@end

@implementation LUSortedListTest

#pragma mark -
#pragma mark Setup

- (void)setUp
{
    [super setUp];
    _list = [LUSortedList new];
}

- (void)tearDown
{
    [_list release];
    [super tearDown];
}

#pragma mark -
#pragma mark Testing

- (void)testSortedAdd
{
    [_list addObject:@"3"];
    [_list addObject:@"2"];
    [_list addObject:@"1"];
    [_list addObject:@"4"];
    
    [self assertList:@"1", @"2", @"3", @"4", nil];
}

- (void)testDuplicateItems
{
    [_list addObject:@"3"];
    [_list addObject:@"3"];
    [_list addObject:@"2"];
    [_list addObject:@"1"];
    [_list addObject:@"1"];
    [_list addObject:@"4"];
    
    [self assertList:@"1", @"2", @"3", @"4", nil];
}

- (void)testRemoveItems
{
    [_list addObject:@"3"];
    [_list addObject:@"2"];
    [_list addObject:@"1"];
    [_list addObject:@"4"];
    
    [_list removeObject:@"2"];
    
    [self assertList:@"1", @"3", @"4", nil];
}

- (void)testIndexOfItem
{
    [_list addObject:@"3"];
    [_list addObject:@"3"];
    [_list addObject:@"2"];
    [_list addObject:@"1"];
    [_list addObject:@"1"];
    [_list addObject:@"4"];
    
    XCTAssertEqual(0, [_list indexOfObject:@"1"]);
    XCTAssertEqual(1, [_list indexOfObject:@"2"]);
    XCTAssertEqual(2, [_list indexOfObject:@"3"]);
    XCTAssertEqual(3, [_list indexOfObject:@"4"]);
}

#pragma mark -
#pragma mark Helpers

- (void)assertList:(id)first, ... NS_REQUIRES_NIL_TERMINATION
{
    NSMutableArray *expected = [NSMutableArray array];
    
    va_list ap;
    va_start(ap, first);
    for (id object = first; object; object = va_arg(ap, id))
    {
        [expected addObject:object];
    }
    va_end(ap);
    
    XCTAssertEqual(expected.count, _list.count);
    for (NSUInteger i = 0; i < expected.count; ++i)
    {
        XCTAssertEqualObjects(expected[i], _list[i]);
    }
}

@end

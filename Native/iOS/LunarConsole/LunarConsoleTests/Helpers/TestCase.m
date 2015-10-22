//
//  TestCase.m
//  LunarConsole
//
//  Created by Alex Lementuev on 10/22/15.
//  Copyright (c) 2015 Space Madness. All rights reserved.
//

#import "TestCase.h"

#import "Lunar.h"

@interface TestCase ()
{
    NSMutableArray * _result;
}

@end

@implementation TestCase

- (void)setUp
{
    [super setUp];
    
    LU_RELEASE(_result);
    _result = [NSMutableArray new];
}

#pragma mark -
#pragma mark Results

- (void)addResult:(id)obj
{
    [_result addObject:obj];
}

- (void)assertResult:(id)first, ...
{
    va_list ap;
    va_start(ap, first);
    NSMutableArray *expected = [NSMutableArray array];
    for (id obj = va_arg(ap, id); obj != nil; obj = va_arg(ap, id))
    {
        [expected addObject:obj];
    }
    va_end(ap);
    
    XCTAssertEqual(expected.count, _result.count);
    for (int i = 0; i < expected.count; ++i)
    {
        XCTAssertEqual([expected objectAtIndex:i], [_result objectAtIndex:i]);
    }
    
    [_result removeAllObjects];
}

@end

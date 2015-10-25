//
//  TestCase.m
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

#import "TestCase.h"

#import "Lunar.h"

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
    for (id obj = first; obj != nil; obj = va_arg(ap, id))
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

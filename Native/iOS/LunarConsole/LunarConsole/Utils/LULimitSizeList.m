//
//  LULimitSizeList.m
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

#import "LULimitSizeList.h"

#import "Lunar.h"

@interface LULimitSizeList ()
{
    NSMutableArray * _objects;
}

@end

@implementation LULimitSizeList

+ (instancetype)listWithCapacity:(NSUInteger)capacity
{
    return LU_AUTORELEASE([[self alloc] initWithCapacity:capacity]);
}

- (instancetype)initWithCapacity:(NSUInteger)capacity
{
    self = [super init];
    if (self)
    {
        _capacity = capacity;
        _objects = [NSMutableArray new];
    }
    return self;
}

- (void)dealloc
{
    LU_RELEASE(_objects);
    LU_SUPER_DEALLOC
}

#pragma mark -
#pragma mark Objects

- (void)addObject:(id)object
{
    LUAssert(object);
    if (object)
    {
        // don't let it to overflow
        if (_objects.count == _capacity)
        {
            [_objects removeObjectAtIndex:0];
        }
        [_objects addObject:object];
        
        // keep track of the total amount of objects added
        ++_totalCount;
    }
}

- (id)objectAtIndex:(NSUInteger)index
{
    return [_objects objectAtIndex:index];
}

- (id)objectAtIndexedSubscript:(NSUInteger)index
{
    return [_objects objectAtIndexedSubscript:index];
}

- (void)removeAllObjects
{
    /* NSMutableArray array never shinks after growing */
    LU_RELEASE(_objects);
    _objects = [NSMutableArray new];
    _totalCount = 0;
}

#pragma mark -
#pragma mark NSFastEnumeration

- (NSUInteger)countByEnumeratingWithState:(NSFastEnumerationState *)state objects:(id __unsafe_unretained [])buffer count:(NSUInteger)len
{
    return [_objects countByEnumeratingWithState:state objects:buffer count:len];
}

#pragma mark -
#pragma mark Properties

- (NSUInteger)count
{
    return _objects.count;
}

- (NSUInteger)overflowCount
{
    return _totalCount > _objects.count ? _totalCount - _objects.count : 0;
}

- (BOOL)isOverfloating
{
    return _totalCount > _objects.count;
}

@end

//
//  LUSortedList.m
//  LunarConsole
//
//  Created by Alex Lementuev on 3/6/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "Lunar.h"

#import "LUSortedList.h"

@interface LUSortedList ()
{
    NSMutableArray * _array;
}

@end

@implementation LUSortedList

- (instancetype)init
{
    self = [super init];
    if (self)
    {
        _array = [NSMutableArray new];
    }
    return self;
}

#pragma mark -
#pragma mark Objects

- (nonnull id)objectAtIndex:(NSUInteger)index
{
    return [_array objectAtIndex:index];
}

- (nonnull id)objectAtIndexedSubscript:(NSUInteger)index
{
    return [_array objectAtIndexedSubscript:index];
}

- (NSUInteger)addObject:(nonnull id)object
{
    LUAssert(object != nil);
    LUAssertMsgv([object respondsToSelector:@selector(compare:)],
                 @"Can't add non-comparable object to a sorted list: %@", [object class]);
    
    if (object != nil && [object respondsToSelector:@selector(compare:)])
    {
        // TODO: use binary search to insert in a sorted order
        for (NSUInteger i = 0; i < _array.count; ++i)
        {
            NSComparisonResult comparisonResult = [object compare:_array[i]];
            if (comparisonResult == NSOrderedAscending)
            {
                [_array insertObject:object atIndex:i];
                return i;
            }
            else if (comparisonResult == NSOrderedSame)
            {
                _array[i] = object;
                return i;
            }
        }
        
        [_array addObject:object];
        return _array.count - 1;
    }
    
    return NSNotFound;
}

- (void)removeObject:(nonnull id)object
{
    LUAssert(object != nil);
    if (object != nil)
    {
        [_array removeObject:object];
    }
}

- (void)removeObjectAtIndex:(NSUInteger)index
{
    [_array removeObjectAtIndex:index];
}

- (void)removeAllObjects
{
    [_array removeAllObjects];
}

- (NSInteger)indexOfObject:(nonnull id)object
{
    LUAssert(object != nil);
    return object != nil ? [_array indexOfObject:object] : -1;
}

#pragma mark -
#pragma mark NSFastEnumeration

- (NSUInteger)countByEnumeratingWithState:(NSFastEnumerationState *)state objects:(id __unsafe_unretained [])buffer count:(NSUInteger)len
{
    return [_array countByEnumeratingWithState:state objects:buffer count:len];
}

#pragma mark -
#pragma mark Properties

- (NSUInteger)count
{
    return _array.count;
}

- (NSArray *)innerArray
{
    return _array;
}

@end

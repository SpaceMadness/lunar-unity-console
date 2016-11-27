//
//  LUSortedList.h
//  LunarConsole
//
//  Created by Alex Lementuev on 3/6/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface LUSortedList : NSObject<NSFastEnumeration>

@property (nonatomic, readonly) NSUInteger count;
@property (nonatomic, readonly, nonnull) NSArray *innerArray;

- (nonnull id)objectAtIndex:(NSUInteger)index;
- (nonnull id)objectAtIndexedSubscript:(NSUInteger)index;

- (NSUInteger)addObject:(nonnull id)object;
- (void)removeObject:(nonnull id)object;
- (void)removeObjectAtIndex:(NSUInteger)index;
- (void)removeAllObjects;

- (NSInteger)indexOfObject:(nonnull id)object;

@end

//
//  LULimitedList.h
//  LunarConsole
//
//  Created by Alex Lementuev on 8/7/15.
//  Copyright (c) 2015 Space Madness. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface LULimitSizeList : NSObject<NSFastEnumeration>

@property (nonatomic, readonly) NSUInteger count;
@property (nonatomic, readonly) NSUInteger totalCount;
@property (nonatomic, readonly) NSUInteger capacity;
@property (nonatomic, readonly) NSUInteger overflowCount;
@property (nonatomic, readonly) BOOL isOverfloating;

+ (instancetype)listWithCapacity:(NSUInteger)capacity;
- (instancetype)initWithCapacity:(NSUInteger)capacity;

- (void)addObject:(id)object;
- (id)objectAtIndex:(NSUInteger)index;
- (id)objectAtIndexedSubscript:(NSUInteger)index;

- (void)removeAllObjects;

@end

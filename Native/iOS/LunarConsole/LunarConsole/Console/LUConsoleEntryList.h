//
//  LunarConsoleEntryList.h
//  LunarConsole
//
//  Created by Alex Lementuev on 8/5/15.
//  Copyright © 2015 Space Madness. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "LUConsoleEntry.h"

@interface LUConsoleEntryList : NSObject

@property (nonatomic, readonly) NSUInteger count;
@property (nonatomic, readonly) NSUInteger totalCount;

@property (nonatomic, readonly) NSString *filterText;
@property (nonatomic, readonly) BOOL isFiltering;
@property (nonatomic, readonly) NSUInteger overflowAmount;
@property (nonatomic, readonly) BOOL isOverfloating;

@property (nonatomic, readonly) NSUInteger logCount;
@property (nonatomic, readonly) NSUInteger warningCount;
@property (nonatomic, readonly) NSUInteger errorCount;

+ (instancetype)listWithCapacity:(NSUInteger)capacity;
- (instancetype)initWithCapacity:(NSUInteger)capacity;

- (BOOL)addEntry:(LUConsoleEntry *)entry;
- (LUConsoleEntry *)entryAtIndex:(NSUInteger)index;
- (void)clear;

- (BOOL)setFilterByText:(NSString *)filterText;
- (BOOL)setFilterByLogType:(LUConsoleLogType)logType disabled:(BOOL)disabled;
- (BOOL)setFilterByLogTypeMask:(LUConsoleLogTypeMask)logTypeMask disabled:(BOOL)disabled;

- (NSString *)getText;

@end

//
//  LunarConsole.h
//  LunarConsole
//
//  Created by Alex Lementuev on 8/3/15.
//  Copyright (c) 2015 Space Madness. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "LUConsoleEntry.h"

@class LUConsole;
@class LUConsoleEntryList;

@protocol LunarConsoleDelegate <NSObject>

@required
- (void)lunarConsole:(LUConsole *)console didAddEntry:(LUConsoleEntry *)entry filtered:(BOOL)filtered;
- (void)lunarConsoleDidClearEntries:(LUConsole *)console;

@end

@interface LUConsole : NSObject

@property (nonatomic, assign) id<LunarConsoleDelegate> delegate;

@property (nonatomic, readonly) LUConsoleEntryList * entries;
@property (nonatomic, readonly) NSUInteger entriesCount;
@property (nonatomic, readonly) NSUInteger overflowAmount;
@property (nonatomic, readonly) BOOL isOverfloating;

- (instancetype)initWithCapacity:(NSUInteger)capacity;

- (LUConsoleEntry *)entryAtIndex:(NSUInteger)index;

- (void)logMessage:(NSString *)message stackTrace:(NSString *)stackTrace type:(LUConsoleLogType)type;
- (void)clear;

- (NSString *)getText;

@end

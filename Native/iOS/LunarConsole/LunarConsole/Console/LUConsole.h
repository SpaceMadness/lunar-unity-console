//
//  LUConsole.h
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

#import <UIKit/UIKit.h>

#import "LUConsoleEntry.h"

@class LUConsole;
@class LUConsoleEntryList;

@protocol LunarConsoleDelegate <NSObject>

@required
- (void)lunarConsole:(LUConsole *)console didAddEntry:(LUConsoleEntry *)entry filtered:(BOOL)filtered;
- (void)lunarConsole:(LUConsole *)console didRemoveRange:(NSRange )range;
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

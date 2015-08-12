//
//  LUConsolePlugin.h
//  LunarConsole
//
//  Created by Alex Lementuev on 8/5/15.
//  Copyright (c) 2015 Space Madness. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "LUObject.h"
#import "LUConsoleEntry.h"

@interface LUConsolePlugin : LUObject

- (instancetype)initWithCapacity:(NSUInteger)capacity;

- (void)show;
- (void)hide;

- (void)logMessage:(NSString *)message stackTrace:(NSString *)stackTrace type:(LUConsoleLogType)type;
- (void)clear;

- (void)enableGestureRecognition;
- (void)disableGestureRecognition;

@end

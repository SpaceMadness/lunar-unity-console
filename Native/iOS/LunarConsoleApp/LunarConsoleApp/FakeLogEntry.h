//
//  FakeLogEntry.h
//  LunarConsoleApp
//
//  Created by Alex Lementuev on 11/23/15.
//  Copyright © 2015 Space Madness. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "Lunar.h"

@interface FakeLogEntry : NSObject

@property (nonatomic, readonly) LUConsoleLogType type;
@property (nonatomic, readonly) NSString * message;
@property (nonatomic, readonly) NSString * stacktrace;

- (instancetype)initWithType:(LUConsoleLogType)type message:(NSString *)message stackTrace:(NSString *)stackTrace;

@end

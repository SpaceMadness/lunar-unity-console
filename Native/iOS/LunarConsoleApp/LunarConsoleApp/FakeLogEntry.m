//
//  FakeLogEntry.m
//  LunarConsoleApp
//
//  Created by Alex Lementuev on 11/23/15.
//  Copyright © 2015 Space Madness. All rights reserved.
//

#import "FakeLogEntry.h"

@implementation FakeLogEntry

- (instancetype)initWithType:(LUConsoleLogType)type message:(NSString *)message stackTrace:(NSString *)stackTrace
{
    self = [super init];
    if (self)
    {
        _type = type;
        _message = [message copy];
        _stacktrace = [stackTrace copy];
    }
    return self;
}

- (void)dealloc
{
    LU_RELEASE(_message);
    LU_RELEASE(_stacktrace);
    LU_SUPER_DEALLOC
}

@end

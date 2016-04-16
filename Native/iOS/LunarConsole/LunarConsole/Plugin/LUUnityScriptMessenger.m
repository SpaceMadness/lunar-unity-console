//
//  LUUnityScriptMessenger.m
//  LunarConsole
//
//  Created by Alex Lementuev on 4/15/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "LUUnityScriptMessenger.h"

#import "Lunar.h"

extern void UnitySendMessage(const char *, const char *, const char *);

@interface LUUnityScriptMessenger ()
{
    NSString * _targetName;
}

@end

@implementation LUUnityScriptMessenger

- (instancetype)initWithTargetName:(NSString *)targetName
{
    self = [super init];
    if (self)
    {
        _targetName = LU_RETAIN(targetName);
    }
    return self;
}

- (void)dealloc
{
    LU_RELEASE(_targetName);
    LU_SUPER_DEALLOC
}

#pragma mark -
#pragma mark Messages

- (void)sendMessageWithName:(NSString *)name
{
    [self sendMessageWithName:name param:@""];
}

- (void)sendMessageWithName:(NSString *)name param:(NSString *)param
{
    UnitySendMessage([_targetName UTF8String], [name UTF8String], [param UTF8String]);
}

@end

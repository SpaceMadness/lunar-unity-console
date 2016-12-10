//
//  LUNotificationCenter.m
//  LunarConsole
//
//  Created by Alex Lementuev on 12/10/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "LUNotificationCenter.h"

#import "Lunar.h"

static id<LUNotificationCenterImpl> _impl;

@interface LUNotificationCenterDefault : NSObject <LUNotificationCenterImpl>

@end

@implementation LUNotificationCenter

+ (void)initialize
{
    if ([self class] == [LUNotificationCenter class])
    {
        [self setImpl:[LUNotificationCenterDefault new]];
    }
}

+ (void)setImpl:(id<LUNotificationCenterImpl>)impl
{
    _impl = impl ? impl : [LUNotificationCenterDefault new];
}

+ (void)addObserver:(id)observer selector:(SEL)selector name:(NSNotificationName)name object:(id)object
{
    [_impl addObserver:observer selector:selector name:name object:object];
}

+ (void)removeObserver:(id)observer
{
    [_impl removeObserver:observer];
}

+ (void)removeObserver:(id)observer name:(NSNotificationName)name object:(id)object
{
    [_impl removeObserver:observer name:name object:object];
}

+ (void)postNotificationName:(NSNotificationName)name object:(id)object
{
    [self postNotificationName:name object:object userInfo:nil];
}

+ (void)postNotificationName:(NSNotificationName)name object:(id)object userInfo:(NSDictionary *)userInfo
{
    [_impl postNotificationName:name object:object userInfo:userInfo];
}

@end

@implementation LUNotificationCenterDefault

- (void)addObserver:(id)observer selector:(SEL)selector name:(NSNotificationName)name object:(id)object
{
    [[NSNotificationCenter defaultCenter] addObserver:observer selector:selector name:name object:object];
}

- (void)removeObserver:(id)observer
{
    [[NSNotificationCenter defaultCenter] removeObserver:observer];
}

- (void)removeObserver:(id)observer name:(NSNotificationName)name object:(id)object
{
    [[NSNotificationCenter defaultCenter] removeObserver:observer name:name object:object];
}

- (void)postNotificationName:(NSNotificationName)name object:(id)object userInfo:(NSDictionary *)userInfo
{
    [[NSNotificationCenter defaultCenter] postNotificationName:name object:object userInfo:userInfo];
}

@end

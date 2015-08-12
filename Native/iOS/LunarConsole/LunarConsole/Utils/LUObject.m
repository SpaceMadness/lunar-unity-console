//
//  LUObject.m
//  LunarConsole
//
//  Created by Alex Lementuev on 8/5/15.
//  Copyright (c) 2015 Space Madness. All rights reserved.
//

#import "LUObject.h"

#import "Lunar.h"

@implementation LUObject

- (void)dealloc
{
    [self unregisterNotifications];
    LU_SUPER_DEALLOC
}

#pragma mark -
#pragma mark Notifications

- (void)registerNotificationName:(NSString *)name selector:(SEL)selector
{
    [[NSNotificationCenter defaultCenter] addObserver:self selector:selector name:name object:nil];
}

- (void)unregisterNotifications
{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

@end

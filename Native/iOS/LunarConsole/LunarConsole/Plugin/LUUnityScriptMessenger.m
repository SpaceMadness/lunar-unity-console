//
//  LUUnityScriptMessenger.m
//  LunarConsole
//
//  Created by Alex Lementuev on 2/23/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "LUUnityScriptMessenger.h"

#include "Lunar.h"

extern void UnitySendMessage(const char *objectName, const char *methodName, const char *message);

@interface LUUnityScriptMessenger ()
{
    NSString * _targetName;
    NSString * _methodName;
}

@end

@implementation LUUnityScriptMessenger

- (instancetype)initWithTargetName:(NSString *)targetName methodName:(NSString *)methodName
{
    self = [super init];
    if (self)
    {
        if (targetName.length == 0)
        {
            NSLog(@"Can't create script messenger: target name is nil or empty");
            LU_RELEASE(self);
            self = nil;
            return nil;
        }
        
        if (methodName.length == 0)
        {
            NSLog(@"Can't create script messenger: method name is nil or empty");
            LU_RELEASE(self);
            self = nil;
            return nil;
        }
        
        _targetName = [targetName copy];
        _methodName = [methodName copy];
    }
    return self;
}

- (void)dealloc
{
    LU_RELEASE(_targetName);
    LU_RELEASE(_methodName);
    LU_SUPER_DEALLOC
}

- (void)sendMessageName:(NSString *)name
{
    [self sendMessageName:name params:nil];
}

- (void)sendMessageName:(NSString *)name params:(NSDictionary *)params
{
    NSMutableDictionary *dict = [NSMutableDictionary dictionaryWithObjectsAndKeys:name, @"name", nil];
    if (params.count > 0)
    {
        [dict addEntriesFromDictionary:params];
    }
    
    NSString *paramString = LUSerializeDictionaryToString(dict);
    UnitySendMessage(_targetName.UTF8String, _methodName.UTF8String, paramString.UTF8String);
}

@end

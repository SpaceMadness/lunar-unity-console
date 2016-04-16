//
//  LUUnityScriptMessenger.m
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2016 Alex Lementuev, SpaceMadness.
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

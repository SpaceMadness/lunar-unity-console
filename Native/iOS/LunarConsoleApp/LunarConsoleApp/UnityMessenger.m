//
//  UnityMessenger.m
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2015-2020 Alex Lementuev, SpaceMadness.
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


#import "UnityMessenger.h"
#import "LULog.h"

static UnityMessenger * _instance;

@interface UnityMessenger ()
- (void)messageReceived:(NSString *)message;
@end

@implementation UnityMessenger

- (instancetype)init
{
    self = [super init];
    if (self)
    {
        _instance = self;
    }
    return self;
}

- (void)messageReceived:(NSString *)message
{
	[_delegate onUnityMessage:message];
}

@end

void UnitySendMessage(const char *objectName, const char *methodName, const char *message)
{
    LULog(@"Send native message: %s.%s(%s)", objectName, methodName, message);
	[_instance messageReceived:[NSString stringWithUTF8String:message]];
}

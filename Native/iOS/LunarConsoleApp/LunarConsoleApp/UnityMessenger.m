//
//  UnityMessanger.m
//  LunarConsoleApp
//
//  Created by Alex Lementuev on 1/28/17.
//  Copyright © 2017 Space Madness. All rights reserved.
//

#import "UnityMessenger.h"

static UnityMessenger * _instance;

@interface UnityMessenger ()
{
    __weak id<UnityMessengerDelegate> _delegate;
}

- (void)messageReceived:(NSString *)message;

@end

@implementation UnityMessenger

- (instancetype)initWithDelegate:(id<UnityMessengerDelegate>)delegate
{
    self = [super init];
    if (self)
    {
        _instance = self;
        _delegate = delegate;
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
    NSLog(@"Send native message: %s.%s(%s)", objectName, methodName, message);
    [_instance messageReceived:[NSString stringWithUTF8String:message]];
}

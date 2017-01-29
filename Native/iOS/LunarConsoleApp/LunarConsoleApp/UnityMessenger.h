//
//  UnityMessanger.h
//  LunarConsoleApp
//
//  Created by Alex Lementuev on 1/28/17.
//  Copyright © 2017 Space Madness. All rights reserved.
//

#import <Foundation/Foundation.h>

@class UnityMessenger;

@protocol UnityMessengerDelegate <NSObject>

- (void)onUnityMessage:(NSString *)message;

@end

@interface UnityMessenger : NSObject

- (instancetype)initWithDelegate:(id<UnityMessengerDelegate>)delegate;

@end

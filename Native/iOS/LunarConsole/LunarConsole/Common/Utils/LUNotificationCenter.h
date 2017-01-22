//
//  LUNotificationCenter.h
//  LunarConsole
//
//  Created by Alex Lementuev on 12/10/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol LUNotificationCenterImpl <NSObject>

- (void)addObserver:(id)observer selector:(SEL)selector name:(NSNotificationName)name object:(id)object;
- (void)removeObserver:(id)observer;
- (void)removeObserver:(id)observer name:(NSNotificationName)name object:(id)object;
- (void)postNotificationName:(NSNotificationName)name object:(id)object userInfo:(NSDictionary *)userInfo;

@end

@interface LUNotificationCenter : NSObject

+ (void)setImpl:(id<LUNotificationCenterImpl>)impl;

+ (void)addObserver:(id)observer selector:(SEL)selector name:(NSNotificationName)name object:(id)object;
+ (void)removeObserver:(id)observer;
+ (void)removeObserver:(id)observer name:(NSNotificationName)name object:(id)object;

+ (void)postNotificationName:(NSNotificationName)name object:(id)object;
+ (void)postNotificationName:(NSNotificationName)name object:(id)object userInfo:(NSDictionary *)userInfo;

@end

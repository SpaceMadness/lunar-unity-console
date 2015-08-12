//
//  LUObject.h
//  LunarConsole
//
//  Created by Alex Lementuev on 8/5/15.
//  Copyright (c) 2015 Space Madness. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface LUObject : NSObject

- (void)registerNotificationName:(NSString *)name selector:(SEL)selector;
- (void)unregisterNotifications;

@end

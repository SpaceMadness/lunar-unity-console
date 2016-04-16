//
//  LUUnityScriptMessenger.h
//  LunarConsole
//
//  Created by Alex Lementuev on 4/15/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface LUUnityScriptMessenger : NSObject

- (instancetype)initWithTargetName:(NSString *)targetName;

- (void)sendMessageWithName:(NSString *)name;
- (void)sendMessageWithName:(NSString *)name param:(NSString *)param;

@end

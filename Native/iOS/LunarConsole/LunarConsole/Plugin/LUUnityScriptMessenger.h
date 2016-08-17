//
//  LUUnityScriptMessenger.h
//  LunarConsole
//
//  Created by Alex Lementuev on 2/23/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface LUUnityScriptMessenger : NSObject

- (instancetype)initWithTargetName:(NSString *)targetName methodName:(NSString *)methodName;

- (void)sendMessageName:(NSString *)name;
- (void)sendMessageName:(NSString *)name params:(NSDictionary *)params;

@end

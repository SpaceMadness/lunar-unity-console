//
//  LUStackTrace.h
//  LunarConsole
//
//  Created by Alex Lementuev on 11/23/15.
//  Copyright © 2015 Space Madness. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface LUStacktrace : NSObject

+ (NSString *)optimizeStacktrace:(NSString *)stacktrace;

@end

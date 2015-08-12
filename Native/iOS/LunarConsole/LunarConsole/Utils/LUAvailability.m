//
//  LUAvailability.m
//  LunarConsole
//
//  Created by Alex Lementuev on 8/8/15.
//  Copyright (c) 2015 Space Madness. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "LUAvailability.h"

static LUSystemVersion _systemVersion;

BOOL lunar_ios_version_available(LUSystemVersion version)
{
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        
        NSArray *tokens = [[UIDevice currentDevice].systemVersion componentsSeparatedByString:@"."];
        int multiplier = 10000;
        
        _systemVersion = 0;
        for (int i = 0; i < tokens.count; ++i)
        {
            int val = [[tokens objectAtIndex:i] intValue];
            _systemVersion += val * multiplier;
            multiplier /= 100;
        }
        
        NSLog(@"System version: %ld", (unsigned long)_systemVersion);
    });
    
    return _systemVersion >= version;
}
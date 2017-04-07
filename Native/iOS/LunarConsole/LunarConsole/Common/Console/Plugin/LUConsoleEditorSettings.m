//
//  LUConsoleEditorSettings.m
//  LunarConsole
//
//  Created by Alex Lementuev on 4/6/17.
//  Copyright © 2017 Space Madness. All rights reserved.
//

#import "LUConsoleEditorSettings.h"

@implementation LUConsoleEditorSettings

- (instancetype)init
{
    self = [super init];
    if (self)
    {
        _exceptionWarningEnabled = YES;
        _transparentLogOverlayEnabled = NO;
        _actionSortingEnabled = YES;
        _variableSortingEnabled = YES;
    }
    return self;
}

- (instancetype)initWithDictionary:(NSDictionary *)settings
{
    self = [super init];
    if (self)
    {
        _exceptionWarningEnabled = [[settings objectForKey:@"exceptionWarning"] boolValue];
        _transparentLogOverlayEnabled = [[settings objectForKey:@"transparentLogOverlay"] boolValue];
        _actionSortingEnabled = [[settings objectForKey:@"sortActions"] boolValue];
        _variableSortingEnabled = [[settings objectForKey:@"sortVariables"] boolValue];
    }
    return self;
}

@end

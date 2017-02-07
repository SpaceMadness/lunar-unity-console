//
//  LUConsolePluginImp.m
//  LunarConsole
//
//  Created by Alex Lementuev on 2/6/17.
//  Copyright © 2017 Space Madness. All rights reserved.
//

#import "LUConsolePluginImp.h"

BOOL LUConsoleIsFreeVersion = YES;
BOOL LUConsoleIsFullVersion = NO;

@implementation LUConsolePluginImp

- (instancetype)initWithPlugin:(LUConsolePlugin *)plugin
{
    self = [super init];
    if (self)
    {
    }
    return self;
}

- (void)showOverlay
{
}

- (void)hideOverlay
{
}

@end

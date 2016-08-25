//
//  LUSwitch.m
//  LunarConsole
//
//  Created by Alex Lementuev on 8/24/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "Lunar.h"

#import "LUSwitch.h"

@implementation LUSwitch

- (void)dealloc
{
    LU_RELEASE(_userData);
    LU_SUPER_DEALLOC
}

@end

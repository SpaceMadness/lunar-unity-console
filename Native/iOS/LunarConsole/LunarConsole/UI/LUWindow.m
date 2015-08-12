//
//  LUWindow.m
//  LunarConsole
//
//  Created by Alex Lementuev on 8/8/15.
//  Copyright (c) 2015 Space Madness. All rights reserved.
//

#import "LUWindow.h"

#import "Lunar.h"

@implementation LUWindow

+ (instancetype)window
{
    return LU_AUTORELEASE([[self alloc] init]);
}

- (instancetype)init
{
    self = [super initWithFrame:[UIScreen mainScreen].bounds];
    if (self)
    {   
    }
    return self;
}

@end

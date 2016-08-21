//
//  LUConsoleOverlayController.m
//  LunarConsole
//
//  Created by Alex Lementuev on 8/20/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "Lunar.h"

#import "LUConsoleOverlayController.h"

@interface LUConsoleOverlayController ()

@end

@implementation LUConsoleOverlayController

+ (instancetype)controllerWithConsole:(LUConsole *)console
{
    return LU_AUTORELEASE([[[self class] alloc] initWithConsole:console]);
}

- (instancetype)initWithConsole:(LUConsole *)console
{
    self = [super initWithNibName:NSStringFromClass([self class]) bundle:nil];
    if (self)
    {
    }
    return self;
}

@end

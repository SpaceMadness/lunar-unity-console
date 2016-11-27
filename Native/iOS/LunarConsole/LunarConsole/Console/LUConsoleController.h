//
//  LUConsoleController.h
//  LunarConsole
//
//  Created by Alex Lementuev on 11/26/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "LUViewController.h"

@class LUConsolePlugin;

@interface LUConsoleController : LUViewController

+ (instancetype)controllerWithPlugin:(LUConsolePlugin *)plugin;
- (instancetype)initWithPlugin:(LUConsolePlugin *)plugin;

@end

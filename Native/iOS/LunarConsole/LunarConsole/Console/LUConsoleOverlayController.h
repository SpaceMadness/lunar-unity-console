//
//  LUConsoleOverlayController.h
//  LunarConsole
//
//  Created by Alex Lementuev on 8/20/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "LUViewController.h"

@interface LUConsoleOverlayController : LUViewController

+ (instancetype)controllerWithConsole:(LUConsole *)console;
- (instancetype)initWithConsole:(LUConsole *)console;

@end

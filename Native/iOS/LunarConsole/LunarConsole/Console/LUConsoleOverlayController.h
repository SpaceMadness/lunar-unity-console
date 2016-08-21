//
//  LUConsoleOverlayController.h
//  LunarConsole
//
//  Created by Alex Lementuev on 8/20/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "LUViewController.h"

@interface LUConsoleOverlayControllerSettings : NSObject

/// How many rows can be visible at the same time
@property (nonatomic, assign) NSUInteger maxVisibleRows;

/// How much time each row would be displayed on the screen
@property (nonatomic, assign) NSTimeInterval rowDisplayTime;

+ (instancetype)settings;

@end

@interface LUConsoleOverlayController : LUViewController

+ (instancetype)controllerWithConsole:(LUConsole *)console settings:(LUConsoleOverlayControllerSettings *)settings;
- (instancetype)initWithConsole:(LUConsole *)console settings:(LUConsoleOverlayControllerSettings *)settings;

@end

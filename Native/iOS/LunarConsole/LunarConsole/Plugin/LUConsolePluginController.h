//
//  LUConsolePluginViewController.h
//  LunarConsole
//
//  Created by Alex Lementuev on 2/26/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "LUViewController.h"

@class LUConsolePlugin;
@class LUConsolePluginController;

@protocol LUConsolePluginControllerDelegate <NSObject>

- (void)pluginControllerDidOpen:(LUConsolePluginController *)controller;
- (void)pluginControllerDidClose:(LUConsolePluginController *)controller;

@end

@interface LUConsolePluginController : LUViewController

@property (nonatomic, assign) id<LUConsolePluginControllerDelegate> delegate;

+ (instancetype)controllerWithPlugin:(LUConsolePlugin *)consolePlugin;
- (instancetype)initWithPlugin:(LUConsolePlugin *)consolePlugin;

@end

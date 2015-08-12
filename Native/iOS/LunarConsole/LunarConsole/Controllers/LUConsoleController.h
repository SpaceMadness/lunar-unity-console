//
//  LunarConsoleController.h
//  LunarConsole
//
//  Created by Alex Lementuev on 8/3/15.
//  Copyright (c) 2015 Space Madness. All rights reserved.
//

#import <UIKit/UIKit.h>

@class LUConsole;
@class LUConsoleController;

@protocol LUConsoleControllerDelegate <NSObject>

@optional
- (void)consoleControllerDidClose:(LUConsoleController *)controller;

@end

@interface LUConsoleController : UIViewController

@property (nonatomic, assign) id<LUConsoleControllerDelegate> delegate;

+ (instancetype)controllerWithConsole:(LUConsole *)console;
- (instancetype)initWithConsole:(LUConsole *)console;

@end

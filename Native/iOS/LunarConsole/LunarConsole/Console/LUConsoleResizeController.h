//
//  LUConsoleResizeController.h
//  LunarConsole
//
//  Created by Alex Lementuev on 11/28/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import <UIKit/UIKit.h>

@class LUConsoleResizeController;

@protocol LUConsoleResizeControllerDelegate <NSObject>

- (void)consoleResizeControllerDidClose:(LUConsoleResizeController *)controller;

@end

@interface LUConsoleResizeController : UIViewController

@property (nonatomic, weak) id<LUConsoleResizeControllerDelegate> delegate;

@end

//
//  LUConsoleMenuController.h
//  LunarConsole
//
//  Created by Alex Lementuev on 1/31/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import <UIKit/UIKit.h>

@class LUConsoleMenuController;

@interface LUConsoleMenuControllerButton : NSObject

+ (instancetype)buttonWithTitle:(NSString *)title target:(id)target action:(SEL)action;
- (instancetype)initWithTitle:(NSString *)title target:(id)target action:(SEL)action;

@end

@protocol LUConsoleMenuControllerDelegate <NSObject>

- (void)menuControllerDidRequestClose:(LUConsoleMenuController *)controller;

@end


@interface LUConsoleMenuController : UIViewController

@property (nonatomic, assign) id<LUConsoleMenuControllerDelegate> delegate;

- (void)addButtonTitle:(NSString *)title target:(id)target action:(SEL)action;

@end

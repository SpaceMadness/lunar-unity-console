//
//  LUConsolePopupController.h
//  LunarConsole
//
//  Created by Alex Lementuev on 12/1/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "LUViewController.h"

@class LUConsolePopupController;

@protocol LUConsolePopupControllerDelegate <NSObject>

- (void)popupControllerDidClose:(LUConsolePopupController *)controller;

@optional
- (void)popupControllerDidPressMoreButton:(LUConsolePopupController *)controller;

@end

@interface LUConsolePopupController : LUViewController

@property (nonatomic, readonly) UIViewController *contentController;
@property (nonatomic, strong) UIImage *iconImage;
@property (nonatomic, strong) NSString *popupTitle;
@property (nonatomic, weak) id<LUConsolePopupControllerDelegate> popupDelegate;

- (instancetype)initWithContentController:(UIViewController *)contentController;

@end

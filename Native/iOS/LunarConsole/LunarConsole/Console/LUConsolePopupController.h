//
//  LUConsolePopupController.h
//  LunarConsole
//
//  Created by Alex Lementuev on 12/1/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "LUViewController.h"

@class LUConsolePopupButton;
@class LUConsolePopupController;

@protocol LUConsolePopupControllerDelegate <NSObject>

- (void)popupControllerDidDismiss:(LUConsolePopupController *)controller;

@end

typedef void(^LUConsolePopupButtonCallback)(LUConsolePopupButton *button);

@interface LUConsolePopupButton : NSObject

+ (instancetype)buttonWithIcon:(UIImage *)icon target:(id)target action:(SEL)action;
- (instancetype)initWithIcon:(UIImage *)icon target:(id)target action:(SEL)action;

@end

@interface LUConsolePopupController : LUViewController

@property (nonatomic, readonly) LUViewController *contentController;
@property (nonatomic, weak) id<LUConsolePopupControllerDelegate> popupDelegate;

- (instancetype)initWithContentController:(LUViewController *)contentController;

- (void)presentFromController:(UIViewController *)controller animated:(BOOL)animated;
- (void)dismissAnimated:(BOOL)animated;

@end

//
//  LUExceptionWarningController.h
//  LunarConsole
//
//  Created by Alex Lementuev on 8/8/15.
//  Copyright (c) 2015 Space Madness. All rights reserved.
//

#import <UIKit/UIKit.h>

@class LUExceptionWarningController;

@protocol LUExceptionWarningControllerDelegate <NSObject>

- (void)exceptionWarningControllerDidShow:(LUExceptionWarningController *)controller;
- (void)exceptionWarningControllerDidDismiss:(LUExceptionWarningController *)controller;

@end

@interface LUExceptionWarningController : UIViewController

@property (nonatomic, assign) id<LUExceptionWarningControllerDelegate> delegate;

- (instancetype)initWithMessage:(NSString *)message;

@end

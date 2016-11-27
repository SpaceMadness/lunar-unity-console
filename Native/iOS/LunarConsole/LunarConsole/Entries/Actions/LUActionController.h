//
//  LUActionController.h
//  LunarConsole
//
//  Created by Alex Lementuev on 2/23/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "LUViewController.h"

@class LUActionController;
@class LUActionRegistry;

@protocol LUActionControllerDelegate <NSObject>

@required
- (void)actionController:(LUActionController *)controller didSelectActionWithId:(int)actionId;

@end

@interface LUActionController : LUViewController

@property (nonatomic, weak) id<LUActionControllerDelegate> delegate;

+ (instancetype)controllerWithActionRegistry:(LUActionRegistry *)actionRegistry;
- (instancetype)initWithActionRegistry:(LUActionRegistry *)actionRegistry;

@end

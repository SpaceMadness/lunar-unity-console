//
//  LUActionControllerBase.h
//  LunarConsole
//
//  Created by Alex Lementuev on 2/6/17.
//  Copyright © 2017 Space Madness. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "LUViewController.h"

@class LUActionController;
@class LUActionRegistry;

extern NSString * const LUActionControllerDidChangeVariable;
extern NSString * const LUActionControllerDidChangeVariableKeyVariable;

extern NSString * const LUActionControllerDidSelectAction;
extern NSString * const LUActionControllerDidSelectActionKeyAction;

@interface LUActionControllerBase : LUViewController

+ (instancetype)controllerWithActionRegistry:(LUActionRegistry *)actionRegistry;
- (instancetype)initWithActionRegistry:(LUActionRegistry *)actionRegistry;

@end

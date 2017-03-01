//
//  LUCVarEditController.h
//  LunarConsole
//
//  Created by Alex Lementuev on 2/18/17.
//  Copyright © 2017 Space Madness. All rights reserved.
//

#import "LUViewController.h"

@class LUCVar;
@class LUCVarEditController;

@protocol LUCVarEditControllerDelegate <NSObject>

- (void)editController:(LUCVarEditController *)controller didChangeValue:(NSString *)value;

@end

@interface LUCVarEditController : LUViewController

@property (nonatomic, weak) id<LUCVarEditControllerDelegate> delegate;

- (instancetype)initWithVariable:(LUCVar *)variable;

@end

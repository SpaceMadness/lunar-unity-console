//
//  LUActionButton.h
//  LunarConsole
//
//  Created by Alex Lementuev on 11/26/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import <UIKit/UIKit.h>

@class LUActionButton;

@protocol LUActionButtonDelegate <NSObject>

- (void)actionButtonDidStartEdit:(LUActionButton *)actionButton;

@end

@interface LUActionButton : UIButton

@property (nonatomic, weak) id<LUActionButtonDelegate> stateDelegate;

@end

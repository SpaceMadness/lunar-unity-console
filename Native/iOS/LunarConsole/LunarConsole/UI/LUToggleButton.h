//
//  LUToggleButton.h
//  
//
//  Created by Alex Lementuev on 8/10/15.
//
//

#import <UIKit/UIKit.h>

@class LUToggleButton;

@protocol LUToggleButtonDelegate <NSObject>

- (void)toggleButtonStateChanged:(LUToggleButton *)button;

@end

@interface LUToggleButton : UIButton

@property (nonatomic, assign, getter=isOn) BOOL on;
@property (nonatomic, assign) id<LUToggleButtonDelegate> delegate;

@end

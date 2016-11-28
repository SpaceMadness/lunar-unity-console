//
//  LUResizeBarView.h
//  LunarConsole
//
//  Created by Alex Lementuev on 11/27/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import <UIKit/UIKit.h>

@class LUResizeBarView;

typedef void (^LUResizeBarViewCallback)(LUResizeBarView *resizeBarView, CGPoint translation);

@interface LUResizeBarView : UIView

@property (nonatomic, assign) BOOL horizontal;
@property (nonatomic, assign) BOOL vertical;

@property (nonatomic, copy) LUResizeBarViewCallback callback;

@end

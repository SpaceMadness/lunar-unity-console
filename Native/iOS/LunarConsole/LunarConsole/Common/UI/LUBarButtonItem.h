//
//  LUBarButtonItem.h
//  LunarConsole
//
//  Created by Alex Lementuev on 12/29/18.
//  Copyright © 2018 Space Madness. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@class LUBarButtonItem;

typedef void(^LUBarButtonItemCallback)(LUBarButtonItem *button);

@interface LUBarButtonItem : UIBarButtonItem

- (instancetype)initWithTitle:(NSString *)title style:(UIBarButtonItemStyle)style handler:(LUBarButtonItemCallback)callback;

@end

NS_ASSUME_NONNULL_END

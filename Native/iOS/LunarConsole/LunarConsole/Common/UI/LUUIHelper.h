//
//  UIHelper.h
//  LunarConsole
//
//  Created by Alex Lementuev on 3/29/19.
//  Copyright © 2019 Space Madness. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface LUUIHelper : NSObject

+ (void)view:(UIView *)view centerInParent:(UIView *)parent;
+ (CGRect)safeAreaRect;

@end

NS_ASSUME_NONNULL_END

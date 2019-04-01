//
//  UIHelper.h
//  LunarConsole
//
//  Created by Alex Lementuev on 3/29/19.
//  Copyright © 2019 Space Madness. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface LUAlertAction : NSObject

@property (nonatomic, readonly) NSString *title;
@property (nullable, nonatomic, copy, readonly) void (^handler)(LUAlertAction *action);

- (instancetype)initWithTitle:(NSString *)title handler:(void (^ __nullable)(LUAlertAction *action))handler;

@end

@interface LUUIHelper : NSObject

+ (void)showAlertViewWithTitle:(NSString *)title message:(NSString *)message;
+ (void)showAlertViewWithTitle:(NSString *)title message:(NSString *)message actions:(NSArray<LUAlertAction *> *)actions;
+ (void)view:(UIView *)view centerInParent:(UIView *)parent;
+ (CGRect)safeAreaRect;

@end

NS_ASSUME_NONNULL_END

//
//  UIHelper.m
//  LunarConsole
//
//  Created by Alex Lementuev on 3/29/19.
//  Copyright © 2019 Space Madness. All rights reserved.
//

#import "LUUIHelper.h"
#import "LULittleHelper.h"
#import "LUAvailability.h"

@implementation LUUIHelper

+ (void)view:(UIView *)view centerInParent:(UIView *)parent
{
	view.translatesAutoresizingMaskIntoConstraints = NO;
	NSArray *constraints = @[
		[NSLayoutConstraint constraintWithItem:view
									 attribute:NSLayoutAttributeLeading
									 relatedBy:NSLayoutRelationEqual
										toItem:parent
									 attribute:NSLayoutAttributeLeading
									multiplier:1.0
									  constant:0],
		[NSLayoutConstraint constraintWithItem:view
									 attribute:NSLayoutAttributeTrailing
									 relatedBy:NSLayoutRelationEqual
										toItem:parent
									 attribute:NSLayoutAttributeTrailing
									multiplier:1.0
									  constant:0],
        [NSLayoutConstraint constraintWithItem:view
									 attribute:NSLayoutAttributeTop
									 relatedBy:NSLayoutRelationEqual
										toItem:parent
									 attribute:NSLayoutAttributeTop
									multiplier:1.0
									  constant:0],
		[NSLayoutConstraint constraintWithItem:view
									 attribute:NSLayoutAttributeBottom
									 relatedBy:NSLayoutRelationEqual
										toItem:parent
									 attribute:NSLayoutAttributeBottom
									multiplier:1.0
									  constant:0]
	];
	[NSLayoutConstraint activateConstraints:constraints];
}

+ (CGRect)safeAreaRect
{
	if (@available(iOS 11.0, *)) {
		return [UIApplication sharedApplication].keyWindow.safeAreaLayoutGuide.layoutFrame;
	}
	
	return [UIScreen mainScreen].bounds;
}

@end

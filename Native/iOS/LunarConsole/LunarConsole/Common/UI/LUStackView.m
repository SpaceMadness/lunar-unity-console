//
//  LUStackView.m
//  LunarConsole
//
//  Created by Alex Lementuev on 3/27/19.
//  Copyright © 2019 Space Madness. All rights reserved.
//

#import "LUStackView.h"

@implementation LUStackView

- (void)setBackgroundColor:(UIColor *)backgroundColor {
	UIView *backgroundView = [UIView new];
	backgroundView.opaque = YES;
	backgroundView.backgroundColor = backgroundColor;
	backgroundView.translatesAutoresizingMaskIntoConstraints = NO;
	[self insertSubview:backgroundView atIndex:0];
	[NSLayoutConstraint activateConstraints:@[
        [NSLayoutConstraint constraintWithItem:backgroundView
									 attribute:NSLayoutAttributeLeading
									 relatedBy:NSLayoutRelationEqual
										toItem:self
									 attribute:NSLayoutAttributeLeading
									multiplier:1.0
									  constant:0.0],
		[NSLayoutConstraint constraintWithItem:backgroundView
									 attribute:NSLayoutAttributeTrailing
									 relatedBy:NSLayoutRelationEqual
										toItem:self
									 attribute:NSLayoutAttributeTrailing
									multiplier:1.0
									  constant:0.0],
		[NSLayoutConstraint constraintWithItem:backgroundView
									 attribute:NSLayoutAttributeTop
									 relatedBy:NSLayoutRelationEqual
										toItem:self
									 attribute:NSLayoutAttributeTop
									multiplier:1.0
									  constant:0.0],
		[NSLayoutConstraint constraintWithItem:backgroundView
									 attribute:NSLayoutAttributeBottom
									 relatedBy:NSLayoutRelationEqual
										toItem:self
									 attribute:NSLayoutAttributeBottom
									multiplier:1.0
									  constant:0.0],
	]];
}

@end

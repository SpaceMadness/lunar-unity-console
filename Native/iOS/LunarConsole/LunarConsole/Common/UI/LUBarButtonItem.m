//
//  LUBarButtonItem.m
//  LunarConsole
//
//  Created by Alex Lementuev on 12/29/18.
//  Copyright © 2018 Space Madness. All rights reserved.
//

#import "LUBarButtonItem.h"

@interface LUBarButtonItem ()

@property (nonatomic, copy) LUBarButtonItemCallback callback;

@end

@implementation LUBarButtonItem

- (instancetype)initWithTitle:(NSString *)title style:(UIBarButtonItemStyle)style handler:(LUBarButtonItemCallback)callback
{
	self = [super initWithTitle:title style:style target:self action:@selector(buttonPressed)];
	if (self)
	{
		_callback = callback;
	}
	return self;
}

#pragma mark -
#pragma mark Action

- (void)buttonPressed
{
	if (_callback) {
		_callback(self);
	}
}

@end

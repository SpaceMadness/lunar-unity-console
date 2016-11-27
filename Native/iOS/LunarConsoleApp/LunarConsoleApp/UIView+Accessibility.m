//
//  UIView+Accessibility.m
//  LunarConsoleApp
//
//  Created by Alex Lementuev on 11/27/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "UIView+Accessibility.h"

@implementation UIView (Accessibility)

- (void)setTestAccessibilityIdentifier:(NSString *)accessibilityIdentifier
{
    self.isAccessibilityElement = YES;
    self.accessibilityIdentifier = accessibilityIdentifier;
}

@end

//
//  LUPassTouchView.m
//  LunarConsole
//
//  Created by Alex Lementuev on 11/26/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "LUPassTouchView.h"

@implementation LUPassTouchView

- (BOOL)pointInside:(CGPoint)point withEvent:(UIEvent *)event
{
    for (UIView *subview in self.subviews)
    {
        if (CGRectContainsPoint(subview.frame, point))
        {
            return YES;
        }
    }
    return NO;
}

@end

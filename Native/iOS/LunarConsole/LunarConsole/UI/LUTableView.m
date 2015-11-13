//
//  LUTableView.m
//  LunarConsole
//
//  Created by Alex Lementuev on 11/12/15.
//  Copyright © 2015 Space Madness. All rights reserved.
//

#import "LUTableView.h"

@implementation LUTableView

#pragma mark -
#pragma mark Touch handling

- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event
{
    [super touchesBegan:touches withEvent:event];
    
    if ([_touchDelegate respondsToSelector:@selector(tableView:touchesBegan:withEvent:)])
    {
        [_touchDelegate tableView:self touchesBegan:touches withEvent:event];
    }
}

- (void)touchesCancelled:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event
{
    [super touchesCancelled:touches withEvent:event];
    
    if ([_touchDelegate respondsToSelector:@selector(tableView:touchesCancelled:withEvent:)])
    {
        [_touchDelegate tableView:self touchesCancelled:touches withEvent:event];
    }
}

- (void)touchesMoved:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event
{
    [super touchesMoved:touches withEvent:event];
    
    if ([_touchDelegate respondsToSelector:@selector(tableView:touchesMoved:withEvent:)])
    {
        [_touchDelegate tableView:self touchesMoved:touches withEvent:event];
    }
}

- (void)touchesEnded:(NSSet<UITouch *> *)touches withEvent:(nullable UIEvent *)event
{
    [super touchesEnded:touches withEvent:event];
    
    if ([_touchDelegate respondsToSelector:@selector(tableView:touchesEnded:withEvent:)])
    {
        [_touchDelegate tableView:self touchesEnded:touches withEvent:event];
    }
}

@end

//
//  LUConsoleResizeController.m
//  LunarConsole
//
//  Created by Alex Lementuev on 11/28/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "LUConsoleResizeController.h"

#import "Lunar.h"

static const CGFloat kMinWidth = 320;
static const CGFloat kMinHeight = 320;

typedef enum : NSUInteger {
    LUConsoleResizeOperationNone        = 0,
    LUConsoleResizeOperationMove        = 1 << 1,
    LUConsoleResizeOperationTop         = 1 << 2,
    LUConsoleResizeOperationBottom      = 1 << 3,
    LUConsoleResizeOperationLeft        = 1 << 4,
    LUConsoleResizeOperationRight       = 1 << 5,
    LUConsoleResizeOperationTopLeft     = LUConsoleResizeOperationTop | LUConsoleResizeOperationLeft,
    LUConsoleResizeOperationTopRight    = LUConsoleResizeOperationTop | LUConsoleResizeOperationRight,
    LUConsoleResizeOperationBottomLeft  = LUConsoleResizeOperationBottom | LUConsoleResizeOperationLeft,
    LUConsoleResizeOperationBottomRight = LUConsoleResizeOperationBottom | LUConsoleResizeOperationRight
} LUConsoleResizeOperation;

@interface LUConsoleResizeController ()
{
    CGPoint _touchStart;
    UITouch * _initialTouch;
    LUConsoleResizeOperation _resizeOperation;
}

@property (weak, nonatomic) IBOutlet UIView *resizeTopBar;
@property (weak, nonatomic) IBOutlet UIView *resizeBottomBar;
@property (weak, nonatomic) IBOutlet UIView *resizeLeftBar;
@property (weak, nonatomic) IBOutlet UIView *resizeRightBar;
@property (weak, nonatomic) IBOutlet UIView *resizeTopLeftBar;
@property (weak, nonatomic) IBOutlet UIView *resizeTopRightBar;
@property (weak, nonatomic) IBOutlet UIView *resizeBottomLeftBar;
@property (weak, nonatomic) IBOutlet UIView *resizeBottomRightBar;

@end

@implementation LUConsoleResizeController

- (instancetype)init {
    self = [super initWithNibName:NSStringFromClass([self class]) bundle:nil];
    if (self)
    {
    }
    return self;
}

#pragma mark -
#pragma mark Touch handling

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
    if (_initialTouch == nil)
    {
        _initialTouch = [touches anyObject];
        _touchStart = [_initialTouch locationInView:self.view];
        _resizeOperation = [self lookupResizeOperationForView:_initialTouch.view];
    }
}

- (void)touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event {
    CGPoint touchPoint = [[touches anyObject] locationInView:self.view];
    CGPoint previous = [[touches anyObject] previousLocationInView:self.view];
    
    CGFloat deltaWidth = touchPoint.x - previous.x;
    CGFloat deltaHeight = touchPoint.y - previous.y;
    
    CGRect oldFrame = self.view.frame;
    
    // get the frame values so we can calculate changes below
    CGFloat x = self.view.frame.origin.x;
    CGFloat y = self.view.frame.origin.y;
    CGFloat width = self.view.frame.size.width;
    CGFloat height = self.view.frame.size.height;
    
    if (_resizeOperation & LUConsoleResizeOperationTop)
    {
        if (deltaHeight < 0 && y + deltaHeight < 0)
        {
            deltaHeight = -y;
        }
        else if (deltaHeight > 0 && height - deltaHeight < kMinHeight)
        {
            deltaHeight = height - kMinHeight;
        }
        
        self.view.frame = CGRectMake(x, y + deltaHeight, width, height - deltaHeight);
    }
    else if (_resizeOperation & LUConsoleResizeOperationBottom)
    {
        self.view.frame = CGRectMake(x, y, width, MAX(height + deltaHeight, kMinHeight));
    }
    
    if (_resizeOperation & LUConsoleResizeOperationLeft)
    {
        if (deltaWidth < 0 && x + deltaWidth < 0)
        {
            deltaWidth = -x;
        }
        else if (deltaWidth > 0 && width - deltaWidth < kMinWidth)
        {
            deltaWidth = width - kMinWidth;
        }
        
        self.view.frame = CGRectMake(x + deltaWidth, y, width - deltaWidth, height);
    }
    else if (_resizeOperation & LUConsoleResizeOperationRight)
    {
        self.view.frame = CGRectMake(x, y, MAX(width + deltaWidth, kMinWidth), height);
    }
    
    if (_resizeOperation == LUConsoleResizeOperationMove)
    {
        // not dragging from a corner -- move the view
        CGPoint center = CGPointMake(self.view.center.x + touchPoint.x - _touchStart.x,
                                     self.view.center.y + touchPoint.y - _touchStart.y);
        center.x = MAX(0.5 * width, center.x);
        center.y = MAX(0.5 * height, center.y);
        
        center.x = MIN(CGRectGetWidth(self.view.superview.bounds) - 0.5 * width, center.x);
        center.y = MIN(CGRectGetHeight(self.view.superview.bounds) - 0.5 * height, center.y);
        self.view.center = center;
    }
    
    if (CGRectEqualToRect(self.view.frame, oldFrame))
    {
        _touchStart = [_initialTouch locationInView:self.view];
    }
}

- (void)touchesEnded:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event
{
    _initialTouch = nil;
}

#pragma mark -
#pragma mark Actions

- (IBAction)onClose:(id)sender {
    if ([_delegate respondsToSelector:@selector(consoleResizeControllerDidClose:)])
    {
        [_delegate consoleResizeControllerDidClose:self];
    }
}

#pragma mark -
#pragma mark Helpers

- (LUConsoleResizeOperation)lookupResizeOperationForView:(UIView *)view
{
    if (view == _resizeTopBar)
        return LUConsoleResizeOperationTop;
    if (view == _resizeBottomBar)
        return LUConsoleResizeOperationBottom;
    if (view == _resizeLeftBar)
        return LUConsoleResizeOperationLeft;
    if (view == _resizeRightBar)
        return LUConsoleResizeOperationRight;
    if (view == _resizeTopLeftBar)
        return LUConsoleResizeOperationTopLeft;
    if (view == _resizeTopRightBar)
        return LUConsoleResizeOperationTopRight;
    if (view == _resizeBottomLeftBar)
        return LUConsoleResizeOperationBottomLeft;
    if (view == _resizeBottomRightBar)
        return LUConsoleResizeOperationBottomRight;
    
    return LUConsoleResizeOperationMove;
}

@end

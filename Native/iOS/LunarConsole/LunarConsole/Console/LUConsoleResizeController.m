//
//  LUConsoleResizeController.m
//  LunarConsole
//
//  Created by Alex Lementuev on 11/28/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "LUConsoleResizeController.h"

#import "Lunar.h"

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

- (void)viewDidLoad {
    [super viewDidLoad];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark -
#pragma mark Touch handling

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
    UITouch *touch = [touches anyObject];
    _touchStart = [touch locationInView:self.view];
    _resizeOperation = [self lookupResizeOperationForView:touch.view];
}

- (void)touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event {
    CGPoint touchPoint = [[touches anyObject] locationInView:self.view];
    CGPoint previous = [[touches anyObject] previousLocationInView:self.view];
    
    CGFloat deltaWidth = touchPoint.x - previous.x;
    CGFloat deltaHeight = touchPoint.y - previous.y;
    
    // get the frame values so we can calculate changes below
    CGFloat x = self.view.frame.origin.x;
    CGFloat y = self.view.frame.origin.y;
    CGFloat width = self.view.frame.size.width;
    CGFloat height = self.view.frame.size.height;
    
    if (_resizeOperation & LUConsoleResizeOperationTop)
    {
        self.view.frame = CGRectMake(x, y + deltaHeight, width, height - deltaHeight);
    }
    else if (_resizeOperation & LUConsoleResizeOperationBottom)
    {
        self.view.frame = CGRectMake(x, y, width, height + deltaHeight);
    }
    
    if (_resizeOperation & LUConsoleResizeOperationLeft)
    {
        self.view.frame = CGRectMake(x + deltaWidth, y, width - deltaWidth, height);
    }
    else if (_resizeOperation & LUConsoleResizeOperationRight)
    {
        self.view.frame = CGRectMake(x, y, width + deltaWidth, height);
    }
    
    if (_resizeOperation == LUConsoleResizeOperationMove)
    {
        // not dragging from a corner -- move the view
        self.view.center = CGPointMake(self.view.center.x + touchPoint.x - _touchStart.x,
                                       self.view.center.y + touchPoint.y - _touchStart.y);
    }
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

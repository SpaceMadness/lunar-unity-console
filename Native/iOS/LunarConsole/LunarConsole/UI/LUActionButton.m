//
//  LUActionButton.m
//  LunarConsole
//
//  Created by Alex Lementuev on 11/26/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "LUActionButton.h"

#import "Lunar.h"

@interface LUActionButton ()
{
    UIGestureRecognizer * _longPressGestureRecognizer;
    UIGestureRecognizer * _panViewGestureRecognizer;
}
@end

@implementation LUActionButton

- (instancetype)initWithCoder:(NSCoder *)aDecoder
{
    self = [super initWithCoder:aDecoder];
    if (self)
    {
        LUButtonSkin *skin = [LUTheme mainTheme].actionButtonLargeSkin;
        
        [self setBackgroundImage:skin.normalImage forState:UIControlStateNormal];
        [self setBackgroundImage:skin.selectedImage forState:UIControlStateHighlighted];
        
        _longPressGestureRecognizer = [[UILongPressGestureRecognizer alloc] initWithTarget:self
                                                                                    action:@selector(handleLongPressGesture:)];
        [self addGestureRecognizer:_longPressGestureRecognizer];
    }
    return self;
}

- (void)handleLongPressGesture:(UILongPressGestureRecognizer *)gestureRecognizer
{
    [self removeGestureRecognizer:gestureRecognizer];
    
    _panViewGestureRecognizer = [LUPanViewGestureRecognizer new];
    [self addGestureRecognizer:_panViewGestureRecognizer];
}

@end

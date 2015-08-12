//
//  LUToggleButton.m
//  
//
//  Created by Alex Lementuev on 8/10/15.
//
//

#import "LUToggleButton.h"

@implementation LUToggleButton

- (instancetype)initWithCoder:(NSCoder *)aDecoder
{
    self = [super initWithCoder:aDecoder];
    if (self)
    {
        [self addTarget:self action:@selector(onClick:) forControlEvents:UIControlEventTouchUpInside];
    }
    return self;
}

#pragma mark -
#pragma mark Actions

- (void)onClick:(id)sender
{
    self.on = !self.isOn;
}

#pragma mark -
#pragma mark Properties

- (BOOL)isOn
{
    return self.selected;
}

- (void)setOn:(BOOL)on
{
    if (self.selected != on)
    {
        self.selected = on;
        if ([_delegate respondsToSelector:@selector(toggleButtonStateChanged:)])
        {
            [_delegate toggleButtonStateChanged:self];
        }
    }
}

@end

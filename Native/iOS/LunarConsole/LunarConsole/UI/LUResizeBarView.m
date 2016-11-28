//
//  LUResizeBarView.m
//  LunarConsole
//
//  Created by Alex Lementuev on 11/27/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "LUResizeBarView.h"

@implementation LUResizeBarView

- (instancetype)initWithCoder:(NSCoder *)aDecoder {
    self = [super initWithCoder:aDecoder];
    if (self) {
        UIPanGestureRecognizer *gestureRecognizer = [[UIPanGestureRecognizer alloc] initWithTarget:self
                                                                                            action:@selector(onPanGesture:)];
        [self addGestureRecognizer:gestureRecognizer];
    }
    return self;
}

#pragma mark -
#pragma mark Gesture recognition

- (void)onPanGesture:(UIPanGestureRecognizer *)gestureRecognizer {
    if (_callback) {
        _callback(self, [gestureRecognizer translationInView:[self superview]]);
    }
}

@end

//
//  LUSlider.m
//  LunarConsole
//
//  Created by Alex Lementuev on 2/18/17.
//  Copyright © 2017 Space Madness. All rights reserved.
//

#import "LUSlider.h"

#import "Lunar.h"

@implementation LUSlider

- (instancetype)initWithCoder:(NSCoder *)aDecoder
{
    self = [super initWithCoder:aDecoder];
    if (self)
    {
        self.tintColor = [LUTheme mainTheme].switchTintColor;
    }
    return self;
}

@end

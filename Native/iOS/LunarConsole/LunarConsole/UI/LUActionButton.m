//
//  LUActionButton.m
//  LunarConsole
//
//  Created by Alex Lementuev on 11/26/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "LUActionButton.h"

#import "Lunar.h"

@implementation LUActionButton

- (instancetype)initWithCoder:(NSCoder *)aDecoder
{
    self = [super initWithCoder:aDecoder];
    if (self)
    {
        LUButtonSkin *skin = [LUTheme mainTheme].actionButtonLargeSkin;
        
        [self setBackgroundImage:skin.normalImage forState:UIControlStateNormal];
        [self setBackgroundImage:skin.selectedImage forState:UIControlStateHighlighted];
    }
    return self;
}

@end

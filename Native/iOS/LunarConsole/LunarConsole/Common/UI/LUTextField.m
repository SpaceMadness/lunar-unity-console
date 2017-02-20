//
//  LUTextField.m
//  LunarConsole
//
//  Created by Alex Lementuev on 2/19/17.
//  Copyright © 2017 Space Madness. All rights reserved.
//

#import "LUTextField.h"

#import "Lunar.h"

@implementation LUTextField

- (instancetype)initWithCoder:(NSCoder *)aDecoder
{
    self = [super initWithCoder:aDecoder];
    if (self)
    {
        self.backgroundColor = [LUTheme mainTheme].variableEditBackground;
        self.textColor = [LUTheme mainTheme].variableEditTextColor;
    }
    return self;
}

@end

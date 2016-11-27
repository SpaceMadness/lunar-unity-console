//
//  LUConsoleActionTableViewCell.m
//  LunarConsole
//
//  Created by Alex Lementuev on 2/24/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "Lunar.h"

#import "LUActionTableViewCell.h"

@implementation LUActionTableViewCell

+ (instancetype)cellWithReuseIdentifier:(nullable NSString *)reuseIdentifier
{
    return [[[self class] alloc] initWithReuseIdentifier:reuseIdentifier];
}

- (instancetype)initWithReuseIdentifier:(nullable NSString *)reuseIdentifier
{
    self = [super initWithStyle:UITableViewCellStyleDefault reuseIdentifier:reuseIdentifier];
    if (self)
    {
        LUTheme *theme = [LUTheme mainTheme];
        
        self.textLabel.font = theme.actionsFont;
        self.textLabel.textColor = theme.actionsTextColor;
        LU_SET_ACCESSIBILITY_IDENTIFIER(self.textLabel, @"Action Title");
    }
    return self;
}

#pragma mark -
#pragma mark Properties

- (NSString *)title
{
    return self.textLabel.text;
}

- (void)setTitle:(NSString *)title
{
    self.textLabel.text = title;
}

- (UIColor *)cellColor
{
    return self.contentView.backgroundColor;
}

- (void)setCellColor:(UIColor * __nullable)cellColor
{
    self.contentView.backgroundColor = cellColor;
}

@end

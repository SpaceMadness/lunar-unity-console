//
//  LUCVarTableViewCell.m
//  LunarConsole
//
//  Created by Alex Lementuev on 4/7/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "LUCVarTableViewCell.h"

#import "Lunar.h"

@interface LUCVarTableViewCell ()

@property (nonatomic, weak) IBOutlet UILabel *titleLabel;

@end

@implementation LUCVarTableViewCell

- (instancetype)initWithReuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:UITableViewCellStyleDefault reuseIdentifier:reuseIdentifier];
    if (self)
    {
        [self createCellView];
    }
    return self;
}

#pragma mark -
#pragma mark Loading

- (void)createCellView
{
    UIView *view = [[NSBundle mainBundle] loadNibNamed:self.cellNibName owner:self options:nil].firstObject;
    view.frame = self.contentView.bounds;
    view.backgroundColor = [UIColor clearColor];
    view.opaque = YES;
    view.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
    [self.contentView addSubview:view];
}

- (NSString *)cellNibName
{
    return NSStringFromClass([self class]);
}

#pragma mark -
#pragma mark Inheritance

- (void)setupVariable:(LUCVar *)variable
{
    _variableId = variable.actionId;
    
    _titleLabel.text = variable.name;
    
    LUTheme *theme = [LUTheme mainTheme];
    _titleLabel.textColor = theme.actionsTextColor;
    _titleLabel.font = theme.actionsFont;
    _titleLabel.backgroundColor = [UIColor clearColor];
    _titleLabel.opaque = YES;
}

- (void)notifyValueChanged:(NSString *)value
{
    if ([_delegate respondsToSelector:@selector(consoleVariableTableViewCell:didChangeValue:)])
    {
        [_delegate consoleVariableTableViewCell:self didChangeValue:value];
    }
}

@end

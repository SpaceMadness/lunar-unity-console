//
//  LUCVarTableViewCell.m
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2016 Alex Lementuev, SpaceMadness.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
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

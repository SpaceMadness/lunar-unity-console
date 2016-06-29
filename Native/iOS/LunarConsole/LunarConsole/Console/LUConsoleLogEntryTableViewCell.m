//
//  LUConsoleTableCell.m
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

#import "LUConsoleLogEntryTableViewCell.h"

#import "Lunar.h"

static UIEdgeInsets _messageInsets;

@interface LUConsoleLogEntryTableViewCell ()
{
    UILabel     * _messageLabel;
    UIImageView * _iconView;
}

@end

@interface LUConsoleLogEntryTableViewCell (Inheritance)

- (LUTheme *)theme;

@end

@implementation LUConsoleLogEntryTableViewCell

+ (void)load
{
    if (!LU_IOS_MIN_VERSION_AVAILABLE)
    {
        return;
    }
    
    if ([self class] == [LUConsoleLogEntryTableViewCell class])
    {
        LUTheme *theme = [LUTheme mainTheme];
        
        UIImage *icon = theme.cellLog.icon;
        CGFloat iconWidth = icon.size.width;
        CGFloat iconHeight = icon.size.height;
        CGFloat iconX = 0.5 * (theme.cellHeight - iconHeight);

        _messageInsets = UIEdgeInsetsMake(theme.indentVer, iconX + iconWidth + iconX, theme.indentVer, theme.indentHor);
    }
}

+ (instancetype)cellWithFrame:(CGRect)frame reuseIdentifier:(nullable NSString *)reuseIdentifier
{
    return LU_AUTORELEASE([[[self class] alloc] initWithFrame:frame reuseIdentifier:reuseIdentifier]);
}

- (instancetype)initWithFrame:(CGRect)frame reuseIdentifier:(nullable NSString *)reuseIdentifier
{
    self = [super initWithStyle:UITableViewCellStyleDefault reuseIdentifier:reuseIdentifier];
    if (self)
    {
        self.contentView.bounds = frame;
        
        LUTheme *theme = [self theme];
        // icon
        UIImage *iconImage = theme.cellLog.icon;
        
        _iconView = [[UIImageView alloc] initWithImage:iconImage];
        CGFloat iconWidth = iconImage.size.width;
        CGFloat iconHeight = iconImage.size.height;
        CGFloat iconX = 0.5 * (theme.cellHeight - iconHeight);
        CGFloat iconY = 0.5 * (CGRectGetHeight(frame) - iconHeight);
        _iconView.frame = CGRectMake(iconX, iconY, iconWidth, iconHeight);
        
        [self.contentView addSubview:_iconView];
        
        // message
        CGFloat messageX = _messageInsets.left;
        CGFloat messageY = _messageInsets.top;
        CGFloat messageWidth = CGRectGetWidth(frame) - (_messageInsets.left + _messageInsets.right);
        CGFloat messageHeight = CGRectGetHeight(frame) - (_messageInsets.top + _messageInsets.bottom);
        
        _messageLabel = [[UILabel alloc] initWithFrame:CGRectMake(messageX, messageY, messageWidth, messageHeight)];
        _messageLabel.font = theme.font;
        _messageLabel.lineBreakMode = theme.lineBreakMode;
        _messageLabel.numberOfLines = 0;
        _messageLabel.opaque = YES;
        LU_SET_ACCESSIBILITY_IDENTIFIER(_messageLabel, @"Log Message Label");
        
        [self.contentView addSubview:_messageLabel];
    }
    return self;
}

- (void)dealloc
{
    LU_RELEASE(_messageLabel);
    LU_RELEASE(_iconView);
    LU_SUPER_DEALLOC
}

- (void)setSize:(CGSize)size
{
    self.contentView.bounds = CGRectMake(0, 0, size.width, size.height);

    // icon
    CGRect iconFrame = _iconView.frame;
    iconFrame.origin.y = 0.5 * (size.height - CGRectGetHeight(iconFrame));
    _iconView.frame = iconFrame;
    
    // message
    CGFloat messageX = _messageInsets.left;
    CGFloat messageY = _messageInsets.top;
    CGFloat messageWidth = size.width - (_messageInsets.left + _messageInsets.right);
    CGFloat messageHeight = size.height - (_messageInsets.top + _messageInsets.bottom);
    
    _messageLabel.frame = CGRectMake(messageX, messageY, messageWidth, messageHeight);
}

+ (CGFloat)heightForCellWithText:(nullable NSString *)text width:(CGFloat)width
{
    LUTheme *theme = [LUTheme mainTheme];
    
    CGSize constraintSize = CGSizeMake(width - (_messageInsets.left + _messageInsets.right), CGFLOAT_MAX);
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wdeprecated-declarations"
    CGFloat textHeight = [text sizeWithFont:theme.font constrainedToSize:constraintSize lineBreakMode:theme.lineBreakMode].height;
    CGFloat height = (int)(textHeight + (_messageInsets.top + _messageInsets.bottom) + .99); // size should not be a fracture number (or gray lines will appear)
#pragma clang diagnostic pop
    return MAX(theme.cellHeight, height);
}

#pragma mark -
#pragma mark Properties

- (LUTheme *)theme
{
    return [LUTheme mainTheme];
}

- (UIColor *)cellColor
{
    return _messageLabel.backgroundColor;
}

- (void)setCellColor:(UIColor * __nullable)cellColor
{
    _messageLabel.backgroundColor = cellColor;
    self.contentView.backgroundColor = cellColor;
}

- (UIImage *)icon
{
    return _iconView.image;
}

- (void)setIcon:(UIImage * __nullable)icon
{
    _iconView.image = icon;
}

- (NSString *)message
{
    return _messageLabel.text;
}

- (void)setMessage:(NSString *)message
{
    _messageLabel.text = message;
}

- (UIColor *)messageColor
{
    return _messageLabel.textColor;
}

- (void)setMessageColor:(UIColor * __nullable)messageColor
{
    _messageLabel.textColor = messageColor;
}

@end

@interface LUConsoleCollapsedLogEntryTableViewCell ()
{
    UIImageView * _backgroundImageView;
    UILabel     * _countLabel;
}

@end

@implementation LUConsoleCollapsedLogEntryTableViewCell

- (instancetype)initWithFrame:(CGRect)frame reuseIdentifier:(nullable NSString *)reuseIdentifier
{
    self = [super initWithFrame:frame reuseIdentifier:reuseIdentifier];
    if (self)
    {
        LUTheme *theme = [self theme];
        
        // background
        _backgroundImageView = [[UIImageView alloc] initWithImage:theme.collapseBackgroundImage];
        _backgroundImageView.contentMode = UIViewContentModeScaleToFill;
        [self.contentView addSubview:_backgroundImageView];
        
        // count
        _countLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 0, 0)];
        _countLabel.font = theme.font;
        _countLabel.numberOfLines = 1;
        _countLabel.lineBreakMode = NSLineBreakByClipping;
        _countLabel.opaque = YES;
        _countLabel.textColor = theme.collapseTextColor;
        _countLabel.backgroundColor = theme.collapseBackgroundColor;
        LU_SET_ACCESSIBILITY_IDENTIFIER(_countLabel, @"Log Collapse Label");
        
        [self.contentView addSubview:_countLabel];
    }
    return self;
}

- (void)dealloc
{
    LU_RELEASE(_backgroundImageView);
    LU_RELEASE(_countLabel);
    LU_SUPER_DEALLOC
}

- (void)setSize:(CGSize)size
{
    [super setSize:size];
    
    if (_backgroundImageView.hidden || _countLabel.hidden)
    {
        return; // no need to resize invisible elements
    }
    
    // resize count label to fit the content
    [_countLabel sizeToFit];
    
    CGRect countFrame = _countLabel.frame;
    CGRect backgroundImageFrame = _backgroundImageView.frame;
    
    CGFloat backgroundImageWidth = 9 + CGRectGetWidth(countFrame) + 9;
    CGFloat backgroundImageHeight = CGRectGetHeight(backgroundImageFrame);
    
    backgroundImageFrame.origin.y = 0.5 * (size.height - CGRectGetHeight(backgroundImageFrame));
    backgroundImageFrame.origin.x = size.width - (backgroundImageWidth + 0.5 * ([LUTheme mainTheme].cellHeight - backgroundImageHeight));
    backgroundImageFrame.size.width = backgroundImageWidth;
    _backgroundImageView.frame = backgroundImageFrame;
    
    countFrame.origin.x = backgroundImageFrame.origin.x + 9;
    countFrame.origin.y = 0.5 * (size.height - CGRectGetHeight(countFrame));
    _countLabel.frame = countFrame;
}

- (void)setCollapsedCount:(NSInteger)collapsedCount
{
    BOOL shouldBeHidden = collapsedCount <= 1;
    _countLabel.hidden = shouldBeHidden;
    _backgroundImageView.hidden = shouldBeHidden;
    
    _collapsedCount = collapsedCount;
    _countLabel.text = collapsedCount > 999 ? @"999+" : [NSString stringWithFormat:@"%ld", (long)collapsedCount];
}

@end

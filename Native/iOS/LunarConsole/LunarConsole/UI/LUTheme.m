//
//  LUTheme.m
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

#import "LUTheme.h"

#import "Lunar.h"

static LUTheme * _mainTheme;

@interface LUTheme ()

@property (nonatomic, strong) UIColor *tableColor;
@property (nonatomic, strong) UIColor *logButtonTitleColor;
@property (nonatomic, strong) UIColor *logButtonTitleSelectedColor;

@property (nonatomic, strong) LUCellSkin *cellLog;
@property (nonatomic, strong) LUCellSkin *cellError;
@property (nonatomic, strong) LUCellSkin *cellWarning;

@property (nonatomic, strong) UIColor *backgroundColorLight;
@property (nonatomic, strong) UIColor *backgroundColorDark;

@property (nonatomic, strong) UIFont *font;
@property (nonatomic, strong) UIFont *fontOverlay;
@property (nonatomic, strong) UIFont *fontSmall;
@property (nonatomic, assign) NSLineBreakMode lineBreakMode;
@property (nonatomic, assign) CGFloat cellHeight;
@property (nonatomic, assign) CGFloat indentHor;
@property (nonatomic, assign) CGFloat indentVer;
@property (nonatomic, assign) CGFloat cellHeightTiny;
@property (nonatomic, assign) CGFloat indentHorTiny;
@property (nonatomic, assign) CGFloat indentVerTiny;
@property (nonatomic, assign) CGFloat buttonWidth;
@property (nonatomic, assign) CGFloat buttonHeight;

@property (nonatomic, strong) LUButtonSkin *actionButtonLargeSkin;

@property (nonatomic, strong) UIImage *collapseBackgroundImage;
@property (nonatomic, strong) UIColor *collapseBackgroundColor;
@property (nonatomic, strong) UIColor *collapseTextColor;

@property (nonatomic, strong) UIFont  *contextMenuFont;
@property (nonatomic, strong) UIColor *contextMenuBackgroundColor;
@property (nonatomic, strong) UIColor *contextMenuTextColor;
@property (nonatomic, strong) UIColor *contextMenuTextHighlightColor;

@property (nonatomic, strong) UIColor *switchTintColor;
@end

@interface LUCellSkin ()

@property (nonatomic, strong) UIImage *icon;
@property (nonatomic, strong) UIColor *textColor;
@property (nonatomic, strong) UIColor *backgroundColorLight;
@property (nonatomic, strong) UIColor *backgroundColorDark;
@property (nonatomic, strong) UIColor *overlayTextColor;

@end

@interface LUButtonSkin ()

@property (nonatomic, strong) UIImage *normalImage;
@property (nonatomic, strong) UIImage *selectedImage;
@property (nonatomic, strong) UIFont  *font;

@end

static UIColor * UIColorMake(int rgb)
{
    CGFloat red = ((rgb >> 16) & 0xff) / 255.0;
    CGFloat green = ((rgb >> 8) & 0xff) / 255.0;
    CGFloat blue = (rgb & 0xff) / 255.0;
    return [UIColor colorWithRed:red green:green blue:blue alpha:1.0f];
}

static UIImage * CreateCollapseBackgroundImage()
{
    UIImage *collapseImage = [UIImage imageNamed:@"lunar_console_collapse_background.png"];

    if ([UIScreen mainScreen].scale == 2.0)
    {
        CGFloat offset = 23 / 2.0;
        return [collapseImage resizableImageWithCapInsets:UIEdgeInsetsMake(offset, offset, offset, offset)];
    }
    
    if ([UIScreen mainScreen].scale == 1.0) // should not get there - just a sanity check
    {
        CGFloat offset = 11;
        return [collapseImage resizableImageWithCapInsets:UIEdgeInsetsMake(offset, offset, offset, offset)];
    }
    
    CGFloat offset = 35 / 3.0;
    return [collapseImage resizableImageWithCapInsets:UIEdgeInsetsMake(offset, offset, offset, offset)];
}

@implementation LUTheme

+ (void)initialize
{
    if ([self class] == [LUTheme class])
    {
        LUCellSkin *cellLog = [LUCellSkin cellSkin];
        cellLog.icon = [UIImage imageNamed:@"lunar_console_icon_log.png"];
        cellLog.textColor = UIColorMake(0xb1b1b1);
        cellLog.backgroundColorLight = UIColorMake(0x3c3c3c);
        cellLog.backgroundColorDark = UIColorMake(0x373737);
        cellLog.overlayTextColor = UIColorMake(0xadadad);
        
        LUCellSkin *cellError = [LUCellSkin cellSkin];
        cellError.icon = [UIImage imageNamed:@"lunar_console_icon_log_error.png"];
        cellError.textColor = cellLog.textColor;
        cellError.backgroundColorLight = cellLog.backgroundColorLight;
        cellError.backgroundColorDark = cellLog.backgroundColorDark;
        cellError.overlayTextColor = UIColorMake(0xfc0000);
        
        LUCellSkin *cellWarning = [LUCellSkin cellSkin];
        cellWarning.icon = [UIImage imageNamed:@"lunar_console_icon_log_warning.png"];
        cellWarning.textColor = cellLog.textColor;
        cellWarning.backgroundColorLight = cellLog.backgroundColorLight;
        cellWarning.backgroundColorDark = cellLog.backgroundColorDark;
        cellWarning.overlayTextColor = UIColorMake(0xf4f600);
        
        _mainTheme = [LUTheme new];
        _mainTheme.tableColor = UIColorMake(0x2c2c27);
        _mainTheme.logButtonTitleColor = UIColorMake(0xb1b1b1);
        _mainTheme.logButtonTitleSelectedColor = UIColorMake(0x595959);
        _mainTheme.cellLog = cellLog;
        _mainTheme.cellError = cellError;
        _mainTheme.cellWarning = cellWarning;
        _mainTheme.backgroundColorLight = cellLog.backgroundColorLight;
        _mainTheme.backgroundColorDark = cellLog.backgroundColorDark;
        _mainTheme.font = [self createDefaultFont];
        _mainTheme.fontOverlay = [self createOverlayFont];
        _mainTheme.fontSmall = [self createSmallFont];
        _mainTheme.lineBreakMode = NSLineBreakByWordWrapping;
        _mainTheme.cellHeight = 32;
        _mainTheme.indentHor = 10;
        _mainTheme.indentVer = 2;
        _mainTheme.cellHeightTiny = 12;
        _mainTheme.indentHorTiny = 2;
        _mainTheme.indentVerTiny = 0;
        _mainTheme.buttonWidth = 46;
        _mainTheme.buttonHeight = 30;
        _mainTheme.collapseBackgroundImage = CreateCollapseBackgroundImage();
        _mainTheme.collapseBackgroundColor = UIColorMake(0x424242);
        _mainTheme.collapseTextColor = cellLog.textColor;
        _mainTheme.contextMenuFont = [self createContextMenuFont];
        _mainTheme.contextMenuBackgroundColor = UIColorMake(0x3c3c3c);
        _mainTheme.contextMenuTextColor = cellLog.textColor;
        _mainTheme.contextMenuTextHighlightColor = [UIColor whiteColor];
        _mainTheme.switchTintColor = UIColorMake(0xfed900);
        
        LUButtonSkin *actionButtonLargeSkin = [LUButtonSkin buttonSkin];
        actionButtonLargeSkin.normalImage = LUGet3SlicedImage(@"lunar_console_action_button_large_normal");
        actionButtonLargeSkin.selectedImage = LUGet3SlicedImage(@"lunar_console_action_button_large_selected");
        _mainTheme.actionButtonLargeSkin = actionButtonLargeSkin;
    }
}


+ (UIFont *)createDefaultFont
{
    UIFont *font = [UIFont fontWithName:@"Menlo-regular" size:10];
    if (font != nil)
    {
        return font;
    }
    
    return [UIFont systemFontOfSize:10];
}

+ (UIFont *)createOverlayFont
{
    UIFont *font = [UIFont fontWithName:@"Menlo-bold" size:10];
    if (font != nil)
    {
        return font;
    }
    
    return [UIFont systemFontOfSize:10];
}

+ (UIFont *)createSmallFont
{
    UIFont *font = [UIFont fontWithName:@"Menlo-regular" size:8];
    if (font != nil)
    {
        return font;
    }
    
    return [UIFont systemFontOfSize:8];
}

+ (UIFont *)createContextMenuFont
{
    UIFont *font = [UIFont fontWithName:@"Menlo-regular" size:12];
    if (font != nil)
    {
        return font;
    }
    
    return [UIFont systemFontOfSize:12];
}

+ (LUTheme *)mainTheme
{
    return _mainTheme;
}

@end

@implementation LUCellSkin

+ (instancetype)cellSkin
{
    return [[self alloc] init];
}

@end

@implementation LUButtonSkin

+ (instancetype)buttonSkin
{
    return [[self alloc] init];
}

@end

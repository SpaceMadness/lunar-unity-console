//
//  LUTheme.m
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2015 Alex Lementuev, SpaceMadness.
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

@property (nonatomic, strong) UIFont *font;
@property (nonatomic, strong) UIFont *fontSmall;
@property (nonatomic, assign) NSLineBreakMode lineBreakMode;
@property (nonatomic, assign) CGFloat cellHeight;
@property (nonatomic, assign) CGFloat indentHor;
@property (nonatomic, assign) CGFloat indentVer;
@property (nonatomic, assign) CGFloat buttonWidth;
@property (nonatomic, assign) CGFloat buttonHeight;

@end

@interface LUCellSkin ()

@property (nonatomic, strong) UIImage *icon;
@property (nonatomic, strong) UIColor *textColor;
@property (nonatomic, strong) UIColor *backgroundColorLight;
@property (nonatomic, strong) UIColor *backgroundColorDark;

@end

static UIColor * UIColorMake(int rgb)
{
    CGFloat red = ((rgb >> 16) & 0xff) / 255.0;
    CGFloat green = ((rgb >> 8) & 0xff) / 255.0;
    CGFloat blue = (rgb & 0xff) / 255.0;
    return [UIColor colorWithRed:red green:green blue:blue alpha:1.0f];
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
        
        LUCellSkin *cellError = [LUCellSkin cellSkin];
        cellError.icon = [UIImage imageNamed:@"lunar_console_icon_log_error.png"];
        cellError.textColor = cellLog.textColor;
        cellError.backgroundColorLight = cellLog.backgroundColorLight;
        cellError.backgroundColorDark = cellLog.backgroundColorDark;
        
        LUCellSkin *cellWarning = [LUCellSkin cellSkin];
        cellWarning.icon = [UIImage imageNamed:@"lunar_console_icon_log_warning.png"];
        cellWarning.textColor = cellLog.textColor;
        cellWarning.backgroundColorLight = cellLog.backgroundColorLight;
        cellWarning.backgroundColorDark = cellLog.backgroundColorDark;
        
        _mainTheme = [LUTheme new];
        _mainTheme.tableColor = UIColorMake(0x2c2c27);
        _mainTheme.logButtonTitleColor = UIColorMake(0xb1b1b1);
        _mainTheme.logButtonTitleSelectedColor = UIColorMake(0x595959);
        _mainTheme.cellLog = cellLog;
        _mainTheme.cellError = cellError;
        _mainTheme.cellWarning = cellWarning;
        _mainTheme.font = [self createDefaultFont];
        _mainTheme.fontSmall = [self createSmallFont];
        _mainTheme.lineBreakMode = NSLineBreakByWordWrapping;
        _mainTheme.cellHeight = 32;
        _mainTheme.indentHor = 10;
        _mainTheme.indentVer = 2;
        _mainTheme.buttonWidth = 46;
        _mainTheme.buttonHeight = 30;
    }
}

- (void)dealloc
{
    LU_RELEASE(_tableColor);
    LU_RELEASE(_logButtonTitleColor);
    LU_RELEASE(_logButtonTitleSelectedColor);
    LU_RELEASE(_cellLog);
    LU_RELEASE(_cellWarning);
    LU_RELEASE(_cellError);
    LU_RELEASE(_font);
    LU_RELEASE(_fontSmall);
    
    LU_SUPER_DEALLOC
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

+ (UIFont *)createSmallFont
{
    UIFont *font = [UIFont fontWithName:@"Menlo-regular" size:8];
    if (font != nil)
    {
        return font;
    }
    
    return [UIFont systemFontOfSize:8];
}

+ (LUTheme *)mainTheme
{
    return _mainTheme;
}

@end

@implementation LUCellSkin

+ (instancetype)cellSkin
{
    return LU_AUTORELEASE([[[self class] alloc] init]);
}

- (void)dealloc
{
    LU_RELEASE(_icon);
    LU_RELEASE(_textColor);
    LU_RELEASE(_backgroundColorLight);
    LU_RELEASE(_backgroundColorDark);
    LU_SUPER_DEALLOC
}

@end

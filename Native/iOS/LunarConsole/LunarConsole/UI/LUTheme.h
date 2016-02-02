//
//  LUTheme.h
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

#import <UIKit/UIKit.h>

@interface LUCellSkin : NSObject

+ (instancetype)cellSkin;

@property (nonatomic, readonly) UIImage *icon;
@property (nonatomic, readonly) UIColor *textColor;
@property (nonatomic, readonly) UIColor *backgroundColorLight;
@property (nonatomic, readonly) UIColor *backgroundColorDark;

@end

@interface LUTheme : NSObject

@property (nonatomic, readonly) UIColor *tableColor;
@property (nonatomic, readonly) UIColor *logButtonTitleColor;
@property (nonatomic, readonly) UIColor *logButtonTitleSelectedColor;

@property (nonatomic, readonly) LUCellSkin *cellLog;
@property (nonatomic, readonly) LUCellSkin *cellError;
@property (nonatomic, readonly) LUCellSkin *cellWarning;

@property (nonatomic, readonly) UIFont *font;
@property (nonatomic, readonly) UIFont *fontSmall;
@property (nonatomic, readonly) NSLineBreakMode lineBreakMode;

@property (nonatomic, readonly) CGFloat cellHeight;
@property (nonatomic, readonly) CGFloat indentHor;
@property (nonatomic, readonly) CGFloat indentVer;
@property (nonatomic, readonly) CGFloat buttonWidth;
@property (nonatomic, readonly) CGFloat buttonHeight;

@property (nonatomic, readonly) UIImage *collapseBackgroundImage;
@property (nonatomic, readonly) UIColor *collapseBackgroundColor;
@property (nonatomic, readonly) UIColor *collapseTextColor;

@property (nonatomic, readonly) UIFont  *contextMenuFont;
@property (nonatomic, readonly) UIColor *contextMenuBackgroundColor;
@property (nonatomic, readonly) UIColor *contextMenuTextColor;
@property (nonatomic, readonly) UIColor *contextMenuTextHighlightColor;

+ (LUTheme *)mainTheme;

@end


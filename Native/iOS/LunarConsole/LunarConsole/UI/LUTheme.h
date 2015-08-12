//
//  LunarTheme.h
//  LunarConsole
//
//  Created by Alex Lementuev on 8/4/15.
//  Copyright © 2015 Space Madness. All rights reserved.
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
@property (nonatomic, readonly) NSLineBreakMode lineBreakMode;

@property (nonatomic, readonly) CGFloat cellHeight;
@property (nonatomic, readonly) CGFloat indentHor;
@property (nonatomic, readonly) CGFloat indentVer;
@property (nonatomic, readonly) CGFloat buttonWidth;
@property (nonatomic, readonly) CGFloat buttonHeight;

+ (LUTheme *)mainTheme;

@end


//
//  LunarTableTextCell.h
//  LunarConsole
//
//  Created by Alex Lementuev on 8/3/15.
//  Copyright (c) 2015 Space Madness. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LUConsoleTableCell : UITableViewCell

@property (nonatomic, strong, nullable) UIImage  * icon;
@property (nonatomic, strong, nullable) NSString * message;
@property (nonatomic, strong, nullable) UIColor  * messageColor;
@property (nonatomic, strong, nullable) UIColor  * cellColor;

+ (nonnull instancetype)cellWithFrame:(CGRect)frame reuseIdentifier:(nullable NSString *)reuseIdentifier;
- (nonnull instancetype)initWithFrame:(CGRect)frame reuseIdentifier:(nullable NSString *)reuseIdentifier;

- (void)setSize:(CGSize)size;

+ (CGFloat)heightForCellWithText:(nullable NSString *)text width:(CGFloat)width;

@end

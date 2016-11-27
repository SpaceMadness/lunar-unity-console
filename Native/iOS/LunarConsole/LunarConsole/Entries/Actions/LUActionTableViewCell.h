//
//  LUConsoleActionTableViewCell.h
//  LunarConsole
//
//  Created by Alex Lementuev on 2/24/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LUActionTableViewCell : UITableViewCell

@property (nonatomic, strong, nullable) NSString * title;
@property (nonatomic, copy, nullable) UIColor    * cellColor;

+ (nonnull instancetype)cellWithReuseIdentifier:(nullable NSString *)reuseIdentifier;
- (nonnull instancetype)initWithReuseIdentifier:(nullable NSString *)reuseIdentifier;

@end

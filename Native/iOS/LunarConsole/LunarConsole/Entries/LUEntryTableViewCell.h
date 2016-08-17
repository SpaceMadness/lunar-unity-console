//
//  LUTableViewCell.h
//  LunarConsole
//
//  Created by Alex Lementuev on 4/1/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LUEntryTableViewCell : UITableViewCell

+ (nonnull instancetype)cellWithFrame:(CGRect)frame reuseIdentifier:(nullable NSString *)reuseIdentifier;
- (nonnull instancetype)initWithFrame:(CGRect)frame reuseIdentifier:(nullable NSString *)reuseIdentifier;

- (void)setSize:(CGSize)size;

@end

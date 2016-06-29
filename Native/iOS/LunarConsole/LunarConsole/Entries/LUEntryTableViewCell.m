//
//  LUTableViewCell.m
//  LunarConsole
//
//  Created by Alex Lementuev on 4/1/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "Lunar.h"

#import "LUEntryTableViewCell.h"

@implementation LUEntryTableViewCell

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
    }
    return self;
}

#pragma mark -
#pragma mark Size

- (void)setSize:(CGSize)size
{
    self.contentView.bounds = CGRectMake(0, 0, size.width, size.height);
}

@end

//
//  LUEntryTableViewCell.m
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

#import "Lunar.h"

#import "LUEntryTableViewCell.h"

@implementation LUEntryTableViewCell

+ (instancetype)cellWithFrame:(CGRect)frame cellIdentifier:(nullable NSString *)cellIdentifier
{
    return [[[self class] alloc] initWithFrame:frame cellIdentifier:cellIdentifier];
}

- (instancetype)initWithFrame:(CGRect)frame cellIdentifier:(nullable NSString *)cellIdentifier
{
    self = [super initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
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

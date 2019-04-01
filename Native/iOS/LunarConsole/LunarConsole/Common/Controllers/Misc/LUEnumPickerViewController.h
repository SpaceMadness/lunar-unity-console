//
//  LUEnumPickerViewController.h
//  LunarConsole
//
//  Created by Alex Lementuev on 3/22/19.
//  Copyright © 2019 Space Madness. All rights reserved.
//

#import "LUViewController.h"

NS_ASSUME_NONNULL_BEGIN

@interface LUEnumPickerViewController : LUViewController

@property (nonatomic, readonly) NSUInteger selectedIndex;
@property (nonatomic, readonly) NSString *selectedValue;

@property (nonatomic, strong) id userData;

- (instancetype)initWithValues:(NSArray<NSString *> *)values initialIndex:(NSUInteger)index;

@end

NS_ASSUME_NONNULL_END

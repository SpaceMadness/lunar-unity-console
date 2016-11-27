//
//  LUCVarTableViewCell.h
//  LunarConsole
//
//  Created by Alex Lementuev on 4/7/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import <UIKit/UIKit.h>

@class LUCVar;
@class LUCVarTableViewCell;

@protocol LUCVarTableViewCellDelegate <NSObject>

- (void)consoleVariableTableViewCell:(LUCVarTableViewCell *)cell didChangeValue:(NSString *)value;

@end

@interface LUCVarTableViewCell : UITableViewCell

@property (nonatomic, readonly) NSString * cellNibName;
@property (nonatomic, readonly) int variableId;
@property (nonatomic, assign) id<LUCVarTableViewCellDelegate> delegate;

- (instancetype)initWithReuseIdentifier:(NSString *)reuseIdentifier;

- (void)setupVariable:(LUCVar *)variable;
- (void)notifyValueChanged:(NSString *)value;

@end

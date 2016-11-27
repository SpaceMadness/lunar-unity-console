//
//  LUCVarInputTableViewCell.h
//  LunarConsole
//
//  Created by Alex Lementuev on 4/20/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "LUCVarTableViewCell.h"

@interface LUCVarInputTableViewCell : LUCVarTableViewCell

@property (nonatomic, retain) NSString *inputText;

- (BOOL)isValidInputText:(NSString *)text;

@end

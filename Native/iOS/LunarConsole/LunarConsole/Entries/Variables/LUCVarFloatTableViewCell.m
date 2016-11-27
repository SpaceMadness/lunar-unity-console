//
//  LUCVarFloatTableViewCell.m
//  LunarConsole
//
//  Created by Alex Lementuev on 4/20/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "LUCVarFloatTableViewCell.h"

#import "Lunar.h"

@implementation LUCVarFloatTableViewCell

#pragma mark -
#pragma mark Inheritance

- (BOOL)isValidInputText:(NSString *)text
{
    return LUStringTryParseFloat(text, NULL);
}

@end

//
//  LUCVarBooleanTableViewCell.m
//  LunarConsole
//
//  Created by Alex Lementuev on 4/20/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "Lunar.h"

#import "LUCVarBooleanTableViewCell.h"

@interface LUCVarBooleanTableViewCell ()

@property (nonatomic, weak) IBOutlet UISwitch * toggleSwitch;

@end

@implementation LUCVarBooleanTableViewCell

#pragma mark -
#pragma mark Variable

- (void)setupVariable:(LUCVar *)variable
{
    [super setupVariable:variable];
    _toggleSwitch.on = [variable.value isEqualToString:@"1"];
}

#pragma mark -
#pragma mark Actions

- (IBAction)onToggleSwitch:(id)sender
{
    [self notifyValueChanged:_toggleSwitch.isOn ? @"1" : @"0"];
}

@end

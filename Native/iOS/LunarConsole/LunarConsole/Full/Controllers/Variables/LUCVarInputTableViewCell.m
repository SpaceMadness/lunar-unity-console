//
//  LUCVarInputTableViewCell.m
//
//  Lunar Network
//
//  Copyright 2017 Alex Lementuev, SpaceMadness.
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

#import "LUCVarInputTableViewCell.h"

#import "Lunar-Full.h"

@interface LUCVarInputTableViewCell () <UITextFieldDelegate>

@property (nonatomic, weak) IBOutlet UITextField * inputField;
@property (nonatomic, weak) IBOutlet UIButton * resetButton;
@property (nonatomic, weak) IBOutlet NSLayoutConstraint * resetButtonWidthConstraint;

@property (nonatomic, assign) CGFloat resetButtonInitialWidth;

@end

@implementation LUCVarInputTableViewCell

#pragma mark -
#pragma mark Inheritance

- (void)setupVariable:(LUCVar *)variable
{
    [super setupVariable:variable];
    
    _resetButtonInitialWidth = _resetButtonWidthConstraint.constant;
    
    _inputField.backgroundColor = [LUTheme mainTheme].variableEditBackground;
    _inputField.textColor = [LUTheme mainTheme].variableEditTextColor;
    
    _inputField.text = variable.value;
    [self updateResetButton];
    
    LU_SET_ACCESSIBILITY_IDENTIFIER(_inputField, @"Variable Input Field");
    LU_SET_ACCESSIBILITY_IDENTIFIER(_resetButton, @"Variable Reset Button");
}

- (BOOL)isValidInputText:(NSString *)text
{
    LU_SHOULD_IMPLEMENT_METHOD
    return NO;
}

#pragma mark -
#pragma mark Actions

- (IBAction)onResetButton:(id)sender
{
    _inputField.text = self.variable.defaultValue;
    [self setVariableValue:self.variable.defaultValue];
}

#pragma mark -
#pragma mark Setup value

- (void)setVariableValue:(NSString *)value
{
    [super setVariableValue:value];
    [self updateResetButton];
}

#pragma mark -
#pragma mark Reset button

- (void)updateResetButton
{
    _resetButtonWidthConstraint.constant = self.variable.isDefaultValue ? 0 : _resetButtonInitialWidth;
    [self layoutIfNeeded];
}

#pragma mark -
#pragma mark Cell loading

- (NSString *)cellNibName
{
    return NSStringFromClass([LUCVarInputTableViewCell class]);
}

#pragma mark -
#pragma mark UITextFieldDelegate

- (void)textFieldDidEndEditing:(UITextField *)textField
{
    NSString *value = textField.text;
    
    if ([self isValidInputText:value])
    {
        [self setVariableValue:value];
    }
    else
    {
        LUDisplayAlertView(@"Input Error", [NSString stringWithFormat:@"Invalid value: '%@'", value]);
    }
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    return NO;
}

@end

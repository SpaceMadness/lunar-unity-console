//
//  LUCVarInputTableViewCell.m
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

#import "LUCVarInputTableViewCell.h"

#import "Lunar.h"

@interface LUCVarInputTableViewCell () <UITextFieldDelegate>

@property (nonatomic, weak) IBOutlet UITextField * inputField;
@property (nonatomic, weak) IBOutlet NSLayoutConstraint * resetButtonWidthConstraint;

@property (nonatomic, strong) NSString * lastValue;
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
    self.lastValue = variable.value;
}

- (BOOL)isValidInputText:(NSString *)text
{
    LU_SHOULD_IMPLEMENT_METHOD
    return NO;
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
        self.lastValue = value;
        [self notifyValueChanged:value];
    }
    else
    {
        LUDisplayAlertView(@"Input Error", [NSString stringWithFormat:@"Invalid value: '%@'", value]);
        textField.text = self.lastValue;
    }
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    return NO;
}

@end

//
//  LUCVarRangeController.m
//  LunarConsole
//
//  Created by Alex Lementuev on 2/18/17.
//  Copyright © 2017 Space Madness. All rights reserved.
//

#import "LUCVarRangeController.h"

#import "Lunar-Full.h"

@interface LUCVarRangeController () <UITextFieldDelegate>
{
    __weak LUCVar * _variable;
}

@property (nonatomic, weak) IBOutlet UISlider * slider;
@property (nonatomic, weak) IBOutlet UITextField * textField;
@property (nonatomic, weak) IBOutlet UILabel *errorLabel;
@property (nonatomic, weak) IBOutlet NSLayoutConstraint *errorLabelHeightConstraint;

@end

@implementation LUCVarRangeController

- (instancetype)initWithVariable:(LUCVar *)variable
{
    self = [super initWithNibName:NSStringFromClass([self class]) bundle:nil];
    if (self) {
        _variable = variable;
        self.popupTitle = _variable.name;
        self.popupIcon = LUGetImage(@"lunar_console_icon_settings");
        self.popupButtons = @[
            [LUConsolePopupButton buttonWithIcon:LUGetImage(@"lunar_console_icon_button_variable_reset")
                        target:self
                        action:@selector(onResetButton:)]
        ];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.view.backgroundColor = [LUTheme mainTheme].backgroundColorDark;
    
    CGFloat min = _variable.range.min;
    CGFloat max = _variable.range.max;
    if (max - min > 0.000001)
    {
        _slider.minimumValue = min;
        _slider.maximumValue = max;
        _errorLabelHeightConstraint.constant = 0.0f;
    }
    else
    {
        _slider.enabled = NO;
        _errorLabel.text = [NSString stringWithFormat:@"Invalid range [%g,  %g]", min, max];
    }
    
    [self updateVariableUI];
}

#pragma mark -
#pragma mark UI

- (void)updateVariableUI
{
    _slider.value = [_variable.value floatValue];
    _textField.text = [_variable value];
}

#pragma mark -
#pragma mark Actions

- (IBAction)sliderValueChanged:(id)sender
{
    UISlider *slider = sender;
    NSString *value = [[NSString alloc] initWithFormat:@"%g", slider.value];
    
    _textField.text = value;
}

- (IBAction)sliderEditingFinished:(id)sender
{
    UISlider *slider = sender;
    NSString *value = [[NSString alloc] initWithFormat:@"%g", slider.value];
    
    _textField.text = value;
    [self setVariableValue:value];
}

- (void)onResetButton:(id)sender
{
    [self setVariableValue:_variable.defaultValue];
    [self updateVariableUI];
}

#pragma mark -
#pragma mark UITextFieldDelegate

- (void)textFieldDidEndEditing:(UITextField *)textField
{
    NSString *valueStr = textField.text;
    float value;
    if (LUStringTryParseFloat(valueStr, &value))
    {
        if (value < _variable.range.min)
        {
            value = _variable.range.min;
        }
        else if (value > _variable.range.max)
        {
            value = _variable.range.max;
        }
        [self setVariableValue:[[NSString alloc] initWithFormat:@"%g", value]];
        [self updateVariableUI];
    }
    else
    {
        LUDisplayAlertView(@"Input Error", [NSString stringWithFormat:@"Invalid value: '%@'", valueStr]);
    }
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    return NO;
}

#pragma mark -
#pragma mark Popup Controller

- (CGSize)preferredPopupSize
{
    return CGSizeMake(0, 70);
}

#pragma mark -
#pragma mark Notifications

- (void)setVariableValue:(NSString *)value
{
    _variable.value = value;
    
    // post notification
    NSDictionary *userInfo = @{ LUActionControllerDidChangeVariableKeyVariable : _variable };
    [LUNotificationCenter postNotificationName:LUActionControllerDidChangeVariable
                                        object:nil
                                      userInfo:userInfo];
}

@end

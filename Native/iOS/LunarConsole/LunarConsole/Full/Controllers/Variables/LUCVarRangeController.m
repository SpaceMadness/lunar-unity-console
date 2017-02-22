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
    
    if (_variable.range.max - _variable.range.min > 0.000001)
    {
        _slider.maximumValue = _variable.range.max;
        _slider.minimumValue = _variable.range.min;
    }
    else
    {
        _slider.enabled = NO;
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

- (void)onResetButton:(id)sender
{
    [_variable resetToDefaultValue];
    [self updateVariableUI];
}

#pragma mark -
#pragma mark UITextFieldDelegate

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

@end

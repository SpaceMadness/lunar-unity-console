//
//  LUCVarFloatTableViewCell.m
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

#import "LUCVarFloatTableViewCell.h"

#import "Lunar-Full.h"

@interface LUCVarFloatTableViewCell() <LUConsolePopupControllerDelegate>
@end

@implementation LUCVarFloatTableViewCell

#pragma mark -
#pragma mark Inheritance

- (BOOL)isValidInputText:(NSString *)text
{
    return LUStringTryParseFloat(text, NULL);
}

#pragma mark -
#pragma mark UITextFieldDelegate

- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField
{
    if (self.variable.hasRange)
    {
        LUCVarRangeController *controller = [[LUCVarRangeController alloc] initWithVariable:self.variable];
        
        LUConsolePopupController *popupController = [[LUConsolePopupController alloc] initWithContentController:controller];
        popupController.popupDelegate = self;
        popupController.popupTitle = self.variable.name;
        popupController.popupIcon = LUGetImage(@"lunar_console_icon_settings");
        
        [popupController presentFromController:self.presentingController animated:YES];
        
        return NO;
    }
    
    return YES;
}

#pragma mark -
#pragma mark LUConsolePopupControllerDelegate

- (void)popupControllerDidDismiss:(LUConsolePopupController *)controller
{
    [controller dismissAnimated:YES];
}

@end

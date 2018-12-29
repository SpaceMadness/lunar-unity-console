//
//  TextInputView.m
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2018 Alex Lementuev, SpaceMadness.
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

#import "TextInputView.h"

#import "Lunar.h"
#import "UIView+Accessibility.h"

@interface TextInputView () <UIAlertViewDelegate>

@property (nonatomic, copy) TextInputViewCallback callback;

@end

@implementation TextInputView

- (instancetype)initWithTitle:(NSString *)title
                      message:(NSString *)message
            cancelButtonTitle:(NSString *)cancelButtonTitle
                     callback:(TextInputViewCallback)callback
{
    self = [super initWithTitle:title message:message delegate:self cancelButtonTitle:cancelButtonTitle otherButtonTitles:@"Cancel", nil];
    if (self) {
        self.alertViewStyle = UIAlertViewStylePlainTextInput;
        self.callback = callback;
        [[self textFieldAtIndex:0] setTestAccessibilityIdentifier:@"Text Input Field"];
    }
    return self;
}

#pragma mark -
#pragma mark UIAlertViewDelegate

#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wdeprecated-declarations"
- (void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex
{
    if (buttonIndex == 0 && _callback) {
        _callback(self, [self textFieldAtIndex:0].text);
    }
}
#pragma clang diagnostic pop

@end

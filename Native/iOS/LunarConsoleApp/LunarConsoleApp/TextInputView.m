//
//  TextInputView.m
//  LunarConsoleApp
//
//  Created by Alex Lementuev on 11/27/16.
//  Copyright © 2016 Space Madness. All rights reserved.
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

- (void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex
{
    if (buttonIndex == 0 && _callback) {
        _callback(self, [self textFieldAtIndex:0].text);
    }
}

@end

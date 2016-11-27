//
//  TextInputView.h
//  LunarConsoleApp
//
//  Created by Alex Lementuev on 11/27/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import <UIKit/UIKit.h>

@class TextInputView;

typedef void (^TextInputViewCallback) (TextInputView *textView, NSString *text);

@interface TextInputView : UIAlertView

- (instancetype)initWithTitle:(NSString *)title message:(NSString *)message cancelButtonTitle:(NSString *)cancelButtonTitle callback:(TextInputViewCallback)callback;

@end

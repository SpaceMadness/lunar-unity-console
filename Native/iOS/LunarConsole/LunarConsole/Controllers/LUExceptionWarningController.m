//
//  LUExceptionWarningController.m
//  LunarConsole
//
//  Created by Alex Lementuev on 8/8/15.
//  Copyright (c) 2015 Space Madness. All rights reserved.
//

#import "LUExceptionWarningController.h"

#import "Lunar.h"

@interface LUExceptionWarningController ()
{
    NSString * _message;
}

@property (nonatomic, assign) IBOutlet UILabel  *errorLabel;

@end

@implementation LUExceptionWarningController

- (instancetype)initWithMessage:(NSString *)message
{
    self = [super initWithNibName:NSStringFromClass([self class]) bundle:nil];
    if (self)
    {
        _message = LU_RETAIN(message);
    }
    return self;
}

- (void)dealloc
{
    LU_RELEASE(_message);
    LU_SUPER_DEALLOC
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    _errorLabel.text = _message;
}

#pragma mark -
#pragma mark Actions

- (IBAction)onShowButton:(id)sender
{
    if ([_delegate respondsToSelector:@selector(exceptionWarningControllerDidShow:)])
    {
        [_delegate exceptionWarningControllerDidShow:self];
    }
}

- (IBAction)onDismissButton:(id)sender
{
    if ([_delegate respondsToSelector:@selector(exceptionWarningControllerDidDismiss:)])
    {
        [_delegate exceptionWarningControllerDidDismiss:self];
    }
}

@end

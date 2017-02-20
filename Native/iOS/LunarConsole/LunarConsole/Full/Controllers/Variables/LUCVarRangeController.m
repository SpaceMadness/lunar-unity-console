//
//  LUCVarRangeController.m
//  LunarConsole
//
//  Created by Alex Lementuev on 2/18/17.
//  Copyright © 2017 Space Madness. All rights reserved.
//

#import "LUCVarRangeController.h"

#import "Lunar-Full.h"

@interface LUCVarRangeController ()
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
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.view.backgroundColor = [LUTheme mainTheme].backgroundColorDark;
}

#pragma mark -
#pragma mark Popup Controller

- (CGSize)preferredPopupSize
{
    return CGSizeMake(0, 70);
}

@end

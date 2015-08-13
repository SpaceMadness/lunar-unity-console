//
//  LUConsoleDetailsController.m
//  LunarConsole
//
//  Created by Alex Lementuev on 8/12/15.
//  Copyright © 2015 Space Madness. All rights reserved.
//

#import "LUConsoleDetailsController.h"

#import "Lunar.h"

@interface LUConsoleDetailsController ()
{
    LUConsoleEntry * _entry;
}

@property (nonatomic, assign) IBOutlet UITextView *textView;

@end

@implementation LUConsoleDetailsController

- (instancetype)initWithEntry:(LUConsoleEntry *)entry
{
    self = [super initWithNibName:NSStringFromClass([self class]) bundle:nil];
    if (self)
    {
        _entry = LU_RETAIN(entry);
    }
    return self;
}

- (void)dealloc
{
    LU_RELEASE(_entry);
    LU_SUPER_DEALLOC
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.title = @"Details";
    
    LUTheme *theme = [LUTheme mainTheme];
    self.textView.text = [NSString stringWithFormat:@"%@\n\n%@", _entry.message, _entry.stackTrace];
    self.textView.font = theme.font;
    self.textView.textColor = theme.cellLog.textColor;
    self.textView.backgroundColor = theme.cellLog.backgroundColorLight;
    
    self.navigationController.navigationBar.tintColor = theme.tableColor;
}

@end

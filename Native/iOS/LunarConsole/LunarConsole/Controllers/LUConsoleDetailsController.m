//
//  LUConsoleDetailsController.m
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2015 Alex Lementuev, SpaceMadness.
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

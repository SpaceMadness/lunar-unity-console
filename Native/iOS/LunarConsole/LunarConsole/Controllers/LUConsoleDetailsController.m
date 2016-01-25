//
//  LUConsoleDetailsController.m
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2016 Alex Lementuev, SpaceMadness.
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

#define NO_STACK_TRACE_WARNING @"No stack trace found.\n\nMake sure \"Development Build\" checkbox is checked under \"Build Settings\" (File ▶ Build Settings...)"

@interface LUConsoleDetailsController ()
{
    LUConsoleEntry * _entry;
}

@property (nonatomic, assign) IBOutlet NSLayoutConstraint * contentWidthConstraint;
@property (nonatomic, assign) IBOutlet NSLayoutConstraint * contentHeightConstraint;
@property (nonatomic, assign) IBOutlet UIView             * contentView;
@property (nonatomic, assign) IBOutlet UIView             * bottomBarView;
@property (nonatomic, assign) IBOutlet UIImageView        * iconView;
@property (nonatomic, assign) IBOutlet UILabel            * messageView;
@property (nonatomic, assign) IBOutlet UITextView         * stackTraceView;

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
    
    // size
    CGSize screenSize = LUGetScreenBounds().size;
    _contentWidthConstraint.constant = screenSize.width - 20;
    _contentHeightConstraint.constant = 2 * screenSize.height / 3;
    
    // colors
    self.view.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.5];
    
    LUTheme *theme = [LUTheme mainTheme];
    
    _iconView.image = _entry.icon;
    
    _contentView.backgroundColor = theme.cellLog.backgroundColorLight;
    _contentView.layer.borderColor = [[UIColor colorWithRed:0.37 green:0.37 blue:0.37 alpha:1.0] CGColor];
    _contentView.layer.borderWidth = 2;
    
    _messageView.text = _entry.message;
    _messageView.font = theme.font;
    _messageView.textColor = theme.cellLog.textColor;
    
    NSString *stackTrace = [_entry hasStackTrace] ?
        [LUStacktrace optimizeStacktrace:_entry.stackTrace] : NO_STACK_TRACE_WARNING;
    
    _stackTraceView.text = stackTrace;
    _stackTraceView.font = theme.fontSmall;
    _stackTraceView.textColor = theme.cellLog.textColor;
    
    _bottomBarView.backgroundColor = theme.tableColor;
    
    // update layout
    [self.view layoutIfNeeded];
}

#pragma mark -
#pragma mark Actions

- (IBAction)onCopy:(id)sender
{
    UIPasteboard *pastboard = [UIPasteboard generalPasteboard];
    
    NSString *text = _entry.message;
    if ([_entry hasStackTrace])
    {
        text = [text stringByAppendingFormat:@"\n\n%@", _entry.stackTrace];
    }
    [pastboard setString:text];
}

- (IBAction)onClose:(id)sender
{
    if ([_delegate respondsToSelector:@selector(detailsControllerDidClose:)])
    {
        [_delegate detailsControllerDidClose:self];
    }
}

@end

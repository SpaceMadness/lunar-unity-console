//
//  LUConsolePopupController.m
//  LunarConsole
//
//  Created by Alex Lementuev on 12/1/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "LUConsolePopupController.h"
#import "Lunar.h"

@interface LUConsolePopupController ()

@property (nonatomic, weak) IBOutlet UIImageView *iconImageView;
@property (nonatomic, weak) IBOutlet UILabel *titleLabel;
@property (nonatomic, weak) IBOutlet UIButton *buttonMore;
@property (nonatomic, weak) IBOutlet UIView *popupView;
@property (nonatomic, weak) IBOutlet UIView *contentView;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *contentWidthConstraint;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *contentHeightConstraint;

@end

@implementation LUConsolePopupController

- (instancetype)initWithContentController:(UIViewController *)contentController
{
    self = [super initWithNibName:NSStringFromClass([self class]) bundle:nil];
    if (self)
    {
        _contentController = contentController;
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    // colors
    self.view.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.5];
    
    LUTheme *theme = [LUTheme mainTheme];
    
    _popupView.backgroundColor = theme.tableColor;
    _contentView.backgroundColor = theme.tableColor;
    _contentView.translatesAutoresizingMaskIntoConstraints = NO;
    
    _popupView.layer.borderColor = [[UIColor colorWithRed:0.37 green:0.37 blue:0.37 alpha:1.0] CGColor];
    _popupView.layer.borderWidth = 2;
    
    _titleLabel.textColor = theme.cellLog.textColor;
    
    // content controller
    [self addContentController:_contentController];
}

- (void)viewDidLayoutSubviews
{
    [super viewDidLayoutSubviews];
    
    CGSize fullSize = self.view.bounds.size;
    CGFloat contentWidth, contentHeight;
    
    if (fullSize.width < fullSize.height)
    {
        contentWidth = MAX(320, 2 * fullSize.width / 3) - 2 * 20;
        contentHeight = 1.5 * contentWidth;
    }
    else
    {
        contentHeight = MAX(320, 2 * fullSize.height / 3) - 2 * 20;
        contentWidth = 1.5 * contentHeight;
    }
    
    self.contentWidthConstraint.constant = contentWidth;
    self.contentHeightConstraint.constant = contentHeight;
}

#pragma mark -
#pragma mark Content controller

- (void)addContentController:(UIViewController *)contentController
{
    [self addChildViewController:contentController];
    contentController.view.frame = _contentView.bounds;
    [_contentView addSubview:contentController.view];
    [contentController didMoveToParentViewController:self];
    
    // make content controller occupy the content view
    NSArray<NSLayoutConstraint *> *constraints = @[
        [NSLayoutConstraint constraintWithItem:contentController.view
                                     attribute:NSLayoutAttributeWidth
                                     relatedBy:NSLayoutRelationEqual
                                        toItem:_contentView
                                     attribute:NSLayoutAttributeWidth
                                    multiplier:1.0
                                      constant:0],
        [NSLayoutConstraint constraintWithItem:contentController.view
                                     attribute:NSLayoutAttributeHeight
                                     relatedBy:NSLayoutRelationEqual
                                        toItem:_contentView
                                     attribute:NSLayoutAttributeHeight
                                    multiplier:1.0
                                      constant:0],
        [NSLayoutConstraint constraintWithItem:contentController.view
                                     attribute:NSLayoutAttributeCenterX
                                     relatedBy:NSLayoutRelationEqual
                                        toItem:_contentView
                                     attribute:NSLayoutAttributeCenterX
                                    multiplier:1.0
                                      constant:0],
        [NSLayoutConstraint constraintWithItem:contentController.view
                                     attribute:NSLayoutAttributeCenterY
                                     relatedBy:NSLayoutRelationEqual
                                        toItem:_contentView
                                     attribute:NSLayoutAttributeCenterY
                                    multiplier:1.0
                                      constant:0]
    ];
    
    [NSLayoutConstraint activateConstraints:constraints];
}

#pragma mark -
#pragma mark Actions

- (IBAction)onClose:(id)sender
{
    id delegate = _contentController;
    if ([delegate respondsToSelector:@selector(popupControllerDidClose:)])
    {
        [delegate popupControllerDidClose:self];
    }
}

#pragma mark -
#pragma mark Properties

@end

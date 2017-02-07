//
//  LUActionControllerBase.m
//  LunarConsole
//
//  Created by Alex Lementuev on 2/6/17.
//  Copyright © 2017 Space Madness. All rights reserved.
//

#import "Lunar.h"

#import "LUActionControllerBase.h"

NSString * const LUActionControllerDidSelectAction = @"LUActionControllerDidSelectAction";
NSString * const LUActionControllerDidSelectActionKeyAction = @"action";

@interface LUActionControllerBase ()

@property (nonatomic, weak) IBOutlet UIView       * noActionsWarningView;
@property (nonatomic, weak) IBOutlet UILabel      * noActionsWarningLabel;
@property (nonatomic, weak) IBOutlet UITableView  * tableView;
@property (nonatomic, weak) IBOutlet UISearchBar  * filterBar;

@end

@implementation LUActionControllerBase

+ (instancetype)controllerWithActionRegistry:(LUActionRegistry *)actionRegistry
{
    return [[self alloc] initWithActionRegistry:actionRegistry];
}

- (instancetype)initWithActionRegistry:(LUActionRegistry *)actionRegistry
{
    self = [super initWithNibName:NSStringFromClass([self class]) bundle:nil];
    if (self)
    {
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    LUTheme *theme = [LUTheme mainTheme];
    
    // title
    self.title = @"Actions";
    
    // background
    self.view.opaque = YES;
    self.view.backgroundColor = theme.tableColor;
    
    // table view
    _tableView.backgroundColor = theme.tableColor;
    
    // no actions warning
    _noActionsWarningView.backgroundColor = theme.tableColor;
    _noActionsWarningView.opaque = YES;
    _noActionsWarningLabel.font = theme.actionsWarningFont;
    _noActionsWarningLabel.textColor = theme.actionsWarningTextColor;
    
    [self updateNoActionWarningView];
    
    // accessibility
    LU_SET_ACCESSIBILITY_IDENTIFIER(_noActionsWarningView, @"No Actions Warning View");
}

#pragma mark -
#pragma mark No actions warning view

- (void)updateNoActionWarningView
{
    [self setNoActionsWarningViewHidden:NO];
}

- (void)setNoActionsWarningViewHidden:(BOOL)hidden
{
    _tableView.hidden = !hidden;
    _filterBar.hidden = !hidden;
    _noActionsWarningView.hidden = hidden;
}

#pragma mark -
#pragma Interface Builder actions

- (IBAction)onInfoButton:(id)sender
{
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"https://goo.gl/in0obv"]];
}

@end

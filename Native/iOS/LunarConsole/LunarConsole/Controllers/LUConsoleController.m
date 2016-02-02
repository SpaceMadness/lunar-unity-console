//
//  LUConsoleController.m
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

#import <MessageUI/MessageUI.h>

#import "LUConsoleController.h"

#import "Lunar.h"

static LUConsoleControllerState * _sharedControllerState;

@interface LUConsoleController () <LunarConsoleDelegate, LUToggleButtonDelegate,
    UITableViewDataSource, UITableViewDelegate,
    UISearchBarDelegate,
    MFMessageComposeViewControllerDelegate,
    LUTableViewTouchDelegate,
    LUConsoleDetailsControllerDelegate,
    LUConsoleMenuControllerDelegate>
{
    LUConsole * _console;
}

@property (nonatomic, assign) IBOutlet UILabel * statusBarView;
@property (nonatomic, assign) IBOutlet UILabel * overflowWarningLabel;

@property (nonatomic, assign) IBOutlet LUTableView * tableView;
@property (nonatomic, assign) IBOutlet UISearchBar * filterBar;

@property (nonatomic, assign) IBOutlet LULogTypeButton * logButton;
@property (nonatomic, assign) IBOutlet LULogTypeButton * warningButton;
@property (nonatomic, assign) IBOutlet LULogTypeButton * errorButton;

@property (nonatomic, assign) IBOutlet LUToggleButton  * toggleCollapseButton;
@property (nonatomic, assign) IBOutlet LUToggleButton  * scrollLockButton;

@property (nonatomic, assign) IBOutlet NSLayoutConstraint * lastToolbarButtonTrailingConstraint;
@property (nonatomic, assign) IBOutlet NSLayoutConstraint * lastToolbarButtonTrailingConstraintCompact;
@property (nonatomic, assign) IBOutlet NSLayoutConstraint * overflowLabelHeightConstraint;

@property (nonatomic, assign) BOOL scrollLocked;

@end

@implementation LUConsoleController

+ (void)load
{
    if (!LU_IOS_MIN_VERSION_AVAILABLE)
    {
        return;
    }
    
    if ([self class] == [LUConsoleController class])
    {
        // force linker to add these classes for Interface Builder
        [LUTableView class];
        [LULogTypeButton class];
    }
}

+ (instancetype)controllerWithConsole:(LUConsole *)console
{
    return LU_AUTORELEASE([[[self class] alloc] initWithConsole:console]);
}

- (instancetype)initWithConsole:(LUConsole *)console
{
    self = [super initWithNibName:NSStringFromClass([self class]) bundle:nil];
    if (self)
    {
        _console = LU_RETAIN(console);
    }
    return self;
}

- (void)dealloc
{
    LUAssert(_console.delegate == self);
    _console.delegate       = nil;
    _tableView.delegate     = nil;
    _tableView.dataSource   = nil;
    _filterBar.delegate     = nil;
    _logButton.delegate     = nil;
    _warningButton.delegate = nil;
    _errorButton.delegate   = nil;

    LU_RELEASE(_version);
    LU_RELEASE(_console);
    LU_SUPER_DEALLOC
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    _console.delegate = self;
    
    // collapse/expand button
    _toggleCollapseButton.on = _console.isCollapsed;
    _toggleCollapseButton.delegate = self;
    
    // scroll lock
    _scrollLocked = [self controllerState].scrollLocked;
    _scrollLockButton.on = _scrollLocked;
    _scrollLockButton.delegate = self;
    
    LUTheme *theme = [self theme];
    
    // title
    self.title = @"Logs";
    
    // background
    self.view.opaque = YES;
    self.view.backgroundColor = theme.tableColor;
    
    // table view
    _tableView.touchDelegate = self;
    _tableView.backgroundColor = theme.tableColor;
    
    // "status bar" view
    UITapGestureRecognizer *statusBarTapGestureRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self
                                                                             action:@selector(onStatusBarTap:)];
    [_statusBarView addGestureRecognizer:statusBarTapGestureRecognizer];
    LU_RELEASE(statusBarTapGestureRecognizer);
    
    _statusBarView.text = [NSString stringWithFormat:@"Lunar Console v%@", _version ? _version : @"?.?.?"];
    
    // log type buttons
    _logButton.on = ![_console.entries isFilterLogTypeEnabled:LUConsoleLogTypeLog];
    _logButton.delegate = self;
    
    _warningButton.on = ![_console.entries isFilterLogTypeEnabled:LUConsoleLogTypeWarning];
    _warningButton.delegate = self;
    
    _errorButton.on = ![_console.entries isFilterLogTypeEnabled:LUConsoleLogTypeError];
    _errorButton.delegate = self;
    
    // filter text
    _filterBar.text = _console.entries.filterText;
    
    // log entries count
    [self updateEntriesCount];
    
    // overflow warning
    dispatch_async(dispatch_get_main_queue(), ^{
        [self updateOverflowWarning]; // give the table a chance to layout
    });
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self.navigationController setNavigationBarHidden:YES animated:animated];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:animated];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    
    // TODO: clean up cells
}

- (BOOL)prefersStatusBarHidden
{
    return YES;
}

#pragma mark -
#pragma mark Filtering

- (void)filterByText:(NSString *)text
{
    BOOL shouldReload = [_console.entries setFilterByText:text];
    if (shouldReload)
    {
        [self reloadData];
    }
}

- (void)setFilterByLogTypeMask:(LUConsoleLogTypeMask)logTypeMask disabled:(BOOL)disabled
{
    BOOL shouldReload = [_console.entries setFilterByLogTypeMask:logTypeMask disabled:disabled];
    if (shouldReload)
    {
        [self reloadData];
    }
}

#pragma mark -
#pragma mark Collapsing

- (void)setCollapsed:(BOOL)collapsed
{
    _console.collapsed = !_console.isCollapsed;
    [self reloadData];
}

#pragma mark -
#pragma mark Actions

- (IBAction)onClose:(id)sender
{
    if ([_delegate respondsToSelector:@selector(consoleControllerDidClose:)])
    {
        [_delegate consoleControllerDidClose:self];
    }
}

- (IBAction)onClear:(id)sender
{
    // clear entries
    [_console clear];
    
    // update entries count
    [self updateEntriesCount];
}

- (IBAction)onCopy:(id)sender
{
    NSString *text = [_console getText];
    [self copyTextToClipboard:text];
}

- (IBAction)onEmail:(id)sender
{
    if (![MFMessageComposeViewController canSendText])
    {
        LUDisplayAlertView(@"Lunar Mobile Console", @"Log email cannot be sent");
        return;
    }
    
    NSString *bundleName = [[NSBundle mainBundle].infoDictionary objectForKey:@"CFBundleName"];
    NSString *text = [_console getText];
    
    MFMessageComposeViewController* controller = [[MFMessageComposeViewController alloc] init];
    [controller setMessageComposeDelegate: self];
    [controller setSubject:[NSString stringWithFormat:@"%@ console log", bundleName]];
    [controller setBody:text];
    if (controller)
    {
        [self presentViewController:controller animated:YES completion:nil];
    }
    LU_RELEASE(controller);
}

- (IBAction)onStatusBarTap:(UITapGestureRecognizer *)recognizer
{
    _scrollLockButton.on = NO;
    [self scrollToTopAnimated:YES];
}

- (IBAction)onMoreButton:(id)sender
{
    LUConsoleMenuController *controller = [LUConsoleMenuController new];
    
    // toggle collapse button
    if (_console.isCollapsed)
    {
        [controller addButtonTitle:@"Expand" target:self action:@selector(onExpandButton:)];
    }
    else
    {
        [controller addButtonTitle:@"Collapse" target:self action:@selector(onCollapseButton:)];
    }
    
    [controller setDelegate:self];
    
    // add as child view controller
    [self addChildOverlayController:controller animated:NO];
    
    LU_RELEASE(controller);
}

#pragma mark -
#pragma mark LunarConsoleDelegate

- (void)lunarConsole:(LUConsole *)console didAddEntryAtIndex:(NSInteger)index trimmedCount:(NSUInteger)trimmedCount
{
    if (trimmedCount > 0)
    {
        // show warning
        [self showOverflowCount:console.trimmedCount];
        
        // update cells
        [_tableView beginUpdates];
        
        [self removeCellsCount:trimmedCount];
        
        if (index != -1)
        {
            [self insertCellAt:index];
        }
        
        [_tableView endUpdates];
    }
    else if (index != -1)
    {
        [self insertCellAt:index];
    }
    
    // update entries count
    [self updateEntriesCount];
}

- (void)lunarConsole:(LUConsole *)console didUpdateEntryAtIndex:(NSInteger)index trimmedCount:(NSUInteger)trimmedCount
{
    if (trimmedCount > 0)
    {
        // show warning
        [self showOverflowCount:console.trimmedCount];
        
        // update cells
        [_tableView beginUpdates];
        
        [self removeCellsCount:trimmedCount];
        
        if (index != -1)
        {
            [self reloadCellAt:index];
        }
        
        [_tableView endUpdates];
    }
    else if (index != -1)
    {
        [self reloadCellAt:index];
    }
    
    // update entries count
    [self updateEntriesCount];
}

- (void)lunarConsoleDidClearEntries:(LUConsole *)console
{
    [self reloadData];
    [self updateOverflowWarning];
}

#pragma mark -
#pragma mark LUToggleButtonDelegate

- (void)toggleButtonStateChanged:(LUToggleButton *)button
{
    if (button == _scrollLockButton)
    {
        self.scrollLocked = button.isOn;
    }
    else if (button == _toggleCollapseButton)
    {
        [self setCollapsed:button.isOn];
    }
    else
    {
        LUConsoleLogTypeMask mask = 0;
        if (button == _logButton)
        {
            mask |= LU_CONSOLE_LOG_TYPE_MASK(LUConsoleLogTypeLog);
        }
        else if (button == _warningButton)
        {
            mask |= LU_CONSOLE_LOG_TYPE_MASK(LUConsoleLogTypeWarning);
        }
        else if (button == _errorButton)
        {
            mask |= LU_CONSOLE_LOG_TYPE_MASK(LUConsoleLogTypeException) |
                    LU_CONSOLE_LOG_TYPE_MASK(LUConsoleLogTypeError) |
                    LU_CONSOLE_LOG_TYPE_MASK(LUConsoleLogTypeAssert);
        }
        
        [self setFilterByLogTypeMask:mask disabled:button.isOn];
    }
}

#pragma mark -
#pragma mark Scrolling

- (void)scrollToBottomAnimated:(BOOL)animated
{
    NSIndexPath *path = [NSIndexPath indexPathForRow:_console.entriesCount-1 inSection:0];
    [_tableView scrollToRowAtIndexPath:path atScrollPosition:UITableViewScrollPositionBottom animated:animated];
}

- (void)scrollToTopAnimated:(BOOL)animated
{
    if (_console.entriesCount > 0)
    {
        NSIndexPath *path = [NSIndexPath indexPathForRow:0 inSection:0];
        [_tableView scrollToRowAtIndexPath:path atScrollPosition:UITableViewScrollPositionBottom animated:animated];
    }
}

#pragma mark -
#pragma mark UITableViewDataSource

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return _console.entriesCount;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    LUConsoleEntry *entry = [self entryForRowAtIndexPath:indexPath];
    return [entry tableView:tableView cellAtIndex:indexPath.row];
}

#pragma mark -
#pragma mark UITableViewDelegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    LUConsoleEntry *entry = [self entryForRowAtIndexPath:indexPath];
    
    LUConsoleDetailsController *controller = [[LUConsoleDetailsController alloc] initWithEntry:entry];
    controller.delegate = self;
    
    // add as child view controller
    [self addChildOverlayController:controller animated:YES];
    
    LU_RELEASE(controller);
    
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    LUConsoleEntry *entry = [self entryForRowAtIndexPath:indexPath];
    return [entry cellSizeForTableView:tableView].height;
}

- (BOOL)tableView:(UITableView*)tableView shouldShowMenuForRowAtIndexPath:(NSIndexPath*)indexPath
{
    return YES;
}

- (BOOL)tableView:(UITableView *)tableView canPerformAction:(SEL)action forRowAtIndexPath:(NSIndexPath *)indexPath withSender:(id)sender
{
    return action == @selector(copy:);
}

- (void)tableView:(UITableView *)tableView performAction:(SEL)action forRowAtIndexPath:(NSIndexPath *)indexPath withSender:(id)sender
{
    LUConsoleEntry *entry = [self entryForRowAtIndexPath:indexPath];
    [self copyTextToClipboard:entry.message];
}

#pragma mark -
#pragma mark UISearchBarDelegate

- (BOOL)searchBarShouldBeginEditing:(UISearchBar *)searchBar
{
    [searchBar setShowsCancelButton:YES animated:YES];
    
    if (self.traitCollection.horizontalSizeClass == UIUserInterfaceSizeClassCompact)
    {
        NSLayoutConstraint *constraint = [self lastToolbarButtonConstraint];
        CGFloat offset = CGRectGetWidth(self.view.bounds) - CGRectGetWidth(self.filterBar.bounds);
        constraint.constant = -offset;
        
        [UIView animateWithDuration:0.4 animations:^{
            [self.view layoutIfNeeded];
        }];
    }
    
    return YES;
}

- (BOOL)searchBarShouldEndEditing:(UISearchBar *)searchBar
{
    [searchBar setShowsCancelButton:NO animated:YES];
    
    if (self.traitCollection.horizontalSizeClass == UIUserInterfaceSizeClassCompact)
    {
        NSLayoutConstraint *constraint = [self lastToolbarButtonConstraint];
        constraint.constant = 0;
        
        [UIView animateWithDuration:0.4 animations:^{
            [self.view layoutIfNeeded];
        }];
    }
    
    return YES;
}

- (void)searchBarCancelButtonClicked:(UISearchBar *)searchBar
{
    [searchBar setShowsCancelButton:NO animated:YES];
    [searchBar resignFirstResponder];
}

- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText
{
    [self filterByText:searchText];

    if (searchText.length == 0)
    {
        dispatch_async(dispatch_get_main_queue(), ^{
            [searchBar resignFirstResponder];
        });
    }
}

- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar
{
    [searchBar resignFirstResponder];
}

- (NSLayoutConstraint *)lastToolbarButtonConstraint
{
    return self.lastToolbarButtonTrailingConstraintCompact != nil ?
        self.lastToolbarButtonTrailingConstraintCompact : self.lastToolbarButtonTrailingConstraint;
}

#pragma mark -
#pragma mark MFMessageComposeViewControllerDelegate

- (void)messageComposeViewController:(MFMessageComposeViewController *)controller
                 didFinishWithResult:(MessageComposeResult)result
{
    if (result != MessageComposeResultSent)
    {
        LUDisplayAlertView(@"Lunar Mobile Console", @"Log was not sent");
    }
    
    [controller dismissViewControllerAnimated:YES completion:nil];
}

#pragma mark -
#pragma mark LUTableViewTouchDelegate

- (void)tableView:(LUTableView *)tableView touchesBegan:(NSSet*)touches withEvent:(UIEvent *)event
{
    _scrollLockButton.on = NO;
}

#pragma mark -
#pragma mark LUConsoleDetailsControllerDelegate

- (void)detailsControllerDidClose:(LUConsoleDetailsController *)controller
{
    [self removeChildOverlayController:controller animated:YES];
}

#pragma mark -
#pragma mark LUConsoleMenuControllerDelegate

- (void)menuControllerDidRequestClose:(LUConsoleMenuController *)controller
{
    [self removeChildOverlayController:controller animated:NO];
}

#pragma mark -
#pragma mark UIScrollViewDelegate

- (void)scrollViewWillBeginDragging:(UIScrollView *)scrollView
{
    _scrollLockButton.on = NO;
}

#pragma mark -
#pragma mark Actions

- (void)onCollapseButton:(id)sender
{
    [self setCollapsed:YES];
}

- (void)onExpandButton:(id)sender
{
    [self setCollapsed:NO];
}

#pragma mark -
#pragma mark Overflow

- (void)updateOverflowWarning
{
    NSUInteger trimmedCount = _console.trimmedCount;
    if (trimmedCount > 0)
    {
        [self showOverflowCount:trimmedCount];
    }
    else
    {
        [self hideOverflowCount];
    }
}

- (void)showOverflowCount:(NSUInteger)count
{
    if (_overflowLabelHeightConstraint.constant == 0)
    {
        _overflowLabelHeightConstraint.constant = 20;
        [self.view layoutIfNeeded];
    }
    
    NSString *text = count > 999 ? @"Too much output: 999+ items trimmed" :
        [NSString stringWithFormat:@"Too much output: %d item(s) trimmed", (int)count];
    
    _overflowWarningLabel.text = text;
}

- (void)hideOverflowCount
{
    if (_overflowLabelHeightConstraint.constant > 0)
    {
        _overflowLabelHeightConstraint.constant = 0;
        [self.view layoutIfNeeded];
    }
}
         
#pragma mark -
#pragma mark Cell manipulations

- (void)removeCellsCount:(NSInteger)count
{
    if (count == 1)
    {
        NSArray *deleteIndices = [[NSArray alloc] initWithObjects:[NSIndexPath indexPathForRow:0 inSection:0], nil];
        [_tableView deleteRowsAtIndexPaths:deleteIndices withRowAnimation:UITableViewRowAnimationNone];
        LU_RELEASE(deleteIndices);
    }
    else if (count > 1)
    {
        NSMutableArray *deleteIndices = [[NSMutableArray alloc] initWithCapacity:count];
        for (NSInteger rowIndex = 0; rowIndex < count; ++rowIndex)
        {
            [deleteIndices addObject:[NSIndexPath indexPathForRow:rowIndex inSection:0]];
        }
        [_tableView deleteRowsAtIndexPaths:deleteIndices withRowAnimation:UITableViewRowAnimationNone];
        LU_RELEASE(deleteIndices);
    }
}

- (void)insertCellAt:(NSInteger)index
{
    LUAssert(index >= 0 && index < _console.entriesCount);
    
    NSArray *indices = [[NSArray alloc] initWithObjects:[NSIndexPath indexPathForRow:index inSection:0], nil];
    [_tableView insertRowsAtIndexPaths:indices withRowAnimation:UITableViewRowAnimationNone];
    LU_RELEASE(indices);

    // scroll to end
    if (_scrollLocked)
    {
        [self scrollToBottomAnimated:NO];
    }
}

- (void)reloadCellAt:(NSInteger)index
{
    LUAssert(index >= 0 && index < _console.entriesCount);
    
    NSArray *indices = [[NSArray alloc] initWithObjects:[NSIndexPath indexPathForRow:index inSection:0], nil];
    [_tableView reloadRowsAtIndexPaths:indices withRowAnimation:UITableViewRowAnimationNone];
    LU_RELEASE(indices);
}

- (void)updateEntriesCount
{
    LUConsoleEntryList *entries = _console.entries;
    _logButton.count = entries.logCount;
    _warningButton.count = entries.warningCount;
    _errorButton.count = entries.errorCount;
}

#pragma mark -
#pragma mark Helpers

- (LUConsoleEntry *)entryForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return [_console entryAtIndex:indexPath.row];
}

- (LUConsoleEntry *)entryForRowAtIndex:(NSUInteger)index
{
    return [_console entryAtIndex:index];
}

- (void)reloadData
{
    [_tableView reloadData];
}

- (void)copyTextToClipboard:(NSString *)text
{
    UIPasteboard *pasteboard = [UIPasteboard generalPasteboard];
    [pasteboard setString:text];
}

#pragma mark -
#pragma mark Controllers

- (void)addChildOverlayController:(UIViewController *)controller animated:(BOOL)animated
{
    // add as child view controller
    [self addChildViewController:controller];
    controller.view.frame = self.view.bounds;
    [self.view addSubview:controller.view];
    [controller didMoveToParentViewController:self];
    
    // animate
    if (animated) {
        controller.view.alpha = 0;
        [UIView animateWithDuration:0.4 animations:^{
            controller.view.alpha = 1;
        }];
    }
}

- (void)removeChildOverlayController:(UIViewController *)controller animated:(BOOL)animated
{
    if (animated)
    {
        [UIView animateWithDuration:0.4 animations:^{
            controller.view.alpha = 0;
        } completion:^(BOOL finished) {
            [controller willMoveToParentViewController:self];
            [controller.view removeFromSuperview];
            [controller removeFromParentViewController];
        }];
    }
    else
    {
        [controller willMoveToParentViewController:self];
        [controller.view removeFromSuperview];
        [controller removeFromParentViewController];
    }
}

#pragma mark -
#pragma mark Properties

- (LUTheme *)theme
{
    return [LUTheme mainTheme];
}

- (LUConsoleControllerState *)controllerState
{
    return [LUConsoleControllerState sharedControllerState];
}

- (void)setScrollLocked:(BOOL)scrollLocked
{
    _scrollLocked = scrollLocked;
    [self controllerState].scrollLocked = _scrollLocked;
}

@end

@implementation LUConsoleControllerState

- (instancetype)init
{
    self = [super init];
    if (self)
    {
        _scrollLocked = YES;
    }
    return self;
}

+ (instancetype)sharedControllerState
{
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        _sharedControllerState = [[self alloc] init];
    });
    
    return _sharedControllerState;
}

@end

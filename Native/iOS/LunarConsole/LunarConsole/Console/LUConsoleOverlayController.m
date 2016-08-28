//
//  LUConsoleOverlayController.m
//  LunarConsole
//
//  Created by Alex Lementuev on 8/20/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "Lunar.h"

#import "LUConsoleOverlayController.h"

@interface LUConsoleOverlayController () <UITableViewDataSource, UITableViewDelegate, LunarConsoleDelegate>
{
    NSMutableArray                      * _entries;
    LUConsole                           * _console;
    LUConsoleOverlayControllerSettings  * _settings;
    BOOL                                  _dispatchScheduled;
    BOOL                                  _dispatchCancelled;
}

@property (nonatomic, assign) IBOutlet UITableView * tableView;

@end

@implementation LUConsoleOverlayController

+ (instancetype)controllerWithConsole:(LUConsole *)console settings:(LUConsoleOverlayControllerSettings *)settings
{
    return LU_AUTORELEASE([[[self class] alloc] initWithConsole:console settings:settings]);
}

- (instancetype)initWithConsole:(LUConsole *)console settings:(LUConsoleOverlayControllerSettings *)settings
{
    self = [super initWithNibName:NSStringFromClass([self class]) bundle:nil];
    if (self)
    {
        _console = LU_RETAIN(console);
        _console.delegate = self;
        
        _settings = LU_RETAIN(settings);
        
        _entries = [[NSMutableArray alloc] initWithCapacity:_settings.maxVisibleRows];
    }
    return self;
}

- (void)dealloc
{
    if (_console.delegate == self)
    {
        _console.delegate = nil;
    }
    
    _tableView.delegate   = nil;
    _tableView.dataSource = nil;
    
    LU_RELEASE(_console);
    LU_RELEASE(_settings);
    LU_SUPER_DEALLOC;
}

#pragma mark -
#pragma mark Life cycle

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    _dispatchCancelled = NO;
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    _dispatchCancelled = YES;
}

#pragma mark -
#pragma mark UITableViewDataSource

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return _entries.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    LUConsoleLogEntry *entry = [self entryForRowAtIndexPath:indexPath];
    return [entry tableView:tableView cellAtIndex:indexPath.row];
}

#pragma mark -
#pragma mark UITableViewDelegate

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    LUConsoleLogEntry *entry = [self entryForRowAtIndexPath:indexPath];
    return [entry cellSizeForTableView:tableView].height;
}

#pragma mark -
#pragma mark LunarConsoleDelegate

- (void)lunarConsole:(LUConsole *)console didAddEntryAtIndex:(NSInteger)index trimmedCount:(NSUInteger)trimmedCount
{
    LUConsoleOverlayLogEntry *entry = [LUConsoleOverlayLogEntry entryWithEntry:[console entryAtIndex:index]];
    
    // remove row after the delay
    [self scheduleDispatchAfterDelay:_settings.rowDisplayTime];
    
    [UIView performWithoutAnimation:^{
        if (_entries.count < _settings.maxVisibleRows)
        {
            [_entries addObject:entry];
            [_tableView insertRowsAtIndexPaths:@[[NSIndexPath indexPathForRow:_entries.count - 1 inSection:0]]
                              withRowAnimation:UITableViewRowAnimationNone];
        }
        else
        {
                [_tableView beginUpdates];
                
                [self removeFirstRow];
                
                [_entries addObject:entry];
                [_tableView insertRowsAtIndexPaths:@[[NSIndexPath indexPathForRow:_entries.count - 1 inSection:0]]
                                  withRowAnimation:UITableViewRowAnimationNone];
                
                [_tableView endUpdates];
        }
    }];
}

- (void)lunarConsole:(LUConsole *)console didUpdateEntryAtIndex:(NSInteger)index trimmedCount:(NSUInteger)trimmedCount
{
    [self lunarConsole:console didAddEntryAtIndex:index trimmedCount:trimmedCount];
}

#pragma mark -
#pragma mark Rows

- (void)removeFirstRow
{
    if (_entries.count > 0)
    {
        [_entries removeObjectAtIndex:0];
        [_tableView deleteRowsAtIndexPaths:@[[NSIndexPath indexPathForRow:0 inSection:0]]
                          withRowAnimation:UITableViewRowAnimationNone];
    }
}

#pragma mark -
#pragma mark Dispatch

- (void)scheduleDispatchAfterDelay:(NSTimeInterval)delay
{
    if (!_dispatchScheduled)
    {
        _dispatchScheduled = YES;
        
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(delay * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            if (!_dispatchCancelled)
            {
                if (_entries.count > 0)
                {
                    [UIView performWithoutAnimation:^{
                        [self removeFirstRow];
                    }];
                }
                
                _dispatchScheduled = NO;
                
                if (_entries.count > 0)
                {
                    [self scheduleDispatchAfterDelay:delay];
                }
            }
        });
    }
}

#pragma mark -
#pragma mark Helpers

- (LUConsoleLogEntry *)entryForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return [_entries objectAtIndex:indexPath.row];
}

- (LUConsoleLogEntry *)entryForRowAtIndex:(NSUInteger)index
{
    return [_entries objectAtIndex:index];
}

- (void)reloadData
{
    [_tableView reloadData];
}

@end

@implementation LUConsoleOverlayControllerSettings

+ (instancetype)settings
{
    return LU_AUTORELEASE([[[self class] alloc] init]);
}

- (instancetype)init
{
    self = [super init];
    if (self)
    {
        _maxVisibleRows = 3;
        _rowDisplayTime = 1.0;
    }
    return self;
}

@end

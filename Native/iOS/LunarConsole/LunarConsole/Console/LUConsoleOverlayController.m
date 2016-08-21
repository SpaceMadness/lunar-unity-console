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
    CGFloat                               _totalHeight;
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
    LUConsoleLogEntry *entry = [console entryAtIndex:index];
    if (_entries.count < _settings.maxVisibleRows)
    {
        [_entries addObject:[LUConsoleOverlayLogEntry entryWithEntry:entry]];
        [_tableView insertRowsAtIndexPaths:@[[NSIndexPath indexPathForRow:_entries.count - 1 inSection:0]]
                          withRowAnimation:UITableViewRowAnimationNone];
    }
    else
    {
        [_tableView beginUpdates];
        
        [_entries removeObjectAtIndex:0];
        [_tableView deleteRowsAtIndexPaths:@[[NSIndexPath indexPathForRow:0 inSection:0]]
                          withRowAnimation:UITableViewRowAnimationNone];
        
        [_entries addObject:[LUConsoleOverlayLogEntry entryWithEntry:entry]];
        [_tableView insertRowsAtIndexPaths:@[[NSIndexPath indexPathForRow:_entries.count - 1 inSection:0]]
                          withRowAnimation:UITableViewRowAnimationNone];
        
        [_tableView endUpdates];
    }
}

- (void)lunarConsole:(LUConsole *)console didUpdateEntryAtIndex:(NSInteger)index trimmedCount:(NSUInteger)trimmedCount
{
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

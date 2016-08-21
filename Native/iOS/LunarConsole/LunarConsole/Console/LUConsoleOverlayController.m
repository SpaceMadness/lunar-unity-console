//
//  LUConsoleOverlayController.m
//  LunarConsole
//
//  Created by Alex Lementuev on 8/20/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "Lunar.h"

#import "LUConsoleOverlayController.h"

@interface LUConsoleOverlayController () <UITableViewDataSource, UITableViewDelegate>
{
    NSMutableArray * _entries;
    LUConsole * _console;
}

@property (nonatomic, assign) IBOutlet UITableView * tableView;

@end

@implementation LUConsoleOverlayController

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
        _entries = [NSMutableArray new];
        for (int i = 0; i < _console.entriesCount; ++i)
        {
            [_entries addObject:[LUConsoleOverlayLogEntry entryWithEntry:[_console entryAtIndex:i]]];
        }
    }
    return self;
}

- (void)dealloc
{
    _tableView.delegate   = nil;
    _tableView.dataSource = nil;
    
    LU_RELEASE(_console);
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

//
//  LunarConsole.m
//  LunarConsole
//
//  Created by Alex Lementuev on 8/3/15.
//  Copyright (c) 2015 Space Madness. All rights reserved.
//

#import "LUConsole.h"

#import "Lunar.h"

@interface LUConsole ()
{
    LUConsoleEntryList * _entries;
}

@end

@implementation LUConsole

- (instancetype)initWithCapacity:(NSUInteger)capacity
{
    self = [super init];
    if (self)
    {
        _entries = [[LUConsoleEntryList alloc] initWithCapacity:capacity];
    }
    return self;
}

- (void)dealloc
{
    LU_RELEASE(_entries);
    LU_SUPER_DEALLOC
}

#pragma mark -
#pragma mark Entries

- (LUConsoleEntry *)entryAtIndex:(NSUInteger)index
{
    return [_entries entryAtIndex:index];
}

- (void)logMessage:(NSString *)message stackTrace:(NSString *)stackTrace type:(LUConsoleLogType)type
{
    LUConsoleEntry *entry = [[LUConsoleEntry alloc] initWithType:type message:message]; // TODO: use stack trace
    BOOL filtered = [_entries addEntry:entry];
    [_delegate lunarConsole:self didAddEntry:entry filtered:filtered];
    LU_RELEASE(entry);
}

- (void)clear
{
    [_entries clear];
    if ([_delegate respondsToSelector:@selector(lunarConsoleDidClearEntries:)])
    {
        [_delegate lunarConsoleDidClearEntries:self];
    }
}

- (NSString *)getText
{
    return [_entries getText];
}

#pragma mark -
#pragma mark Properties

- (NSUInteger)entriesCount
{
    return _entries.count;
}

- (NSUInteger)overflowAmount
{
    return _entries.overflowAmount;
}

- (BOOL)isOverfloating
{
    return _entries.isOverfloating;
}

@end

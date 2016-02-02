//
//  LUConsoleEntryLookupTable.m
//  LunarConsole
//
//  Created by Alex Lementuev on 1/25/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "LUConsoleEntryLookupTable.h"

#import "Lunar.h"

@interface LUConsoleEntryLookupTable ()
{
    NSMutableDictionary * _table; // TODO: replace with a search trie
}

@end

@implementation LUConsoleEntryLookupTable

- (instancetype)init
{
    self = [super init];
    if (self)
    {
        _table = [[NSMutableDictionary alloc] init];
    }
    return self;
}

- (void)dealloc
{
    LU_RELEASE(_table);
    LU_SUPER_DEALLOC
}

- (LUConsoleCollapsedEntry *)addEntry:(LUConsoleEntry *)entry
{
    LUAssert(entry);
    
    NSString *message = entry.message;
    if (message)
    {
        LUConsoleCollapsedEntry *collapsedEntry = [_table objectForKey:message];
        if (collapsedEntry == nil)
        {
            collapsedEntry = [LUConsoleCollapsedEntry entryWithEntry:entry];
            [_table setObject:collapsedEntry forKey:message];
        }
        else
        {
            [collapsedEntry increaseCount];
        }
        
        return collapsedEntry;
    }
    
    LUAssert(message);
    return nil;
}

- (void)removeEntry:(LUConsoleCollapsedEntry *)entry
{
    LUAssert(entry);
    
    NSString *message = entry.message;
    if (message)
    {
        [_table removeObjectForKey:message];
    }
}

- (void)clear
{
    [_table removeAllObjects];
}

@end

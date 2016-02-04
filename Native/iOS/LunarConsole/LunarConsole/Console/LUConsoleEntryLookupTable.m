//
//  LUConsoleEntryLookupTable.m
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

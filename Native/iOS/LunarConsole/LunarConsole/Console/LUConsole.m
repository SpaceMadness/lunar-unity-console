//
//  LUConsole.m
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2015 Alex Lementuev, SpaceMadness.
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

#import "LUConsole.h"

#import "Lunar.h"

@interface LUConsole ()
{
    LUConsoleEntryList * _entries;
}

@end

@implementation LUConsole

- (instancetype)initWithCapacity:(NSUInteger)capacity trimCount:(NSUInteger)trimCount
{
    self = [super init];
    if (self)
    {
        _entries = [[LUConsoleEntryList alloc] initWithCapacity:capacity trimCount:trimCount];
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
    NSUInteger oldTrimmedCount = _entries.trimmedCount; // trimmed count before we added a new item
    
    LUConsoleEntry *entry = [[LUConsoleEntry alloc] initWithType:type message:message stackTrace:stackTrace];
    BOOL filtered = [_entries addEntry:entry];

    NSInteger trimmed = _entries.trimmedCount - oldTrimmedCount;
    if (trimmed > 0) // more items are trimmed
    {
        if ([_delegate respondsToSelector:@selector(lunarConsole:didRemoveRange:)])
        {
            [_delegate lunarConsole:self didRemoveRange:NSMakeRange(0, trimmed)];
        }
    }
    
    [_delegate lunarConsole:self didAddEntry:entry filtered:filtered trimmedCount:trimmed];
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

- (NSUInteger)trimmedCount
{
    return _entries.trimmedCount;
}

- (BOOL)isTrimmed
{
    return _entries.isTrimmed;
}

@end

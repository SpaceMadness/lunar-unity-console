//
//  LUConsoleEntryList.m
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

#import "LUConsoleEntryList.h"

#import "Lunar.h"

@interface LUConsoleEntryList ()
{
    LULimitSizeList * _entries;
    LULimitSizeList * _filteredEntries;
    LULimitSizeList * _currentEntries;
    LUConsoleLogType  _logDisabledTypesMask;
}

@end

@implementation LUConsoleEntryList

+ (instancetype)listWithCapacity:(NSUInteger)capacity trimCount:(NSUInteger)trimCount
{
    return LU_AUTORELEASE([[[self class] alloc] initWithCapacity:capacity trimCount:trimCount]);
}

- (instancetype)initWithCapacity:(NSUInteger)capacity trimCount:(NSUInteger)trimCount
{
    self = [super init];
    if (self)
    {
        _entries = [[LULimitSizeList alloc] initWithCapacity:capacity trimCount:trimCount];
        _currentEntries = _entries;
        _logDisabledTypesMask = 0;
    }
    return self;
}

- (void)dealloc
{
    LU_RELEASE(_entries);
    LU_RELEASE(_filteredEntries);
    LU_RELEASE(_filterText);
    LU_SUPER_DEALLOC
}

#pragma mark -
#pragma mark Entries

- (BOOL)addEntry:(LUConsoleEntry *)entry
{
    LUAssert(entry);
    if (entry != nil)
    {
        // add entry
        [_entries addObject:entry];
        
        // count types
        LUConsoleLogType entryType = entry.type;
        if (entryType == LUConsoleLogTypeLog)
        {
            ++_logCount;
        }
        else if (entryType == LUConsoleLogTypeWarning)
        {
            ++_warningCount;
        }
        else if (LU_IS_CONSOLE_LOG_TYPE_ERROR(entryType))
        {
            ++_errorCount;
        }
        
        // filter
        if (self.isFiltering)
        {
            if ([self filterEntry:entry])
            {
                [_filteredEntries addObject:entry];
                return YES;
            }
            
            return NO; // if item was rejected - we don't need to update table cells
        }
        
        return YES;
    }
    
    return NO;
}

- (LUConsoleEntry *)entryAtIndex:(NSUInteger)index
{
    LUAssert(index >= 0 && index < _currentEntries.count);
    return [_currentEntries objectAtIndex:index];
}

- (void)clear
{
    [_entries removeAllObjects];
    [_filteredEntries removeAllObjects];
    
    _logCount = 0;
    _warningCount = 0;
    _errorCount = 0;
}

#pragma mark -
#pragma mark Filtering

- (BOOL)setFilterByText:(NSString *)filterText
{
    if (_filterText != filterText) // filter text has changed
    {
        NSString *oldFilterText = LU_AUTORELEASE(LU_RETAIN(_filterText)); // manual reference counting rocks!
        
        LU_RELEASE(_filterText);
        _filterText = LU_RETAIN(filterText);
        
        if (filterText.length > oldFilterText.length && (oldFilterText.length == 0 || [filterText hasPrefix:oldFilterText])) // added more characters
        {
            return [self appendFilter];
        }
        
        return [self applyFilter];
    }
    
    return NO;
}

- (BOOL)setFilterByLogType:(LUConsoleLogType)logType disabled:(BOOL)disabled
{
    return [self setFilterByLogTypeMask:LU_CONSOLE_LOG_TYPE_MASK(logType) disabled:disabled];
}

- (BOOL)setFilterByLogTypeMask:(LUConsoleLogTypeMask)logTypeMask disabled:(BOOL)disabled
{
    LUConsoleLogType oldDisabledTypesMask = _logDisabledTypesMask;
    if (disabled)
    {
        _logDisabledTypesMask |= logTypeMask;
    }
    else
    {
        _logDisabledTypesMask &= ~logTypeMask;
    }
    
    if (oldDisabledTypesMask != _logDisabledTypesMask)
    {
        return disabled ? [self appendFilter] : [self applyFilter];
    }
    
    return NO;
}

- (BOOL)isFilterLogTypeEnabled:(LUConsoleLogType)type
{
    return (_logDisabledTypesMask & LU_CONSOLE_LOG_TYPE_MASK(type)) == 0;
}

- (BOOL)appendFilter
{
    if (self.isFiltering)
    {
        [self useFilteredFromEntries:_filteredEntries];
        return YES;
    }
    
    return [self applyFilter];
}

- (BOOL)applyFilter
{
    BOOL filtering = _filterText.length > 0 || [self hasLogTypeFilters]; // needs filtering?
    if (filtering)
    {
        [self useFilteredFromEntries:_entries];
        return YES;
    }
    
    return [self removeFilter];
}

- (BOOL)removeFilter
{
    if (self.isFiltering)
    {
        _currentEntries = _entries;
        
        LU_RELEASE(_filteredEntries);
        _filteredEntries = nil;
        
        return YES;
    }
    
    return NO;
}

- (void)useFilteredFromEntries:(LULimitSizeList *)entries
{
    LULimitSizeList *filteredEntries = [self filterEntries:entries];
    
    // use filtered items
    _currentEntries = filteredEntries;
    
    // store filtered items
    LU_RELEASE(_filteredEntries);
    _filteredEntries = LU_RETAIN(filteredEntries);
}

- (LULimitSizeList *)filterEntries:(LULimitSizeList *)entries
{
    LULimitSizeList *list = [LULimitSizeList listWithCapacity:entries.capacity      // same capacity
                                                    trimCount:entries.trimCount];   // and trim policy as original
    for (id entry in entries)
    {
        if ([self filterEntry:entry])
        {
            [list addObject:entry];
        }
    }
    
    return list;
}

- (BOOL)filterEntry:(LUConsoleEntry *)entry
{
    // filter by log type
    if (_logDisabledTypesMask & LU_CONSOLE_LOG_TYPE_MASK(entry.type))
    {
        return NO;
    }
    
    // filter by message
    return _filterText.length == 0 || [entry.message rangeOfString:_filterText options:NSCaseInsensitiveSearch].location != NSNotFound;
}

#pragma mark -
#pragma mark Text

- (NSString *)getText
{
    NSMutableString *text = [NSMutableString string];
    
    NSUInteger index = 0;
    NSUInteger count = _currentEntries.count;
    for (LUConsoleEntry *entry in _currentEntries)
    {
        [text appendString:entry.message];
        if (++index < count)
        {
            [text appendString:@"\n"];
        }
    }
    
    return text;
}

#pragma mark -
#pragma mark Helpers

- (BOOL)hasLogTypeFilters
{
    return _logDisabledTypesMask != 0;
}

#pragma mark -
#pragma mark Properties

- (NSUInteger)count
{
    return _currentEntries.count;
}

- (NSUInteger)totalCount
{
    return _currentEntries.totalCount;
}

- (BOOL)isFiltering
{
    return _filteredEntries != nil;
}

@end

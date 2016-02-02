//
//  LUConsoleEntry.m
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

#import "LUConsoleEntry.h"

#import "Lunar.h"

static NSArray * _cellSkinLookup;

@interface LUConsoleEntry ()
{
    CGFloat _cachedHeight;
}

@end

@implementation LUConsoleEntry

+ (void)load
{
    if (!LU_IOS_MIN_VERSION_AVAILABLE)
    {
        return;
    }
    
    if ([self class] == [LUConsoleEntry class])
    {
        LUTheme *theme = [LUTheme mainTheme];
        
        _cellSkinLookup = [[NSArray alloc] initWithObjects:
            theme.cellError,   // error
            theme.cellError,   // assert,
            theme.cellWarning, // warning,
            theme.cellLog,     // log,
            theme.cellError,   // exception
        nil];
        
        LUAssert(_cellSkinLookup.count == LU_CONSOLE_LOG_TYPE_COUNT);
    }
}

+ (instancetype)entryWithType:(LUConsoleLogType)type message:(NSString *)message stackTrace:(NSString *)stackTrace
{
    return LU_AUTORELEASE([[[self class] alloc] initWithType:type message:message stackTrace:stackTrace]);
}

- (instancetype)initWithType:(LUConsoleLogType)type message:(NSString *)message stackTrace:(NSString *)stackTrace
{
    self = [super init];
    if (self)
    {
        _type = type;
        _message = LU_RETAIN(message);
        _stackTrace = LU_RETAIN(stackTrace);
        _cachedHeight = -1;
    }
    return self;
}

- (void)dealloc
{
    LU_RELEASE(_message);
    LU_RELEASE(_stackTrace);
    LU_SUPER_DEALLOC
}

#pragma mark -
#pragma mark Equality

- (BOOL)isEqual:(id)object
{
    if ([object isKindOfClass:[self class]])
    {
        LUConsoleEntry *other = object;
        return other.type == _type && [other.message isEqualToString:_message];
    }
    
    return false;
}

#pragma mark -
#pragma mark Cells

- (UITableViewCell *)tableView:(UITableView *)tableView cellAtIndex:(NSUInteger)index
{
    CGSize cellSize = [self cellSizeForTableView:tableView];
    CGRect cellBounds = CGRectMake(0, 0, cellSize.width, cellSize.height);
    
    LUConsoleTableCell *cell = [tableView dequeueReusableCellWithIdentifier:@"log"];
    if (cell == nil)
    {
        cell = [LUConsoleTableCell cellWithFrame:cellBounds reuseIdentifier:@"log"];
    }
    else
    {
        [cell setSize:cellSize];
    }
    
    LUCellSkin *cellSkin = [self cellSkinForLogType:_type];
    
    cell.message = _message;
    cell.messageColor = cellSkin.textColor;
    cell.cellColor = index % 2 == 0 ? cellSkin.backgroundColorDark : cellSkin.backgroundColorLight;
    cell.icon = cellSkin.icon;
    
    return cell;
}

- (CGSize)cellSizeForTableView:(UITableView *)tableView
{
    CGFloat cellWidth = CGRectGetWidth(tableView.bounds);
    if (_cachedHeight < 0.0f)
    {
        _cachedHeight = [LUConsoleTableCell heightForCellWithText:_message width:cellWidth];
    }
    
    return CGSizeMake(cellWidth, _cachedHeight);
}

#pragma mark -
#pragma mark Helpers

- (LUCellSkin *)cellSkinForLogType:(LUConsoleLogType)type
{
    if (type >= 0 && type < _cellSkinLookup.count)
    {
        return _cellSkinLookup[type];
    }
    
    LUAssert(type >= 0 && type < _cellSkinLookup.count);
    return nil;
}

#pragma mark -
#pragma mark Properties

- (UIImage *)icon
{
    return [self cellSkinForLogType:_type].icon;
}

- (BOOL)hasStackTrace
{
    return _stackTrace.length > 0;
}

@end

@implementation LUConsoleCollapsedEntry

+ (instancetype)entryWithEntry:(LUConsoleEntry *)entry
{
    return LU_AUTORELEASE([[self alloc] initWithEntry:entry]);
}

- (instancetype)initWithEntry:(LUConsoleEntry *)entry
{
    self = [super initWithType:entry.type message:entry.message stackTrace:entry.stackTrace];
    if (self)
    {
        _count = 1;
        _index = -1;
    }
    return self;
}

#pragma mark -
#pragma mark Cells

- (UITableViewCell *)tableView:(UITableView *)tableView cellAtIndex:(NSUInteger)index
{
    CGSize cellSize = [self cellSizeForTableView:tableView];
    CGRect cellBounds = CGRectMake(0, 0, cellSize.width, cellSize.height);
    
    LUConsoleTableCollapsedCell *cell = [tableView dequeueReusableCellWithIdentifier:@"collapse"];
    if (cell == nil)
    {
        cell = [LUConsoleTableCollapsedCell cellWithFrame:cellBounds reuseIdentifier:@"collapse"];
    }
    
    LUCellSkin *cellSkin = [self cellSkinForLogType:self.type];
    
    cell.message = self.message;
    cell.messageColor = cellSkin.textColor;
    cell.cellColor = index % 2 == 0 ? cellSkin.backgroundColorDark : cellSkin.backgroundColorLight;
    cell.icon = cellSkin.icon;
    cell.collapsedCount = self.count;
    
    [cell setSize:cellSize];
    
    return cell;
}

#pragma mark -
#pragma mark Properties

- (void)increaseCount
{
    ++_count;
}

@end

//
//  LunarLogEntry.m
//  LunarConsole
//
//  Created by Alex Lementuev on 8/3/15.
//  Copyright (c) 2015 Space Madness. All rights reserved.
//

#import "LUConsoleEntry.h"

#import "Lunar.h"

static NSArray * _cellSkinLookup;

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

+ (instancetype)entryWithType:(LUConsoleLogType)type message:(NSString *)message
{
    return LU_AUTORELEASE([[[self class] alloc] initWithType:type message:message]);
}

- (instancetype)initWithType:(LUConsoleLogType)type message:(NSString *)message
{
    self = [super init];
    if (self)
    {
        _type = type;
        _message = LU_RETAIN(message);
    }
    return self;
}

- (void)dealloc
{
    LU_RELEASE(_message);
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
        [cell setSize:cellBounds.size];
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
    CGFloat cellHeight = [LUConsoleTableCell heightForCellWithText:_message width:cellWidth];
    
    return CGSizeMake(cellWidth, cellHeight);
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

@end

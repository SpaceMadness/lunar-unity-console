//
//  LUCVar.m
//  LunarConsole
//
//  Created by Alex Lementuev on 4/1/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "LUCVar.h"

#import "Lunar.h"

NSString * const LUCVarTypeNameBoolean = @"Boolean";
NSString * const LUCVarTypeNameInteger = @"Integer";
NSString * const LUCVarTypeNameFloat   = @"Float";
NSString * const LUCVarTypeNameString  = @"String";
NSString * const LUCVarTypeNameUnknown = @"Unknown";

@interface LUCVar ()
{
    Class _cellClass;
}

@end

@implementation LUCVar

+ (instancetype)variableWithId:(int)entryId name:(NSString *)name value:(NSString *)value type:(LUCVarType)type cellClass:(Class)cellClass
{
    return [[self alloc] initWithId:entryId name:name value:value type:type cellClass:cellClass];
}

- (instancetype)initWithId:(int)entryId name:(NSString *)name value:(NSString *)value type:(LUCVarType)type cellClass:(Class)cellClass
{
    self = [super initWithId:entryId name:name];
    if (self)
    {
        _value = value;
        _cellClass = cellClass;
        _type = type;
    }
    return self;
}

#pragma mark -
#pragma mark UITableView

- (UITableViewCell *)tableView:(UITableView *)tableView cellAtIndex:(NSUInteger)index
{
    NSString *identifier = NSStringFromClass(_cellClass);
    LUCVarTableViewCell *cell = (LUCVarTableViewCell *)[tableView dequeueReusableCellWithIdentifier:identifier];
    if (cell == nil)
    {
        cell = [[_cellClass alloc] initWithReuseIdentifier:identifier];
    }
    
    [cell setupVariable:self];
    
    return cell;
}

#pragma mark -
#pragma mark Lookup 

+ (LUCVarType)typeForName:(NSString *)type
{
    if ([type isEqualToString:LUCVarTypeNameBoolean]) return LUCVarTypeBoolean;
    if ([type isEqualToString:LUCVarTypeNameInteger]) return LUCVarTypeInteger;
    if ([type isEqualToString:LUCVarTypeNameFloat])   return LUCVarTypeFloat;
    if ([type isEqualToString:LUCVarTypeNameString])  return LUCVarTypeString;
    
    return LUCVarTypeUnknown;
}

+ (NSString *)typeNameForType:(LUCVarType)type
{
    switch (type)
    {
        case LUCVarTypeBoolean: return LUCVarTypeNameBoolean;
        case LUCVarTypeInteger: return LUCVarTypeNameInteger;
        case LUCVarTypeFloat:   return LUCVarTypeNameFloat;
        case LUCVarTypeString:  return LUCVarTypeNameString;
        case LUCVarTypeUnknown: return LUCVarTypeNameUnknown;
    }
}

@end

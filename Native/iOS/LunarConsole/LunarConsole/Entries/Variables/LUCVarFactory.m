//
//  LUCVarFactory.m
//  LunarConsole
//
//  Created by Alex Lementuev on 4/7/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "LUCVarFactory.h"

#import "Lunar.h"

#import "LUCVarBooleanTableViewCell.h"
#import "LUCVarFloatTableViewCell.h"
#import "LUCVarIntegerTableViewCell.h"
#import "LUCVarStringTableViewCell.h"

@implementation LUCVarFactory

+ (LUCVar *)variableWithId:(int)entryId name:(NSString *)name value:(NSString *)value type:(LUCVarType)type
{
    Class cellClass = [self tableCellClassForVariableType:type];
    if (cellClass == NULL)
    {
        NSLog(@"Can't resolve cell class for variable type: %ld", (long) type);
        return nil;
    }
    
    return [LUCVar variableWithId:entryId name:name value:value type:type cellClass:cellClass];
}

+ (Class)tableCellClassForVariableType:(LUCVarType)type
{
    switch (type)
    {
        case LUCVarTypeBoolean: return [LUCVarBooleanTableViewCell class];
        case LUCVarTypeInteger: return [LUCVarIntegerTableViewCell class];
        case LUCVarTypeFloat:   return [LUCVarFloatTableViewCell class];
        case LUCVarTypeString:  return [LUCVarStringTableViewCell class];
        case LUCVarTypeUnknown: return NULL;
    }
}

@end
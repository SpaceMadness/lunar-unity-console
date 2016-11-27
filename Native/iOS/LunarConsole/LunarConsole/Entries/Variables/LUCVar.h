//
//  LUCVar.h
//  LunarConsole
//
//  Created by Alex Lementuev on 4/1/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "LUEntry.h"

typedef enum : NSUInteger {
    LUCVarTypeUnknown,
    LUCVarTypeBoolean,
    LUCVarTypeInteger,
    LUCVarTypeFloat,
    LUCVarTypeString
} LUCVarType;

extern NSString * const LUCVarTypeNameBoolean;
extern NSString * const LUCVarTypeNameInteger;
extern NSString * const LUCVarTypeNameFloat;
extern NSString * const LUCVarTypeNameString;
extern NSString * const LUCVarTypeNameUnknown;

@interface LUCVar : LUEntry

@property (nonatomic, readonly) LUCVarType type;
@property (nonatomic, retain) NSString *value;

+ (instancetype)variableWithId:(int)entryId name:(NSString *)name value:(NSString *)value type:(LUCVarType)type cellClass:(Class)cellClass;
- (instancetype)initWithId:(int)entryId name:(NSString *)name value:(NSString *)value type:(LUCVarType)type cellClass:(Class)cellClass;

+ (LUCVarType)typeForName:(NSString *)type;
+ (NSString *)typeNameForType:(LUCVarType)type;

@end
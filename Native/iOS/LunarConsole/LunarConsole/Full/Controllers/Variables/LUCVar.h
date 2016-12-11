//
//  LUCVar.h
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
@property (nonatomic, strong) NSString *value;
@property (nonatomic, strong) NSString *defaultValue;

+ (instancetype)variableWithId:(int)entryId name:(NSString *)name value:(NSString *)value defaultValue:(NSString *)defaultValue type:(LUCVarType)type cellClass:(Class)cellClass;
- (instancetype)initWithId:(int)entryId name:(NSString *)name value:(NSString *)value defaultValue:(NSString *)defaultValue type:(LUCVarType)type cellClass:(Class)cellClass;

+ (LUCVarType)typeForName:(NSString *)type;
+ (NSString *)typeNameForType:(LUCVarType)type;

@end

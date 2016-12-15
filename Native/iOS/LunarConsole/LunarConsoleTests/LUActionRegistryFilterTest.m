//
//  LUActionRegistryFilterTest.m
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

#import <XCTest/XCTest.h>

#import "Lunar.h"
#import "TestCase.h"

@interface LUEntryInfo : NSObject

@property (nonatomic, readonly) NSString *name;

- (instancetype)initWithName:(NSString *)name;

- (BOOL)isEqualToEntry:(LUEntry *)entry;

@end

@interface LUActionInfo : LUEntryInfo

+ (instancetype)actionWithName:(NSString *)name;

@end

@interface LUCVarInfo : LUEntryInfo

@property (nonatomic, readonly) NSString *value;
@property (nonatomic, readonly) NSString *type;

+ (instancetype)variableWithName:(NSString *)name value:(NSString *)value type:(NSString *)type;
- (instancetype)initWithName:(NSString *)name value:(NSString *)value type:(NSString *)type;

@end

static int _nextActionId;

@interface LUActionRegistryFilterTest : TestCase <LUActionRegistryFilterDelegate>
{
    LUActionRegistry * _actionRegistry;
    LUActionRegistryFilter * _registryFilter;
}

@end

@implementation LUActionRegistryFilterTest

#pragma mark -
#pragma mark Setup

- (void)setUp
{
    [super setUp];
    
    _actionRegistry = [[LUActionRegistry alloc] init];
    _registryFilter = [[LUActionRegistryFilter alloc] initWithActionRegistry:_actionRegistry];
    _registryFilter.delegate = self;
    _nextActionId = 0;
}

- (void)tearDown
{
    [super tearDown];
    
    [_actionRegistry release];
    [_registryFilter release];
}

#pragma mark -
#pragma mark Filter by text

- (void)testFilterByText
{
    LUActionRegistryFilter *filter = createFilter(
      [LUActionInfo actionWithName:@"line1"],
      [LUActionInfo actionWithName:@"line11"],
      [LUActionInfo actionWithName:@"line111"],
      [LUActionInfo actionWithName:@"line1111"],
      [LUActionInfo actionWithName:@"foo"],
      [LUCVarInfo variableWithName:@"line1" value:@"value" type:LUCVarTypeNameString],
      [LUCVarInfo variableWithName:@"line11" value:@"value" type:LUCVarTypeNameString],
      [LUCVarInfo variableWithName:@"line111" value:@"value" type:LUCVarTypeNameString],
      [LUCVarInfo variableWithName:@"line1111" value:@"value" type:LUCVarTypeNameString],
      [LUCVarInfo variableWithName:@"foo" value:@"value" type:LUCVarTypeNameString], nil);
    
    XCTAssert(!filter.isFiltering);
    
    XCTAssert([filter setFilterText:@"l"]);
    [self filter:filter assertActions:@"line1", @"line11", @"line111", @"line1111", nil];
    [self filter:filter assertVariables:@"line1", @"line11", @"line111", @"line1111", nil];
    XCTAssert(filter.isFiltering);
    
    XCTAssert(![filter setFilterText:@"l"]);
    [self filter:filter assertActions:@"line1", @"line11", @"line111", @"line1111", nil];
    [self filter:filter assertVariables:@"line1", @"line11", @"line111", @"line1111", nil];
    XCTAssert(filter.isFiltering);
    
    XCTAssert([filter setFilterText:@"li"]);
    [self filter:filter assertActions:@"line1", @"line11", @"line111", @"line1111", nil];
    [self filter:filter assertVariables:@"line1", @"line11", @"line111", @"line1111", nil];
    XCTAssert(filter.isFiltering);
    
    XCTAssert([filter setFilterText:@"lin"]);
    [self filter:filter assertActions:@"line1", @"line11", @"line111", @"line1111", nil];
    [self filter:filter assertVariables:@"line1", @"line11", @"line111", @"line1111", nil];
    XCTAssert(filter.isFiltering);
    
    XCTAssert([filter setFilterText:@"line"]);
    [self filter:filter assertActions:@"line1", @"line11", @"line111", @"line1111", nil];
    [self filter:filter assertVariables:@"line1", @"line11", @"line111", @"line1111", nil];
    XCTAssert(filter.isFiltering);
    
    XCTAssert([filter setFilterText:@"line1"]);
    [self filter:filter assertActions:@"line1", @"line11", @"line111", @"line1111", nil];
    [self filter:filter assertVariables:@"line1", @"line11", @"line111", @"line1111", nil];
    XCTAssert(filter.isFiltering);
    
    XCTAssert([filter setFilterText:@"line11"]);
    [self filter:filter assertActions:@"line11", @"line111", @"line1111", nil];
    [self filter:filter assertVariables:@"line11", @"line111", @"line1111", nil];
    XCTAssert(filter.isFiltering);
    
    XCTAssert([filter setFilterText:@"line111"]);
    [self filter:filter assertActions:@"line111", @"line1111", nil];
    [self filter:filter assertVariables:@"line111", @"line1111", nil];
    XCTAssert(filter.isFiltering);
    
    XCTAssert([filter setFilterText:@"line1111"]);
    [self filter:filter assertActions:@"line1111", nil];
    [self filter:filter assertVariables:@"line1111", nil];
    XCTAssert(filter.isFiltering);
    
    XCTAssert([filter setFilterText:@"line11111"]);
    [self assertFilterEntries:filter, nil];
    XCTAssert(filter.isFiltering);
    
    XCTAssert([filter setFilterText:@"line1111"]);
    [self filter:filter assertActions:@"line1111", nil];
    [self filter:filter assertVariables:@"line1111", nil];
    XCTAssert(filter.isFiltering);
    
    XCTAssert([filter setFilterText:@"line111"]);
    [self filter:filter assertActions:@"line111", @"line1111", nil];
    [self filter:filter assertVariables:@"line111", @"line1111", nil];
    XCTAssert(filter.isFiltering);
    
    XCTAssert([filter setFilterText:@"line11"]);
    [self filter:filter assertActions:@"line11", @"line111", @"line1111", nil];
    [self filter:filter assertVariables:@"line11", @"line111", @"line1111", nil];
    XCTAssert(filter.isFiltering);
    
    XCTAssert([filter setFilterText:@"line1"]);
    [self filter:filter assertActions:@"line1", @"line11", @"line111", @"line1111", nil];
    [self filter:filter assertVariables:@"line1", @"line11", @"line111", @"line1111", nil];
    XCTAssert(filter.isFiltering);
    
    XCTAssert([filter setFilterText:@"line"]);
    [self filter:filter assertActions:@"line1", @"line11", @"line111", @"line1111", nil];
    [self filter:filter assertVariables:@"line1", @"line11", @"line111", @"line1111", nil];
    XCTAssert(filter.isFiltering);
    
    XCTAssert([filter setFilterText:@"lin"]);
    [self filter:filter assertActions:@"line1", @"line11", @"line111", @"line1111", nil];
    [self filter:filter assertVariables:@"line1", @"line11", @"line111", @"line1111", nil];
    XCTAssert(filter.isFiltering);
    
    XCTAssert([filter setFilterText:@"li"]);
    [self filter:filter assertActions:@"line1", @"line11", @"line111", @"line1111", nil];
    [self filter:filter assertVariables:@"line1", @"line11", @"line111", @"line1111", nil];
    XCTAssert(filter.isFiltering);
    
    XCTAssert([filter setFilterText:@"l"]);
    [self filter:filter assertActions:@"line1", @"line11", @"line111", @"line1111", nil];
    [self filter:filter assertVariables:@"line1", @"line11", @"line111", @"line1111", nil];
    XCTAssert(filter.isFiltering);
    
    XCTAssert([filter setFilterText:@""]);
    [self filter:filter assertActions:@"foo", @"line1", @"line11", @"line111", @"line1111", nil];
    [self filter:filter assertVariables:@"foo", @"line1", @"line11", @"line111", @"line1111", nil];
    XCTAssert(!filter.isFiltering);
}

#pragma mark -
#pragma mark Register entries

- (void)testRegisterEntries
{
    [self registerActionWithName:@"a11"];
    [self registerVariableWithName:@"v11"];
    
    [self registerActionWithName:@"a1"];
    [self registerVariableWithName:@"v1"];
    
    [self registerActionWithName:@"a111"];
    [self registerVariableWithName:@"v111"];
    
    [self filter:_registryFilter assertActions:@"a1", @"a11", @"a111", nil];
    [self filter:_registryFilter assertVariables:@"v1", @"v11", @"v111", nil];
}

- (void)testRegisterEntriesFiltered
{
    [self setFilterText:@"a11"];
    
    [self registerActionWithName:@"a11"];
    [self registerActionWithName:@"a1"];
    [self registerActionWithName:@"a111"];
    
    [self filter:_registryFilter assertActions:@"a11", @"a111", nil];
    [self filter:_registryFilter assertVariables:nil];
    
    [self registerVariableWithName:@"v11"];
    [self registerVariableWithName:@"v1"];
    [self registerVariableWithName:@"v111"];
    
    [self setFilterText:@"v11"];
    
    [self filter:_registryFilter assertActions:nil];
    [self filter:_registryFilter assertVariables:@"v11", @"v111", nil];
    
    // remove the filter
    [self setFilterText:@""];
    
    [self filter:_registryFilter assertActions:@"a1", @"a11", @"a111", nil];
    [self filter:_registryFilter assertVariables:@"v1", @"v11", @"v111", nil];
}

- (void)testRegisterMultipleActionsWithSameName
{
    [self registerActionWithName:@"a2"];
    [self registerActionWithName:@"a3"];
    [self registerActionWithName:@"a1"];
    [self registerActionWithName:@"a3"];
    
    // should be in default group
    [self filter:_registryFilter assertActions:@"a1", @"a2", @"a3", nil];
}

#pragma mark -
#pragma mark Unregister actions

- (void)testUnregisterActions
{
    int id2 = [self registerActionWithName:@"a2"].actionId;
    int id1 = [self registerActionWithName:@"a1"].actionId;
    int id3 = [self registerActionWithName:@"a3"].actionId;
    
    [self unregisterActionWithId:id1];
    [self filter:_registryFilter assertActions:@"a2", @"a3", nil];
    
    [self unregisterActionWithId:id2];
    [self filter:_registryFilter assertActions:@"a3", nil];
    
    [self unregisterActionWithId:id3];
    [self filter:_registryFilter assertActions:nil];
    
    [self unregisterActionWithId:id3];
    [self filter:_registryFilter assertActions:nil];
}

- (void)testUnregisterActionFiltered
{
    [self setFilterText:@"a11"];
    
    int id2 = [self registerActionWithName:@"a11"].actionId;
    int id1 = [self registerActionWithName:@"a1"].actionId;
    int id3 = [self registerActionWithName:@"a111"].actionId;
    
    [self unregisterActionWithId:id1];
    [self filter:_registryFilter assertActions:@"a11", @"a111", nil];
    
    [self unregisterActionWithId:id2];
    [self filter:_registryFilter assertActions:@"a111", nil];
    
    [self unregisterActionWithId:id3];
    [self filter:_registryFilter assertActions:nil];
    
    [self unregisterActionWithId:id3];
    [self filter:_registryFilter assertActions:nil];
}

#pragma mark -
#pragma mark Delegate notifications

- (void)testDelegateNotifications
{
    // register actions
    [self registerActionWithName:@"a2"];
    [self assertResult:@"added action: a2 (0)", nil];
    
    [self registerActionWithName:@"a1"];
    [self assertResult:@"added action: a1 (0)", nil];
    
    [self registerActionWithName:@"a3"];
    [self assertResult:@"added action: a3 (2)", nil];
    
    // register variables
    [self registerVariableWithName:@"1.bool" type:LUCVarTypeNameBoolean value:@"1"];
    [self assertResult:@"register variable: Boolean 1.bool 1 (0)", nil];
    
    [self registerVariableWithName:@"2.int" type:LUCVarTypeNameInteger value:@"10"];
    [self assertResult:@"register variable: Integer 2.int 10 (1)", nil];
    
    [self registerVariableWithName:@"3.float" type:LUCVarTypeNameFloat value:@"3.14"];
    [self assertResult:@"register variable: Float 3.float 3.14 (2)", nil];
    
    [self registerVariableWithName:@"4.string" type:LUCVarTypeNameString value:@"value"];
    [self assertResult:@"register variable: String 4.string value (3)", nil];
    
    // unregister variables
    [self unregisterActionWithName:@"a1"];
    [self assertResult:@"removed action: a1 (0)", nil];
    
    [self unregisterActionWithName:@"a3"];
    [self assertResult:@"removed action: a3 (1)", nil];
    
    [self unregisterActionWithName:@"a2"];
    [self assertResult:@"removed action: a2 (0)", nil];
}

- (void)testDelegateNotificationsFiltered
{
    // XCTFail(@"Implement me");
}

- (void)testFilteringByTextAddActions
{
//    LUActionRegistryFilter *filter = createFilter(nil);
//    XCTAssert(!filter.isFiltering);
//    
//    XCTAssert([filter setFilterText:@"line11"]);
//    [filter.registry registerActionWithId:0 name:@"line111" group:@""];
//    
//    [self filter:filter assertActions:@"line111", nil];
//    XCTAssert(filter.isFiltering);
//    
//    [filter.registry registerActionWithId:1 name:@"line1" group:@""];
//    [filter.registry registerActionWithId:2 name:@"line11" group:@""];
//    
//    [self filter:filter assertActions:@"line11", @"line111", nil];
//    
//    [filter.registry unregisterActionWithId:2];
//    [self filter:filter assertActions:@"line111", nil];
//    
//    [filter.registry unregisterActionWithId:1];
//    [self filter:filter assertActions:@"line111", nil];
//    
//    [filter.registry unregisterActionWithId:0];
//    [self assertFilterGroups:filter, nil];
//    
//    [filter.registry registerActionWithId:3 name:@"line1" group:@"a"];
//    [self assertFilterGroups:filter, nil];
//    
//    [filter.registry registerActionWithId:4 name:@"line11" group:@"a"];
//    [self assertFilterGroups:filter, [LUActionGroupInfo groupInfoWithName:@"a" actions:@"line11", nil], nil];
//    
//    [filter setFilterText:@""];
//    [self assertFilterGroups:filter, [LUActionGroupInfo groupInfoWithName:@"a" actions:@"line1", @"line11", nil], nil];
    
    // XCTFail(@"Implement me");
}

#pragma mark -
#pragma mark LUActionRegistryFilterDelegate

- (void)actionRegistryFilter:(LUActionRegistryFilter *)registryFilter didAddAction:(LUAction *)action atIndex:(NSUInteger)index
{
    [self addResult:[NSString stringWithFormat:@"added action: %@ (%d)", action.name, (int) index]];
}

- (void)actionRegistryFilter:(LUActionRegistryFilter *)registryFilter didRemoveAction:(LUAction *)action atIndex:(NSUInteger)index
{
    [self addResult:[NSString stringWithFormat:@"removed action: %@ (%d)", action.name, (int) index]];
}

- (void)actionRegistryFilter:(LUActionRegistryFilter *)registry didRegisterVariable:(LUCVar *)variable atIndex:(NSUInteger)index
{
    [self addResult:[NSString stringWithFormat:@"register variable: %@ %@ %@ (%d)", [LUCVar typeNameForType:variable.type], variable.name, variable.value, (int) index]];
}

- (void)actionRegistryFilter:(LUActionRegistryFilter *)registry didChangeVariable:(LUCVar *)variable atIndex:(NSUInteger)index
{
    XCTFail(@"Implement me");
}

#pragma mark -
#pragma mark Helpers

- (void)setFilterText:(NSString *)text
{
    [_registryFilter setFilterText:text];
}

- (void)filter:(LUActionRegistryFilter *)filter assertActions:(NSString *)first, ... NS_REQUIRES_NIL_TERMINATION
{
    va_list ap;
    va_start(ap, first);
    NSMutableArray *expectedNames = [NSMutableArray array];
    for (NSString *name = first; name != nil; name = va_arg(ap, NSString *))
    {
        [expectedNames addObject:name];
    }
    va_end(ap);
    
    NSMutableArray *actualNames = [NSMutableArray array];
    for (NSInteger i = 0; i < filter.actions.count; ++i)
    {
        LUAction *action = filter.actions[i];
        [actualNames addObject:action.name];
    }
    
    XCTAssertEqual(expectedNames.count, actualNames.count, @"Expected [%@] but was [%@]", [expectedNames componentsJoinedByString:@","], [actualNames componentsJoinedByString:@","]);
    for (NSInteger i = 0; i < actualNames.count; ++i)
    {
        XCTAssertEqualObjects(expectedNames[i], actualNames[i], @"Expected [%@] but was [%@]", [expectedNames componentsJoinedByString:@","], [actualNames componentsJoinedByString:@","]);
    }
}

- (void)filter:(LUActionRegistryFilter *)filter assertVariables:(NSString *)first, ... NS_REQUIRES_NIL_TERMINATION
{
    va_list ap;
    va_start(ap, first);
    NSMutableArray *expectedNames = [NSMutableArray array];
    for (NSString *name = first; name != nil; name = va_arg(ap, NSString *))
    {
        [expectedNames addObject:name];
    }
    va_end(ap);
    
    NSMutableArray *actualNames = [NSMutableArray array];
    for (NSInteger i = 0; i < filter.variables.count; ++i)
    {
        LUEntry *entry = filter.variables[i];
        [actualNames addObject:entry.name];
    }
    
    XCTAssertEqual(expectedNames.count, actualNames.count, @"Expected [%@] but was [%@]", [expectedNames componentsJoinedByString:@","], [actualNames componentsJoinedByString:@","]);
    for (NSInteger i = 0; i < actualNames.count; ++i)
    {
        XCTAssertEqualObjects(expectedNames[i], actualNames[i], @"Expected [%@] but was [%@]", [expectedNames componentsJoinedByString:@","], [actualNames componentsJoinedByString:@","]);
    }
}

- (void)assertFilterEntries:(LUActionRegistryFilter *)filter, ... NS_REQUIRES_NIL_TERMINATION
{
    va_list ap;
    va_start(ap, filter);
    NSMutableArray *expectedActions = [NSMutableArray array];
    NSMutableArray *expectedVariable = [NSMutableArray array];
    for (LUActionInfo *info = va_arg(ap, LUActionInfo *); info != nil; info = va_arg(ap, LUActionInfo *))
    {
        if ([info isKindOfClass:[LUActionInfo class]])
        {
            [expectedActions addObject:info];
        }
        else if ([info isKindOfClass:[LUCVarInfo class]])
        {
            [expectedVariable addObject:info];
        }
    }
    va_end(ap);
    
    XCTAssertEqual(expectedActions.count, filter.actions.count, @"Expected [%@] but was [%@]", [expectedActions componentsJoinedByString:@","], [filter.actions componentsJoinedByString:@","]);
    for (NSInteger i = 0; i < filter.actions.count; ++i)
    {
        LUActionInfo *info = expectedActions[i];
        XCTAssertTrue([info isEqualToEntry:filter.actions[i]], @"Expected [%@] but was [%@]", [expectedActions componentsJoinedByString:@","], [filter.actions componentsJoinedByString:@","]);
    }
}

- (LUAction *)registerActionWithName:(NSString *)name
{
    return [_actionRegistry registerActionWithId:_nextActionId++ name:name];
}

- (LUCVar *)registerVariableWithName:(NSString *)name type:(NSString *)typeName value:(NSString *)value
{
    return [_actionRegistry registerVariableWithId:_nextActionId++ name:name typeName:typeName value:value defaultValue:value];
}

- (LUCVar *)registerVariableWithName:(NSString *)name
{
    return [_actionRegistry registerVariableWithId:_nextActionId++ name:name typeName:LUCVarTypeNameString value:@"value" defaultValue:@"value"];
}

- (BOOL)unregisterActionWithName:(NSString *)name
{
    for (LUAction* action in _actionRegistry.actions)
    {
        if ([action.name isEqualToString:name])
        {
            [_actionRegistry unregisterActionWithId:action.actionId];
            return YES;
        }
    }
    
    return NO;
}

- (void)unregisterActionWithId:(int)actionId
{
    [_actionRegistry unregisterActionWithId:actionId];
}

static LUActionRegistryFilter *createFilter(LUEntryInfo *first, ...)
{
    LUActionRegistry *registry = [LUActionRegistry registry];
    
    va_list ap;
    va_start(ap, first);
    for (LUEntryInfo *info = first; info != nil; info = va_arg(ap, LUEntryInfo *))
    {
        if ([info isKindOfClass:[LUActionInfo class]])
        {
            [registry registerActionWithId:_nextActionId++ name:info.name];
        }
        else if ([info isKindOfClass:[LUCVarInfo class]])
        {
            LUCVarInfo *cvar = (LUCVarInfo *)info;
            [registry registerVariableWithId:_nextActionId++ name:cvar.name typeName:cvar.type value:cvar.value defaultValue:cvar.value];
        }
        else
        {
            abort(); // not the best solution but better than ignoring
        }
    }
    va_end(ap);
    
    return [LUActionRegistryFilter filterWithActionRegistry:registry];
}


@end

@implementation LUEntryInfo

- (instancetype)initWithName:(NSString *)name
{
    self = [super init];
    if (self)
    {
        _name = [name retain];
    }
    return self;
}

- (void)dealloc
{
    [_name release];
    [super dealloc];
}

- (BOOL)isEqualToEntry:(LUEntry *)entry
{
    return NO;
}

@end

@implementation LUActionInfo

+ (instancetype)actionWithName:(NSString *)name
{
    return [[[self alloc] initWithName:name] autorelease];
}

- (BOOL)isEqualToEntry:(LUEntry *)entry
{
    return [entry isKindOfClass:[LUAction class]] && [entry.name isEqualToString:self.name];
}

@end

@implementation LUCVarInfo

+ (instancetype)variableWithName:(NSString *)name value:(NSString *)value type:(NSString *)type
{
    return [[[self alloc] initWithName:name value:value type:type] autorelease];
}

- (instancetype)initWithName:(NSString *)name value:(NSString *)value type:(NSString *)type
{
    self = [super initWithName:name];
    if (self)
    {
        _value = [value retain];
        _type = [type retain];
    }
    return self;
}

- (void)dealloc
{
    [_value release];
    [_type release];
    [super dealloc];
}

- (BOOL)isEqualToEntry:(LUEntry *)entry
{
    if (![entry isKindOfClass:[LUCVarInfo class]]) return NO;
    
    LUCVar *cvar = (LUCVar *)entry;
    return [self.name isEqualToString:cvar.name] &&
           [self.value isEqualToString:cvar.value] &&
           [self.type isEqualToString:[LUCVar typeNameForType:cvar.type]];
}

@end

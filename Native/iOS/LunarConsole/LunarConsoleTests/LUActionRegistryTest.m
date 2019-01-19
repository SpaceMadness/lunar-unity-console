//
//  LUActionRegistryTest.m
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2019 Alex Lementuev, SpaceMadness.
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

#import "TestCase.h"

#import "Lunar.h"
#import "Lunar-Full.h"

@interface LUActionRegistryTest : TestCase <LUActionRegistryDelegate>
{
    LUActionRegistry * _actionRegistry;
    int _nextActionId;
}
@end

@implementation LUActionRegistryTest

#pragma mark -
#pragma mark Setup/Teardown

- (void)setUp
{
    [super setUp];
    _actionRegistry = [[LUActionRegistry alloc] init];
    _actionRegistry.delegate = self;
    _nextActionId = 0;
}

- (void)tearDown
{
    [super tearDown];
    [_actionRegistry release];
}

#pragma mark -
#pragma mark Register

- (void)testRegisterActionsAndVariables
{
    [self registerActionWithName:@"a2"];
    [self registerActionWithName:@"a1"];
    [self registerActionWithName:@"a3"];
    
    [self registerVariableWithName:@"v2"];
    [self registerVariableWithName:@"v1"];
    [self registerVariableWithName:@"v3"];
    
    [self assertActions:@"a1", @"a2", @"a3", nil];
    [self assertVariables:@"v1", @"v2", @"v3", nil];
}

- (void)testRegisterMultipleActionsWithSameName
{
    [self registerActionWithName:@"a2"];
    [self registerActionWithName:@"a3"];
    [self registerActionWithName:@"a1"];
    [self registerActionWithName:@"a3"];
    
    [self assertActions:@"a1", @"a2", @"a3", nil];
}

#pragma mark -
#pragma mark Unregister actions

- (void)testUnregisterAction
{
    int id2 = [self registerActionWithName:@"a2"].actionId;
    int id1 = [self registerActionWithName:@"a1"].actionId;
    int id3 = [self registerActionWithName:@"a3"].actionId;
    
    [self unregisterActionWithId:id1];
    [self assertActions:@"a2", @"a3", nil];
    
    [self unregisterActionWithId:id2];
    [self assertActions:@"a3", nil];

    [self unregisterActionWithId:id3];
    [self assertActions:nil];
    
    [self unregisterActionWithId:id3];
    [self assertActions:nil];
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

#pragma mark -
#pragma mark LUActionRegistryDelegte

- (void)actionRegistry:(LUActionRegistry *)registry didAddAction:(LUAction *)action atIndex:(NSUInteger)index
{
    [self addResult:[NSString stringWithFormat:@"added action: %@ (%d)", action.name, (int) index]];
}

- (void)actionRegistry:(LUActionRegistry *)registry didRemoveAction:(LUAction *)action atIndex:(NSUInteger)index
{
    [self addResult:[NSString stringWithFormat:@"removed action: %@ (%d)", action.name, (int) index]];
}

- (void)actionRegistry:(LUActionRegistry *)registry didRegisterVariable:(LUCVar *)variable atIndex:(NSUInteger)index
{
    [self addResult:[NSString stringWithFormat:@"register variable: %@ %@ %@ (%d)", [LUCVar typeNameForType:variable.type], variable.name, variable.value, (int) index]];
}

- (void)actionRegistry:(LUActionRegistry *)registry didDidChangeVariable:(LUCVar *)variable atIndex:(NSUInteger)index
{
    XCTFail(@"Implement me");
}

#pragma mark -
#pragma mark Helpers

- (LUAction *)registerActionWithName:(NSString *)name
{
    return [_actionRegistry registerActionWithId:_nextActionId++ name:name];
}

- (LUCVar *)registerVariableWithName:(NSString *)name
{
    return [self registerVariableWithName:name type:LUCVarTypeNameString];
}

- (LUCVar *)registerVariableWithName:(NSString *)name type:(NSString *)typeName
{
    return [self registerVariableWithName:name type:typeName value:@"value"];
}

- (LUCVar *)registerVariableWithName:(NSString *)name type:(NSString *)typeName value:(NSString *)value
{
    return [_actionRegistry registerVariableWithId:_nextActionId++ name:name typeName:typeName value:value defaultValue:value];
}

- (void)unregisterActionWithId:(int)actionId
{
    [_actionRegistry unregisterActionWithId:actionId];
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

- (void)assertActions:(NSString *)first, ... NS_REQUIRES_NIL_TERMINATION
{
    NSMutableArray *expected = [NSMutableArray array];
    va_list ap;
    va_start(ap, first);
    
    for (NSString *name = first; name != nil; name = va_arg(ap, NSString *))
    {
        [expected addObject:name];
    }
    
    va_end(ap);
    
    XCTAssertEqual(expected.count, _actionRegistry.actions.count);
    
    NSUInteger index = 0;
    for (LUAction *action in _actionRegistry.actions)
    {
        XCTAssertEqualObjects(expected[index], action.name);
        ++index;
    }
}

- (void)assertVariables:(NSString *)first, ... NS_REQUIRES_NIL_TERMINATION
{
    NSMutableArray *expected = [NSMutableArray array];
    va_list ap;
    va_start(ap, first);
    
    for (NSString *name = first; name != nil; name = va_arg(ap, NSString *))
    {
        [expected addObject:name];
    }
    
    va_end(ap);
    
    XCTAssertEqual(expected.count, _actionRegistry.variables.count);
    
    NSUInteger index = 0;
    for (LUCVar *cvar in _actionRegistry.variables)
    {
        XCTAssertEqualObjects(expected[index], cvar.name);
        ++index;
    }
}

@end

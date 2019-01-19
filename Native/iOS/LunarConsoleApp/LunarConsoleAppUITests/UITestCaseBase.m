//
//  UITestCaseBase.m
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

#import "UITestCaseBase.h"

@interface UITestCaseBase () <NetPeerDelegate>

@end

@implementation UITestCaseBase
{
    NetPeer * _peer;
    NSCondition * _clientConnectCondition;
    NSCondition * _clientAckCondition;
    NSCondition * _resultCondition;
    BOOL _clientConnected;
    BOOL _messageAcknowledged;
    NSMutableArray * _result;
}

- (void)setUp
{
    [super setUp];
    
    _result = [NSMutableArray new];
    
    _clientConnectCondition = [NSCondition new];
    _clientAckCondition = [NSCondition new];
    _resultCondition = [NSCondition new];
    
    _peer = [NetPeer new];
    _peer.delegate = self;
    [_peer startListeningOnPort:10500];
}

- (void)tearDown
{
    [_peer shutdown];
    
    [super tearDown];
}

#pragma mark -
#pragma mark Result

- (void)assertResult:(NSArray *)expected
{
    [_resultCondition lock];
    if (expected.count != _result.count)
    {
        [_resultCondition waitUntilDate:[NSDate dateWithTimeIntervalSinceNow:5.0]];
    }
    NSString *message = [NSString stringWithFormat:@"Expected: '%@' but was '%@'", [expected componentsJoinedByString:@","], [_result componentsJoinedByString:@","]];
    
    XCTAssertEqual(expected.count, _result.count, @"%@", message);
    for (int i = 0; i < expected.count; ++i)
    {
        XCTAssertEqualObjects([expected objectAtIndex:i], [_result objectAtIndex:i], @"%@", message);
    }
    
    [_result removeAllObjects];
    [_resultCondition unlock];
}

- (void)addResult:(NSString *)result
{
    [_resultCondition lock];
    [_result addObject:result];
    [_resultCondition signal];
    [_resultCondition unlock];
}

#pragma mark -
#pragma mark Network

- (void)waitForClientToConnect
{
    [_clientConnectCondition lock];
    while (!_clientConnected)
    {
        [_clientConnectCondition wait];
    }
    [_clientConnectCondition unlock];
}

- (void)sendMessage:(NetPeerMessage *)message
{
    [_clientAckCondition lock];
    
    _messageAcknowledged = NO;
    [_peer sendMessage:message];
    while (!_messageAcknowledged)
    {
        [_clientAckCondition wait];
    }
    [_clientAckCondition unlock];
}

#pragma mark -
#pragma mark Helpers

- (void)app:(XCUIApplication *)app tapButton:(NSString *)title
{
    XCTAssertTrue(app.buttons[title].exists);
    [app.buttons[title] tap];
}

- (void)app:(XCUIApplication *)app tapSwitch:(NSString *)title
{
    XCTAssertTrue(app.switches[title].exists);
    [app.switches[title] tap];
}

- (void)app:(XCUIApplication *)app tapTextField:(NSString *)title
{
    XCUIElement *element = app.textFields[title];
    [self waitUntilElementExists:element timeout:5.0];
    [element tap];
}

- (void)app:(XCUIApplication *)app tapSearchField:(NSString *)title
{
    XCUIElement *element = app.searchFields[title];
    [self waitUntilElementExists:element timeout:5.0];
    [element tap];
}

- (void)appDeleteText:(XCUIApplication *)app
{
    [[self appDeleteKey:app] pressForDuration:2.5];
}

- (void)appDeleteChar:(XCUIApplication *)app
{
    [[self appDeleteKey:app] tap];
}

- (void)app:(XCUIApplication *)app textField:(NSString *)textField enterText:(NSString *)text
{
    // find element
    XCUIElement *element = app.textFields[textField];
    
    // tap element
    [element tap];
    
    // delete old text
    [self appDeleteText:app];
    
    // type new text
    [element typeText:text];
    
    // hit 'return'
    [[self appReturnButton:app] tap];
}

- (XCUIElement *)appDeleteKey:(XCUIApplication *)app
{
    if (app.keys[@"Delete"].exists) return app.keys[@"Delete"];
    if (app.keys[@"delete"].exists) return app.keys[@"delete"];
    
    XCTFail(@"Can't resolve 'delete' button");
    return nil;
}

- (XCUIElement *)appReturnButton:(XCUIApplication *)app
{
    if (app.buttons[@"Return"].exists) return app.buttons[@"Return"];
    if (app.buttons[@"return"].exists) return app.buttons[@"return"];
    
    XCTFail(@"Can't resolve 'return' button");
    return nil;
}

- (XCUIElement *)appSearchButton:(XCUIApplication *)app
{
    if (app.buttons[@"Search"].exists) return app.buttons[@"Search"];
    if (app.buttons[@"search"].exists) return app.buttons[@"search"];
    
    XCTFail(@"Can't resolve 'search' button");
    return nil;
}

- (void)app:(XCUIApplication *)app textField:(NSString *)textField assertText:(NSString *)text
{
    // find element
    XCUIElement *element = app.textFields[textField];
    
    // check value
    XCTAssertEqualObjects(text, element.value);
}

- (void)app:(XCUIApplication *)app runCommandName:(NSString *)name payload:(NSDictionary *)payload
{
    NetPeerMessage *msg = [[NetPeerMessage alloc] initWithName:name];
    for (NSString *key in payload)
    {
        msg[key] = payload[key];
    }
    [self sendMessage:msg];
}

- (void)checkTable:(XCUIElement *)table items:(NSArray<NSString *> *)items
{
    XCUIElementQuery *staticTexts = table.staticTexts;
    XCTAssertEqual(staticTexts.count, items.count);
    for (int i = 0; i < staticTexts.count; ++i)
    {
        XCUIElement *element = [staticTexts elementBoundByIndex:i];
        XCTAssert([items[i] isEqualToString:element.label]);
    }
}

#pragma mark -
#pragma mark NetPeerDelegate

- (void)clientDidConnect
{
    [_clientConnectCondition lock];
    _clientConnected = YES;
    [_clientConnectCondition signal];
    [_clientConnectCondition unlock];
}

- (void)peer:(NetPeer *)peer didReceiveMessage:(NetPeerMessage *)message
{
    NSString *name = message.name;
    if ([name isEqualToString:@"native_callback"])
    {
        [self addResult:message[@"message"]];
    }
}

- (void)peerDidReceiveAck:(NetPeer *)peer
{
    [_clientAckCondition lock];
    _messageAcknowledged = YES;
    [_clientAckCondition signal];
    [_clientAckCondition unlock];
}

@end

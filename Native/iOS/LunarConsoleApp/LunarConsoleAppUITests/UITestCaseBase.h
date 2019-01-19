//
//  UITestCaseBase.h
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

#import <XCTest/XCTest.h>

#import "XCTestCase+Operations.h"
#import "NetPeer.h"

@interface UITestCaseBase : XCTestCase

- (void)assertResult:(NSArray *)expected;
- (void)addResult:(NSString *)result;

- (void)waitForClientToConnect;
- (void)sendMessage:(NetPeerMessage *)message;

- (void)app:(XCUIApplication *)app tapButton:(NSString *)title;
- (void)app:(XCUIApplication *)app tapSwitch:(NSString *)title;
- (void)app:(XCUIApplication *)app tapTextField:(NSString *)title;
- (void)app:(XCUIApplication *)app tapSearchField:(NSString *)title;

- (void)appDeleteText:(XCUIApplication *)app;
- (void)appDeleteChar:(XCUIApplication *)app;
- (void)app:(XCUIApplication *)app textField:(NSString *)textField enterText:(NSString *)text;
- (XCUIElement *)appDeleteKey:(XCUIApplication *)app;
- (XCUIElement *)appReturnButton:(XCUIApplication *)app;
- (XCUIElement *)appSearchButton:(XCUIApplication *)app;

- (void)app:(XCUIApplication *)app textField:(NSString *)textField assertText:(NSString *)text;

- (void)app:(XCUIApplication *)app runCommandName:(NSString *)name payload:(NSDictionary *)commandDict;

- (void)checkTable:(XCUIElement *)table items:(NSArray<NSString *> *)items;

@end

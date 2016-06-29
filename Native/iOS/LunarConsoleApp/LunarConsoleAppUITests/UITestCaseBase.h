//
//  UITestCaseBase.h
//  LunarConsoleApp
//
//  Created by Alex Lementuev on 3/23/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import <XCTest/XCTest.h>

#import "XCTestCase+Operations.h"

@interface UITestCaseBase : XCTestCase

- (void)app:(XCUIApplication *)app tapButton:(NSString *)title;
- (void)app:(XCUIApplication *)app tapSwitch:(NSString *)title;
- (void)app:(XCUIApplication *)app tapTextField:(NSString *)title;
- (void)app:(XCUIApplication *)app tapSearchField:(NSString *)title;

- (void)appDeleteText:(XCUIApplication *)app;
- (void)appDeleteChar:(XCUIApplication *)app;
- (void)app:(XCUIApplication *)app textField:(NSString *)textField enterText:(NSString *)text;
- (XCUIElement *)appDeleteKey:(XCUIApplication *)app;
- (XCUIElement *)appReturnButton:(XCUIApplication *)app;

@end

//
//  LUConsoleControllerUITest.m
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
#import "ViewController.h"
#import "UITestCaseBase.h"

#define GET_CONSOLE_TABLE(app) ([app.tables elementBoundByIndex:0])

@interface LUConsoleLogControllerUITest : UITestCaseBase

@end

@implementation LUConsoleLogControllerUITest

#pragma mark -
#pragma mark Setup

- (void)setUp
{
    [super setUp];
    
    // Put setup code here. This method is called before the invocation of each test method in the class.

    // In UI tests it is usually best to stop immediately when a failure occurs.
    self.continueAfterFailure = NO;
    // UI tests must launch the application that they test. Doing this in setup will make sure it happens for each test method.
    [[[XCUIApplication alloc] init] launch];

    // In UI tests it’s important to set the initial state - such as interface orientation - required for your tests before they run. The setUp method is a good place to do this.
}

- (void)tearDown
{
    // Put teardown code here. This method is called after the invocation of each test method in the class.
    [super tearDown];
}

#pragma mark -
#pragma mark Tests

- (void)testFilter
{
    XCUIApplication *app = [[XCUIApplication alloc] init];
    
    [self app:app logMessage:@"Debug-1" logType:LUConsoleLogTypeLog];
    [self app:app logMessage:@"Debug-2" logType:LUConsoleLogTypeLog];
    [self app:app logMessage:@"Warning-1" logType:LUConsoleLogTypeWarning];
    [self app:app logMessage:@"Warning-2" logType:LUConsoleLogTypeWarning];
    [self app:app logMessage:@"Error-1" logType:LUConsoleLogTypeError];
    [self app:app logMessage:@"Error-2" logType:LUConsoleLogTypeError];
    
    [app.buttons[@"Show Controller"] tap];
    
    XCUIElement *table = GET_CONSOLE_TABLE(app);
    XCTAssert(table.hittable);
    
    [self assertTable:table, @"Debug-1", @"Debug-2", @"Warning-1", @"Warning-2", @"Error-1", @"Error-2", nil];
    
    [app.buttons[@"Type Debug"] tap];
    [self assertTable:table, @"Warning-1", @"Warning-2", @"Error-1", @"Error-2", nil];
    
    [app.buttons[@"Type Warning"] tap];
    [self assertTable:table, @"Error-1", @"Error-2", nil];
    
    [app.buttons[@"Type Error"] tap];
    [self assertTable:table, nil];
    
    [app.buttons[@"Type Debug"] tap];
    [self assertTable:table, @"Debug-1", @"Debug-2", nil];
    
    [app.buttons[@"Type Warning"] tap];
    [self assertTable:table, @"Debug-1", @"Debug-2", @"Warning-1", @"Warning-2", nil];
    
    [app.buttons[@"Type Error"] tap];
    [self assertTable:table, @"Debug-1", @"Debug-2", @"Warning-1", @"Warning-2", @"Error-1", @"Error-2", nil];
    
    XCUIElement *filterSearchField = app.searchFields[@"Filter"];
    XCTAssertTrue(filterSearchField.exists);
    
    [filterSearchField tap];
    
    [self appDeleteText:app];
    [filterSearchField typeText:@"1"];
    [self assertTable:table, @"Debug-1", @"Warning-1", @"Error-1", nil];
    
    [filterSearchField typeText:@"1"];
    [self assertTable:table, nil];
    
    [self appDeleteChar:app];
    [self assertTable:table, @"Debug-1", @"Warning-1", @"Error-1", nil];
    
    [self appDeleteChar:app];
    [self assertTable:table, @"Debug-1", @"Debug-2", @"Warning-1", @"Warning-2", @"Error-1", @"Error-2", nil];
    
    XCTAssertFalse(filterSearchField.selected);
    
    [filterSearchField tap];
    [filterSearchField typeText:@"2"];
    [self assertTable:table, @"Debug-2", @"Warning-2", @"Error-2", nil];
    
    [filterSearchField typeText:@"2"];
    [self assertTable:table, nil];
    
    [filterSearchField.buttons[@"Clear text"] tap];
    XCTAssertFalse(filterSearchField.selected);
    
    [self assertTable:table, @"Debug-1", @"Debug-2", @"Warning-1", @"Warning-2", @"Error-1", @"Error-2", nil];
    
    [filterSearchField tap];
    [app.buttons[@"Search"] tap];
    XCTAssertFalse(filterSearchField.selected);
}

- (void)testCollapse
{
    XCUIApplication *app = [[XCUIApplication alloc] init];
    
    // add elements to console
    [self app:app logMessage:@"Debug" logType:LUConsoleLogTypeLog];
    [self app:app logMessage:@"Warning" logType:LUConsoleLogTypeWarning];
    [self app:app logMessage:@"Error" logType:LUConsoleLogTypeError];
    [self app:app logMessage:@"Debug" logType:LUConsoleLogTypeLog];
    [self app:app logMessage:@"Warning" logType:LUConsoleLogTypeWarning];
    [self app:app logMessage:@"Error" logType:LUConsoleLogTypeError];
    
    // present controller
    [app.buttons[@"Show Controller"] tap];
    
    XCUIElement *table = GET_CONSOLE_TABLE(app);
    XCTAssert(table.hittable);
    
    // collapse elements
    XCUIElement *lunarConsoleIconButtonMoreButton = app.buttons[@"Console Button More"];
    
    [lunarConsoleIconButtonMoreButton tap];
    [app.buttons[@"Collapse"] tap];
    [self assertTable:table, @"Debug@2", @"Warning@2", @"Error@2", nil];
    
    // close controller
    [app.buttons[@"Console Close Button"] tap];
    
    // re-open controller
    [app.buttons[@"Show Controller"] tap];
    
    table = GET_CONSOLE_TABLE(app);
    XCTAssert(table.hittable);
    
    [self assertTable:table, @"Debug@2", @"Warning@2", @"Error@2", nil];
    
    // expand elements
    lunarConsoleIconButtonMoreButton = app.buttons[@"Console Button More"];
    
    [lunarConsoleIconButtonMoreButton tap];
    
    [app.buttons[@"Expand"] tap];
    [self assertTable:table, @"Debug", @"Warning", @"Error", @"Debug", @"Warning", @"Error", nil];
}

- (void)testOverflow
{
    XCUIApplication *app = [[XCUIApplication alloc] init];
    
    [self app:app textField:@"Test Capacity Text" enterText:@"5"];
    [self app:app tapButton:@"Test Capacity Button"];
    
    [self app:app textField:@"Test Trim Text" enterText:@"3"];
    [self app:app tapButton:@"Test Trim Button"];
    
    // add elements to console
    [self app:app logMessage:@"Debug-1" logType:LUConsoleLogTypeLog];
    [self app:app logMessage:@"Warning-1" logType:LUConsoleLogTypeWarning];
    [self app:app logMessage:@"Error-1" logType:LUConsoleLogTypeError];
    [self app:app logMessage:@"Debug-2" logType:LUConsoleLogTypeLog];
    [self app:app logMessage:@"Warning-2" logType:LUConsoleLogTypeWarning];
    
    // show controller
    [app.buttons[@"Show Controller"] tap];
    
    // check table
    XCUIElement *table = GET_CONSOLE_TABLE(app);
    XCTAssert(table.hittable);
    
    [self assertTable:table, @"Debug-1", @"Warning-1", @"Error-1", @"Debug-2", @"Warning-2", nil];
    
    // close controller
    [app.buttons[@"Console Close Button"] tap];
    
    // make console overflow
    [self app:app logMessage:@"Error-2" logType:LUConsoleLogTypeError];
    
    // show controller
    [app.buttons[@"Show Controller"] tap];
    
    // check table
    table = GET_CONSOLE_TABLE(app);
    XCTAssert(table.hittable);
    
    [self assertTable:table, @"Debug-2", @"Warning-2", @"Error-2", nil];
    
    // check overflow message
    XCUIElement *overflowWarningText = [app.staticTexts elementMatchingType:XCUIElementTypeStaticText
                                                                 identifier:@"Console Overflow Warning"];
    
    XCTAssertTrue(overflowWarningText.hittable);
    
    
    NSString *warningMessage = [NSString stringWithFormat:@"Too much output: %d item(s) trimmed", 3];
    XCTAssertEqualObjects(overflowWarningText.label, warningMessage, @"Expected '%@' but was '%@'", warningMessage, overflowWarningText.label);
}

#pragma mark -
#pragma mark Helpers

- (void)assertTable:(XCUIElement *)table, ... NS_REQUIRES_NIL_TERMINATION
{
    NSMutableArray *expected = [[NSMutableArray alloc] init];
    NSMutableArray *expectedCount = [[NSMutableArray alloc] init];
    va_list ap;
    va_start(ap, table);
    
    for (NSString *value = va_arg(ap, NSString *); value; value = va_arg(ap, NSString *))
    {
        NSRange atRange = [value rangeOfString:@"@"];
        if (atRange.location != NSNotFound)
        {
            [expected addObject:[value substringToIndex:atRange.location]];
            
            NSString *count = [value substringFromIndex:atRange.location + atRange.length];
            [expectedCount addObject:count];
        }
        else
        {
            [expected addObject:value];
            [expectedCount addObject:@"0"];
        }
    }
    
    va_end(ap);
    
    XCUIElementQuery *cells = table.cells;
    
    XCTAssertEqual(cells.count, expected.count);
    for (int i = 0; i < cells.count; ++i)
    {
        XCUIElement *cell = [cells elementBoundByIndex:i];
        XCTAssert([expected[i] isEqualToString:cell.staticTexts[@"Log Message Label"].label]);
        if ([expectedCount[i] isEqualToString:@"0"])
        {
            XCTAssertFalse(cell.staticTexts[@"Log Collapse Label"].exists);
        }
        else if ([expectedCount[i] isEqualToString:@"1"])
        {
            XCTAssertFalse(cell.staticTexts[@"Log Collapse Label"].hittable);
        }
        else
        {
            XCTAssertTrue(cell.staticTexts[@"Log Collapse Label"].hittable);
            XCTAssert([expectedCount[i] isEqualToString:cell.staticTexts[@"Log Collapse Label"].label]);
        }
    }
}

#pragma mark -
#pragma mark Messages

- (void)app:(XCUIApplication *)app logMessage:(NSString *)message logType:(LUConsoleLogType)logType
{
    [self app:app logMessage:message logType:logType count:1];
}

- (void)app:(XCUIApplication *)app logMessage:(NSString *)message logType:(LUConsoleLogType)logType count:(NSInteger)count
{
    [self app:app textField:@"Test Message Text" enterText:message];

    NSString *button = @"Debug";
    
    switch (logType)
    {
        case LUConsoleLogTypeAssert:
        case LUConsoleLogTypeError:
        case LUConsoleLogTypeException:
            button = @"Error";
            break;
            
        case LUConsoleLogTypeWarning:
            button = @"Warning";
            break;
            
        default:
            break;
    }
    
    for (NSInteger i = 0; i < count; ++i)
    {
        [app.buttons[button] tap];
    }
}

@end

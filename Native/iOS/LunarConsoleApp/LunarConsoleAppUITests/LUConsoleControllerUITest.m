//
//  LUConsoleControllerUITest.m
//  LunarConsoleApp
//
//  Created by Alex Lementuev on 10/28/15.
//  Copyright © 2015 Space Madness. All rights reserved.
//

#import <XCTest/XCTest.h>

#import "Lunar.h"
#import "ViewController.h"

#define GET_CONSOLE_TABLE(app) [[[[[[[app childrenMatchingType:XCUIElementTypeWindow] elementBoundByIndex:0] childrenMatchingType:XCUIElementTypeOther].element childrenMatchingType:XCUIElementTypeOther].element childrenMatchingType:XCUIElementTypeOther].element childrenMatchingType:XCUIElementTypeOther].element childrenMatchingType:XCUIElementTypeTable].element

#define ERASE_TEXT(app) [app.keys[@"delete"] pressForDuration:1.3]
#define ERASE_CHAR(app) [app.keys[@"delete"] tap]

@interface LUConsoleControllerUITest : XCTestCase

@end

@implementation LUConsoleControllerUITest

- (void)setUp {
    [super setUp];
    
    // Put setup code here. This method is called before the invocation of each test method in the class.

    // In UI tests it is usually best to stop immediately when a failure occurs.
    self.continueAfterFailure = NO;
    // UI tests must launch the application that they test. Doing this in setup will make sure it happens for each test method.
    [[[XCUIApplication alloc] init] launch];

    // In UI tests it’s important to set the initial state - such as interface orientation - required for your tests before they run. The setUp method is a good place to do this.
}

- (void)tearDown {
    // Put teardown code here. This method is called after the invocation of each test method in the class.
    [super tearDown];
}

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
    
    [filterSearchField tap];
    
    ERASE_TEXT(app);
    [filterSearchField typeText:@"1"];
    [self assertTable:table, @"Debug-1", @"Warning-1", @"Error-1", nil];
    
    [filterSearchField typeText:@"1"];
    [self assertTable:table, nil];
    
    ERASE_CHAR(app);
    [self assertTable:table, @"Debug-1", @"Warning-1", @"Error-1", nil];
    
    ERASE_CHAR(app);
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

#pragma mark -
#pragma mark Helpers

- (void)assertTable:(XCUIElement *)table, ... NS_REQUIRES_NIL_TERMINATION
{
    NSMutableArray *expected = [[NSMutableArray alloc] init];
    va_list ap;
    va_start(ap, table);
    
    for (NSString *value = va_arg(ap, NSString *); value; value = va_arg(ap, NSString *))
    {
        [expected addObject:value];
    }
    
    va_end(ap);
    
    XCUIElementQuery *cells = table.cells;
    
    XCTAssertEqual(cells.count, expected.count);
    for (int i = 0; i < cells.count; ++i)
    {
        XCUIElement *cell = [cells elementBoundByIndex:i];
        XCTAssert([expected[i] isEqualToString:cell.staticTexts[@"Log Message Label"].label]);
    }
}

- (void)app:(XCUIApplication *)app logMessage:(NSString *)message logType:(LUConsoleLogType)logType
{
    XCUIElement *textField = [[[[[app.otherElements containingType:XCUIElementTypeNavigationBar identifier:@"View"] childrenMatchingType:XCUIElementTypeOther].element childrenMatchingType:XCUIElementTypeOther].element childrenMatchingType:XCUIElementTypeOther].element childrenMatchingType:XCUIElementTypeTextField].element;
    [textField tap];
    [app.keys[@"delete"] pressForDuration:1.3];
    [textField typeText:message];
    [app.buttons[@"Return"] tap];

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
    
    [app.buttons[button] tap];
}

@end

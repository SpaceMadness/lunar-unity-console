//
//  LUConsoleEditorSettingsTest.m
//  LunarConsoleTests
//
//  Created by Alex Lementuev on 12/28/18.
//  Copyright © 2018 Space Madness. All rights reserved.
//

#import "TestCase.h"

#import "Lunar.h"
#import "Lunar-Full.h"
#import "LUConsoleEditorSettings.h"

@interface LUConsoleEditorSettingsTest : TestCase

@end

@implementation LUConsoleEditorSettingsTest

- (void)testParsing1 {
	NSString *jsonData = @"{\"exceptionWarning\":true,\"transparentLogOverlay\":false,\"transparentLogOverlaySettings\":{\"maxVisibleLines\":3,\"hideDelay\":1.5},\"sortActions\":true,\"sortVariables\":false,\"emails\":[\"user@foo.com\"]}";
	id json = LUDecodeJson(jsonData);
	LUConsoleEditorSettings *settings = [[LUConsoleEditorSettings alloc] initWithDictionary:json];
	XCTAssertEqual(settings.exceptionWarningEnabled, YES);
	XCTAssertEqual(settings.transparentLogOverlayEnabled, NO);
	XCTAssertEqual(settings.logOverlaySettings.maxVisibleLines, 3);
	XCTAssertEqual(settings.logOverlaySettings.hideDelay, 1.5);
	XCTAssertEqual(settings.actionSortingEnabled, YES);
	XCTAssertEqual(settings.variableSortingEnabled, NO);
	XCTAssertEqual(settings.emails.count, 1);
	XCTAssertEqualObjects(settings.emails[0], @"user@foo.com");
}

- (void)testParsing2 {
	NSString *jsonData = @"{\"exceptionWarning\":false,\"transparentLogOverlay\":true,\"transparentLogOverlaySettings\":{\"maxVisibleLines\":3,\"hideDelay\":1.5},\"sortActions\":false,\"sortVariables\":true,\"emails\":[\"user@foo.com\"]}";
	id json = LUDecodeJson(jsonData);
	LUConsoleEditorSettings *settings = [[LUConsoleEditorSettings alloc] initWithDictionary:json];
	XCTAssertEqual(settings.exceptionWarningEnabled, NO);
	XCTAssertEqual(settings.transparentLogOverlayEnabled, YES);
	XCTAssertEqual(settings.logOverlaySettings.maxVisibleLines, 3);
	XCTAssertEqual(settings.logOverlaySettings.hideDelay, 1.5);
	XCTAssertEqual(settings.actionSortingEnabled, NO);
	XCTAssertEqual(settings.variableSortingEnabled, YES);
	XCTAssertEqual(settings.emails.count, 1);
	XCTAssertEqualObjects(settings.emails[0], @"user@foo.com");
}

@end

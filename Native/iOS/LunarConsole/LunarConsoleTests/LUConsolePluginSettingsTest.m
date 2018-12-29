//
//  LUConsolePluginSettingsTest.m
//  LunarConsoleTests
//
//  Created by Alex Lementuev on 12/28/18.
//  Copyright © 2018 Space Madness. All rights reserved.
//

#import "TestCase.h"

#import "Lunar.h"
#import "Lunar-Full.h"
#import "LUConsolePluginSettings.h"

@interface LUConsolePluginSettingsTest : TestCase

@property (nonatomic, strong) NSString *filename;

@end

@implementation LUConsolePluginSettingsTest

- (void)setUp {
	[super setUp];
	
	_filename = [NSTemporaryDirectory() stringByAppendingPathComponent:@"settings-test.bin"];
	[[NSFileManager defaultManager] removeItemAtPath:_filename error:NULL];
}

- (void)testSavingLoading {
	LUConsolePluginSettings *settings = [[LUConsolePluginSettings alloc] initWithFilename:_filename];
	XCTAssertEqual(settings.enableExceptionWarning, YES);
	XCTAssertEqual(settings.enableTransparentLogOverlay, NO);
	XCTAssertEqual(settings.overlayVisibleLinesCount, 3);
	XCTAssertEqual(settings.overlayHideDelay, 1.0);
	
	settings.enableExceptionWarning = NO;
	settings.enableTransparentLogOverlay = YES;
	settings.overlayVisibleLinesCount = 10;
	settings.overlayHideDelay = 5.0;
	
	BOOL saved = [settings save];
	XCTAssertTrue(saved);
	
	settings = [LUConsolePluginSettings loadFromFile:_filename];
	XCTAssertEqual(settings.enableExceptionWarning, NO);
	XCTAssertEqual(settings.enableTransparentLogOverlay, YES);
	XCTAssertEqual(settings.overlayVisibleLinesCount, 10);
	XCTAssertEqual(settings.overlayHideDelay, 5.0);
}

@end

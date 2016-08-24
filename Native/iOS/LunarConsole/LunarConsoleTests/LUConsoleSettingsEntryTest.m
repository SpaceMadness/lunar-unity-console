//
//  LUConsoleSettingsEntryTest.m
//  LunarConsole
//
//  Created by Alex Lementuev on 8/23/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import <XCTest/XCTest.h>

#import "Lunar.h"

@interface LUConsoleSettingsEntryTest : XCTestCase

@end

@implementation LUConsoleSettingsEntryTest

- (void)testExample
{
    LUConsolePluginSettings *settings = [LUConsolePluginSettings settingsWithContentsOfFile:@"settings.bin"];
    NSArray *entries = [LUConsoleSettingsEntry listSettingsEntries:settings];
    XCTAssertEqual(entries.count, 2);
    
    LUConsoleSettingsEntry *entry = [entries objectAtIndex:0];
    XCTAssertEqualObjects(entry.name, @"enableExceptionWarning");
    XCTAssertEqualObjects(entry.title, @"Enable Exception Warning");
    XCTAssertEqualObjects(entry.type, @"Bool");
    XCTAssertEqualObjects(entry.value, @YES);
    XCTAssertEqualObjects(entry.initialValue, @YES);
    XCTAssertFalse(entry.isChanged);
    
    entry = [entries objectAtIndex:1];
    XCTAssertEqualObjects(entry.name, @"enableTransparentLogOverlay");
    XCTAssertEqualObjects(entry.title, @"Enable Transparent Log Overlay");
    XCTAssertEqualObjects(entry.type, @"Bool");
    XCTAssertEqualObjects(entry.value, @NO);
    XCTAssertEqualObjects(entry.initialValue, @NO);
    XCTAssertFalse(entry.isChanged);
    
    entry.value = @YES;
    XCTAssertTrue(entry.isChanged);
}

@end

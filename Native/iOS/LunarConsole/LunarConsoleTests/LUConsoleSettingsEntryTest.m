//
//  LUConsoleSettingsEntryTest.m
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

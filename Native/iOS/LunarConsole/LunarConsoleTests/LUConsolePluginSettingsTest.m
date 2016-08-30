//
//  LUConsolePluginSettingsTest.m
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

static NSString * const kFilepath = @"settings.bin";

@interface LUConsolePluginSettingsTest : XCTestCase

@end

@implementation LUConsolePluginSettingsTest

- (void)setUp
{
    [super setUp];
    [[NSFileManager defaultManager] removeItemAtPath:[self filepath] error:NULL];
}

- (void)testSaveLoad
{
    LUConsolePluginSettings *settigns = [LUConsolePluginSettings settingsWithContentsOfFile:[self filepath]];
    XCTAssertNotNil(settigns);
    
    [settigns setEnableExceptionWarning:NO];
    [settigns setEnableTransparentLogOverlay:YES];
    BOOL saved = [settigns save];
    XCTAssertTrue(saved);
    
    settigns = [LUConsolePluginSettings settingsWithContentsOfFile:[self filepath]];
    XCTAssertNotNil(settigns);
    XCTAssertFalse(settigns.enableExceptionWarning);
    XCTAssertTrue(settigns.enableTransparentLogOverlay);
}

#pragma mark -
#pragma mark Helpers

- (NSString *)filepath
{
    NSArray *searchPaths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentPath = [searchPaths objectAtIndex:0];
    return [documentPath stringByAppendingPathComponent:kFilepath];
}

@end

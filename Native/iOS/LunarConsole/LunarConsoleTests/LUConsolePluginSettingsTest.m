//
//  LUConsolePluginSettingsTest.m
//  LunarConsole
//
//  Created by Alex Lementuev on 8/22/16.
//  Copyright © 2016 Space Madness. All rights reserved.
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

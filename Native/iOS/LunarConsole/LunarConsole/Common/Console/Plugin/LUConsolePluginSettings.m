//
//  LUConsolePluginSettings.m
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2018 Alex Lementuev, SpaceMadness.
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

#import "Lunar.h"

#import "LUConsolePluginSettings.h"
#import "LUSerializableObject+Inheritance.h"

static NSString * const kKeyEnableExceptionWarning      = @"enableExceptionWarning";
static NSString * const kKeyEnableTransparentLogOverlay = @"enableTransparentLogOverlay";
static NSString * const kKeyOverlayVisibleLinesCount    = @"overlayVisibleLinesCount";
static NSString * const kKeyOverlayHideDelay            = @"overlayHideDelay";

@interface LUConsolePluginSettings ()

// IMPORTANT: don't create any other properties here

@end

@implementation LUConsolePluginSettings

#pragma mark -
#pragma mark Loading

+ (void)initialize
{
    if ([self class] == [LUConsolePluginSettings class])
    {
        [self setVersion:1];
    }
}

#pragma mark -
#pragma mark Inheritance

- (void)initDefaults
{
    _enableExceptionWarning = YES;
    _enableTransparentLogOverlay = NO;
	_overlayVisibleLinesCount = 3;
	_overlayHideDelay = 1.0;
}

- (void)serializeWithCoder:(NSCoder *)coder
{
    [coder encodeBool:_enableExceptionWarning forKey:kKeyEnableExceptionWarning];
    [coder encodeBool:_enableTransparentLogOverlay forKey:kKeyEnableTransparentLogOverlay];
	[coder encodeInteger:_overlayVisibleLinesCount forKey:kKeyOverlayVisibleLinesCount];
	[coder encodeDouble:_overlayHideDelay forKey:kKeyOverlayHideDelay];
}

- (void)deserializeWithDecoder:(NSCoder *)decoder
{
    _enableExceptionWarning = [self decodeBool:decoder forKey:kKeyEnableExceptionWarning defaultValue:YES];
	_enableTransparentLogOverlay = [self decodeBool:decoder forKey:kKeyEnableTransparentLogOverlay defaultValue:NO];
	_overlayVisibleLinesCount = [self decodeInteger:decoder forKey:kKeyOverlayVisibleLinesCount defaultValue:3];
	_overlayHideDelay = [self decodeDouble:decoder forKey:kKeyOverlayHideDelay defaultValue:1.0];
}

@end

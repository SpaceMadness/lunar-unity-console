//
//  LUConsolePluginSettings.m
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

#import "Lunar.h"

#import "LUConsolePluginSettings.h"

static const NSUInteger kPluginSettingsVersion          = 1;

static NSString * const kKeyVersion                     = @"version";
static NSString * const kKeyEnableExceptionWarning      = @"enableExceptionWarning";
static NSString * const kKeyEnableTransparentLogOverlay = @"enableTransparentLogOverlay";

@interface LUConsolePluginSettings () <NSCoding>
{
    NSString   * _filename;
}

// IMPORTANT: don't create any other properties here

@end

@implementation LUConsolePluginSettings

- (instancetype)initWithFilename:(NSString *)filename
{
    self = [super init];
    if (self)
    {
        if (filename == nil)
        {
            self = nil;
            return nil;
        }
        
        _filename = filename;
        [self initDefaults];
    }
    return self;
}


#pragma mark -
#pragma mark NSCoding

- (nullable instancetype)initWithCoder:(NSCoder *)decoder
{
    self = [super init];
    if (self)
    {
        [self initDefaults];
        
        NSInteger version = [decoder decodeIntegerForKey:kKeyVersion];
        if (version == kPluginSettingsVersion)
        {
            _enableExceptionWarning = [decoder decodeBoolForKey:kKeyEnableExceptionWarning];
            _enableTransparentLogOverlay = [decoder decodeBoolForKey:kKeyEnableTransparentLogOverlay];
        }
    }
    return self;
}

- (void)encodeWithCoder:(NSCoder *)coder
{
    [coder encodeInteger:kPluginSettingsVersion forKey:kKeyVersion];
    [coder encodeBool:_enableExceptionWarning forKey:kKeyEnableExceptionWarning];
    [coder encodeBool:_enableTransparentLogOverlay forKey:kKeyEnableTransparentLogOverlay];
}

#pragma mark -
#pragma mark Defaults

- (void)initDefaults
{
    _enableExceptionWarning = YES;
    _enableTransparentLogOverlay = NO;
}

#pragma mark -
#pragma mark Save/Load

+ (instancetype)settingsWithContentsOfFile:(NSString *)filename
{
    LUConsolePluginSettings *settings = LUDeserializeObject(filename);
    if (settings != nil)
    {
        [settings setFilename:filename];
        return settings;
    }
    
    return [[[self class] alloc] initWithFilename:filename];
}

- (BOOL)save
{
    return LUSerializeObject(self, _filename);
}

#pragma mark -
#pragma mark Path

- (void)setFilename:(NSString *)filename
{
    _filename = filename;
}

@end

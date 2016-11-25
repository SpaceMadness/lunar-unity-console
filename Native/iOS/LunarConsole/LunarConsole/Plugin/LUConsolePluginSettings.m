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

#import <objc/runtime.h>

#import "Lunar.h"

#import "LUConsolePluginSettings.h"

static const NSUInteger kPluginSettingsVersion = 1;

@interface LUConsolePluginSettings () <NSCoding>
{
    NSString   * _filepath;
    NSUInteger   _version;
}

// IMPORTANT: don't create any other properties here

@end

@implementation LUConsolePluginSettings

- (instancetype)initWithFilepath:(NSString *)filepath
{
    self = [super init];
    if (self)
    {
        if (filepath == nil)
        {
            [self release];
            self = nil;
            return nil;
        }
        
        _filepath = LU_RETAIN(filepath);
        [self initDefaults];
    }
    return self;
}

- (void)dealloc
{
    [_filepath release];
    LU_SUPER_DEALLOC
}

#pragma mark -
#pragma mark NSCoding

- (nullable instancetype)initWithCoder:(NSCoder *)decoder
{
    self = [super init];
    if (self)
    {
        _version = [decoder decodeIntegerForKey:@"version"];
        _enableExceptionWarning = [decoder decodeBoolForKey:@"enableExceptionWarning"];
        _enableTransparentLogOverlay = [decoder decodeBoolForKey:@"enableTransparentLogOverlay"];
    }
    return self;
}

- (void)encodeWithCoder:(NSCoder *)coder
{
    [coder encodeInteger:_version forKey:@"version"];
    [coder encodeBool:_enableExceptionWarning forKey:@"enableExceptionWarning"];
    [coder encodeBool:_enableTransparentLogOverlay forKey:@"enableTransparentLogOverlay"];
}

#pragma mark -
#pragma mark Defaults

- (void)initDefaults
{
    _version = kPluginSettingsVersion;
    _enableExceptionWarning = YES;
    _enableTransparentLogOverlay = NO;
}

#pragma mark -
#pragma mark Save/Load

+ (instancetype)settingsWithContentsOfFile:(NSString *)path
{
    LUConsolePluginSettings *settings = [NSKeyedUnarchiver unarchiveObjectWithFile:path];
    if (settings != nil)
    {
        [settings setFilepath:path];
        return settings;
    }
    
    return LU_AUTORELEASE([[[self class] alloc] initWithFilepath:path]);
}

- (BOOL)save
{
    return [NSKeyedArchiver archiveRootObject:self toFile:_filepath];
}

#pragma mark -
#pragma mark Path

- (void)setFilepath:(NSString *)filepath
{
    if (_filepath != filepath)
    {
        [_filepath release];
        _filepath = LU_RETAIN(filepath);
    }
}

@end

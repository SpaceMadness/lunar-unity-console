//
//  LUConsolePluginSettings.m
//  LunarConsole
//
//  Created by Alex Lementuev on 8/22/16.
//  Copyright © 2016 Space Madness. All rights reserved.
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
            LU_RELEASE(self);
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
    LU_RELEASE(_filepath);
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
        LU_RELEASE(_filepath);
        _filepath = LU_RETAIN(filepath);
    }
}

@end
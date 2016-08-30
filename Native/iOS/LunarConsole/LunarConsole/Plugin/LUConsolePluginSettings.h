//
//  LUConsolePluginSettings.h
//  LunarConsole
//
//  Created by Alex Lementuev on 8/22/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "LUObject.h"

@interface LUConsolePluginSettings : LUObject

@property (nonatomic, assign) BOOL enableExceptionWarning;
@property (nonatomic, assign) BOOL enableTransparentLogOverlay;

+ (instancetype)settingsWithContentsOfFile:(NSString *)path;
- (BOOL)save;

@end
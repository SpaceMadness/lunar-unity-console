//
//  LUConsoleEditorSettings.m
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2017 Alex Lementuev, SpaceMadness.
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

#import "LUConsoleEditorSettings.h"

@implementation LUConsoleEditorSettings

- (instancetype)init
{
    self = [super init];
    if (self)
    {
        _exceptionWarningEnabled = YES;
        _transparentLogOverlayEnabled = NO;
        _actionSortingEnabled = YES;
        _variableSortingEnabled = YES;
    }
    return self;
}

- (instancetype)initWithDictionary:(NSDictionary *)settings
{
    self = [super init];
    if (self)
    {
        _exceptionWarningEnabled = [[settings objectForKey:@"exceptionWarning"] boolValue];
        _transparentLogOverlayEnabled = [[settings objectForKey:@"transparentLogOverlay"] boolValue];
        _actionSortingEnabled = [[settings objectForKey:@"sortActions"] boolValue];
        _variableSortingEnabled = [[settings objectForKey:@"sortVariables"] boolValue];
        _emails = [settings objectForKey:@"emails"];
    }
    return self;
}

@end

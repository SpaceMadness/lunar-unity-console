//
//  LUConsoleSettingsController.h
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

#import <UIKit/UIKit.h>

#import "LUViewController.h"

@class LUConsolePluginSettings;
@class LUConsoleSettingsController;

@interface LUConsoleSettingsEntry : NSObject

@property (nonatomic, readonly) NSString * name;
@property (nonatomic, readonly) NSString * title;
@property (nonatomic, readonly) NSString * type;
@property (nonatomic, strong)   id value;
@property (nonatomic, readonly) NSString * initialValue;
@property (nonatomic, readonly, getter=isChanged) BOOL changed;
@property (nonatomic, assign, getter=isProOnly) BOOL proOnly;

- (instancetype)initWithName:(NSString *)name value:(id)value type:(NSString *)type title:(NSString *)title;

+ (NSArray *)listSettingsEntries:(LUConsolePluginSettings *)settings;

@end

@interface LUConsoleSettingsController : LUViewController

@property (nonatomic, readonly) NSArray * changedEntries;

- (instancetype)initWithSettings:(LUConsolePluginSettings *)settings;

@end

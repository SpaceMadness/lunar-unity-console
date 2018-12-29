//
//  LUConsoleEditorSettings.h
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

#import <Foundation/Foundation.h>

@interface LUConsoleLogOverlaySettings : NSObject

@property (nonatomic, readonly) NSUInteger maxVisibleLines;
@property (nonatomic, readonly) NSTimeInterval hideDelay;

- (instancetype)initWithDictionary:(NSDictionary *)dict;

@end

@interface LUConsoleEditorSettings : NSObject

@property (nonatomic, readonly, getter=isExceptionWarningEnabled) BOOL exceptionWarningEnabled;
@property (nonatomic, readonly, getter=isTransparentLogOverlayEnabled) BOOL transparentLogOverlayEnabled;
@property (nonatomic, readonly, getter=isActionSortingEnabled) BOOL actionSortingEnabled;
@property (nonatomic, readonly, getter=isVariableSortingEnabled) BOOL variableSortingEnabled;
@property (nonatomic, readonly) LUConsoleLogOverlaySettings * logOverlaySettings;
@property (nonatomic, readonly) NSArray<NSString *> * emails;

- (instancetype)init;
- (instancetype)initWithDictionary:(NSDictionary *)settings;

@end

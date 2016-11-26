//
//  LUConsoleLogController.h
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

#import <UIKit/UIKit.h>

#import "LUViewController.h"

@class LUConsolePlugin;
@class LUConsoleLogController;

@protocol LUConsoleLogControllerDelegate <NSObject>

@optional
- (void)consoleControllerDidOpen:(LUConsoleLogController *)controller;
- (void)consoleControllerDidClose:(LUConsoleLogController *)controller;

@end

@interface LUConsoleLogController : LUViewController

@property (nonatomic, strong) NSString *version;
@property (nonatomic, weak) id<LUConsoleLogControllerDelegate> delegate;

+ (instancetype)controllerWithPlugin:(LUConsolePlugin *)console;
- (instancetype)initWithPlugin:(LUConsolePlugin *)console;

@end

@interface LUConsoleLogControllerState : NSObject

+ (instancetype)sharedControllerState;

@end

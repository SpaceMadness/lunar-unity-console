//
//  LUConsoleSettingsController.h
//  LunarConsole
//
//  Created by Alex Lementuev on 8/22/16.
//  Copyright © 2016 Space Madness. All rights reserved.
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

- (instancetype)initWithName:(NSString *)name value:(id)value type:(NSString *)type title:(NSString *)title;

+ (NSArray *)listSettingsEntries:(LUConsolePluginSettings *)settings;

@end

@protocol LUConsoleSettingsControllerDelegate <NSObject>

- (void)consoleSettingsControllerDidClose:(LUConsoleSettingsController *)controller;

@end

@interface LUConsoleSettingsController : LUViewController

@property (nonatomic, assign) id<LUConsoleSettingsControllerDelegate> delegate;

- (instancetype)initWithSettings:(LUConsolePluginSettings *)settings;

@end
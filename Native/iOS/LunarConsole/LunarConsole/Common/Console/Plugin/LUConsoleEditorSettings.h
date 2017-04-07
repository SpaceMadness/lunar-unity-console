//
//  LUConsoleEditorSettings.h
//  LunarConsole
//
//  Created by Alex Lementuev on 4/6/17.
//  Copyright © 2017 Space Madness. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface LUConsoleEditorSettings : NSObject

@property (nonatomic, readonly, getter=isExceptionWarningEnabled) BOOL exceptionWarningEnabled;
@property (nonatomic, readonly, getter=isTransparentLogOverlayEnabled) BOOL transparentLogOverlayEnabled;
@property (nonatomic, readonly, getter=isActionSortingEnabled) BOOL actionSortingEnabled;
@property (nonatomic, readonly, getter=isVariableSortingEnabled) BOOL variableSortingEnabled;

- (instancetype)init;
- (instancetype)initWithDictionary:(NSDictionary *)settings;

@end

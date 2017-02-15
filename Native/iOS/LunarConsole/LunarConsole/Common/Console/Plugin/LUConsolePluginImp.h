//
//  LUConsolePluginImp.h
//  LunarConsole
//
//  Created by Alex Lementuev on 2/6/17.
//  Copyright © 2017 Space Madness. All rights reserved.
//

#import "LUObject.h"

@class LUConsolePlugin;

@interface LUConsolePluginImp : LUObject

- (instancetype)initWithPlugin:(LUConsolePlugin *)plugin;

- (void)showOverlay;
- (void)hideOverlay;

@end

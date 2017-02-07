//
//  LUConsolePluginImp.m
//  LunarConsole
//
//  Created by Alex Lementuev on 2/6/17.
//  Copyright © 2017 Space Madness. All rights reserved.
//

#import "LUConsolePluginImp.h"
#import "Lunar.h"

@interface LUConsolePluginImp ()
{
    __weak LUConsolePlugin * _plugin;
    LUWindow * _overlayWindow;
}

@end

@implementation LUConsolePluginImp

- (instancetype)initWithPlugin:(LUConsolePlugin *)plugin
{
    self = [super init];
    if (self)
    {
        _plugin = plugin;
    }
    return self;
}

- (void)showOverlay
{
    if (_overlayWindow == nil)
    {
        LUConsoleOverlayControllerSettings *settings = [LUConsoleOverlayControllerSettings settings];
        LUConsoleOverlayController *controller = [LUConsoleOverlayController controllerWithConsole:_plugin.console
                                                                                          settings:settings];
        
        CGRect windowFrame = LUGetScreenBounds();
        _overlayWindow = [[LUWindow alloc] initWithFrame:windowFrame];
        _overlayWindow.userInteractionEnabled = NO;
        _overlayWindow.rootViewController = controller;
        _overlayWindow.opaque = YES;
        _overlayWindow.hidden = NO;
    }
}

- (void)hideOverlay
{
    if (_overlayWindow != nil)
    {
        _overlayWindow.rootViewController = nil;
        _overlayWindow.hidden = YES;
        _overlayWindow = nil;
    }
}

@end

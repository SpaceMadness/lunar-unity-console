//
//  unity_native_interface.c
//  LunarConsole
//
//  Created by Alex Lementuev on 8/3/15.
//  Copyright (c) 2015 Space Madness. All rights reserved.
//

#include <Foundation/Foundation.h>

#include "lunar_unity_native_interface.h"

#import "Lunar.h"
#import "LUConsolePlugin.h"

static LUConsolePlugin * _lunarConsolePlugin;

void __lunar_console_initialize(int capacity)
{
    lunar_dispatch_main(^{
        if (_lunarConsolePlugin == nil) {
            _lunarConsolePlugin = [[LUConsolePlugin alloc] initWithCapacity:capacity];
            [_lunarConsolePlugin enableGestureRecognition];
        }
    });
}

void __lunar_console_destroy() {
    lunar_dispatch_main(^{
        LU_RELEASE(_lunarConsolePlugin);
        _lunarConsolePlugin = nil;
    });
}

void __lunar_console_show()
{
    lunar_dispatch_main(^{
        LUAssert(_lunarConsolePlugin);
        [_lunarConsolePlugin show];
    });
}

void __lunar_console_hide()
{
    lunar_dispatch_main(^{
        LUAssert(_lunarConsolePlugin);
        [_lunarConsolePlugin hide];
    });
}

void __lunar_console_log_message(const char * cmessage, const char * cstackTrace, int type)
{
    NSString *message = [[NSString alloc] initWithUTF8String:cmessage];
    NSString *stackTrace = [[NSString alloc] initWithUTF8String:cstackTrace];

    [_lunarConsolePlugin logMessage:message stackTrace:stackTrace type:type];
    
    LU_RELEASE(message);
    LU_RELEASE(stackTrace);
}
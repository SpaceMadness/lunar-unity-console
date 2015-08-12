//
//  unity_native_interface.h
//  LunarConsole
//
//  Created by Alex Lementuev on 8/3/15.
//  Copyright (c) 2015 Space Madness. All rights reserved.
//

#ifndef __LunarConsole__unity_native_interface__
#define __LunarConsole__unity_native_interface__

OBJC_EXTERN void __lunar_console_initialize(int capacity);
OBJC_EXTERN void __lunar_console_destroy(void);
OBJC_EXTERN void __lunar_console_show(void);
OBJC_EXTERN void __lunar_console_hide(void);
OBJC_EXTERN void __lunar_console_log_message(const char *, const char *, int);

#endif /* defined(__LunarConsole__unity_native_interface__) */

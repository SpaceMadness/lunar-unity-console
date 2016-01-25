//
//  lunar_unity_native_interface.h
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

#ifndef __LunarConsole__unity_native_interface__
#define __LunarConsole__unity_native_interface__

OBJC_EXTERN void __lunar_console_initialize(const char *version, int capacity, int trimCount, const char *gesture);
OBJC_EXTERN void __lunar_console_destroy(void);
OBJC_EXTERN void __lunar_console_show(void);
OBJC_EXTERN void __lunar_console_hide(void);
OBJC_EXTERN void __lunar_console_clear(void);
OBJC_EXTERN void __lunar_console_log_message(const char *message, const char *stacktrace, int type);

#endif /* defined(__LunarConsole__unity_native_interface__) */

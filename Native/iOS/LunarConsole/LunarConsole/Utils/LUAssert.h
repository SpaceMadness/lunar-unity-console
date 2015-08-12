//
//  LUAssert.h
//  LunarConsole
//
//  Created by Alex Lementuev on 8/5/15.
//  Copyright © 2015 Space Madness. All rights reserved.
//

#import <Foundation/Foundation.h>

#define LUAssert(expression) if (!(expression)) __lunar_assert(#expression, __FILE__, __LINE__, __FUNCTION__)
#define LUAssertMsg(expression, msg) if (!(expression)) __lunar_assert_msg(#expression, __FILE__, __LINE__, __FUNCTION__, (msg))
#define LUAssertMsgv(expression, msg, ...) if (!(expression)) __lunar_assert_msgv(#expression, __FILE__, __LINE__, __FUNCTION__, (msg), __VA_ARGS__)

void __lunar_assert(const char* expression, const char* file, int line, const char* function);
void __lunar_assert_msg(const char* expression, const char* file, int line, const char* function, NSString *message);
void __lunar_assert_msgv(const char* expression, const char* file, int line, const char* function, NSString *format, ...);
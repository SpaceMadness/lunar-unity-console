//
//  LUAssert.m
//  LunarConsole
//
//  Created by Alex Lementuev on 8/5/15.
//  Copyright © 2015 Space Madness. All rights reserved.
//

#import "LUAssert.h"

#import "Lunar.h"

void __lunar_assert(const char* expression, const char* file, int line, const char* function)
{
    __lunar_assert_msg(expression, file, line, function, @"");
}

void __lunar_assert_msg(const char* expression, const char* file, int line, const char* function, NSString *message)
{
    __lunar_assert_msgv(expression, file, line, function, message);
}

void __lunar_assert_msgv(const char* expressionCStr, const char* fileCStr, int line, const char* functionCStr, NSString *format, ...)
{
    va_list ap;
    va_start(ap, format);
    
    NSString *message = [[NSString alloc] initWithFormat:format arguments:ap];
    NSString *consoleMessage = [[NSString alloc] initWithFormat:@"LUNAR/ASSERT: (%s) in %s:%d %s message:'%@'",
                                expressionCStr, fileCStr, line, functionCStr, message];
    
    NSLog(@"%@", consoleMessage);
    
    LU_RELEASE(message);
    LU_RELEASE(consoleMessage);
    
    va_end(ap);
}
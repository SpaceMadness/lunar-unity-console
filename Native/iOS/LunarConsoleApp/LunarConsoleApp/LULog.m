//
//  LULog.m
//  LunarConsoleApp
//
//  Created by Alex Lementuev on 3/28/19.
//  Copyright © 2019 Space Madness. All rights reserved.
//

#import "LULog.h"

void LULog(NSString *format, ...)
{
	va_list ap;
	va_start(ap, format);
	NSString *message = [[NSString alloc] initWithFormat:format arguments:ap];
	NSLog(@"[LUNAR] %@", message);
	va_end(ap);
}

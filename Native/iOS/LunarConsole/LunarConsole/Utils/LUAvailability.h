//
//  LUAvailability.h
//  LunarConsole
//
//  Created by Alex Lementuev on 8/8/15.
//  Copyright (c) 2015 Space Madness. All rights reserved.
//

#import <Foundation/Foundation.h>

#ifndef __IPHONE_7_0
#define __IPHONE_7_0     70000
#endif

#ifndef __IPHONE_8_0
#define __IPHONE_8_0     80000
#endif

#ifndef __IPHONE_9_0
#define __IPHONE_9_0     90000
#endif

#define LU_SYSTEM_VERSION_MIN __IPHONE_7_0

#define LU_IOS_VERSION_AVAILABLE(sys_ver) lunar_ios_version_available(sys_ver)
#define LU_IOS_MIN_VERSION_AVAILABLE (LU_IOS_VERSION_AVAILABLE(LU_SYSTEM_VERSION_MIN))
#define LU_SELECTOR_AVAILABLE(obj, sel) [(obj) respondsToSelector:@selector(sel)]
#define LU_CLASS_AVAILABLE(className) (NSClassFromString(@#className) != nil)

typedef NSUInteger LUSystemVersion;

BOOL lunar_ios_version_available(LUSystemVersion version);
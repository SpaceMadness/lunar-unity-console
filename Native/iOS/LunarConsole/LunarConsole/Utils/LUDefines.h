//
//  LunarDefines.h
//  LunarConsole
//
//  Created by Alex Lementuev on 8/4/15.
//  Copyright (c) 2015 Space Madness. All rights reserved.
//

#define LU_SHOULD_IMPLEMENT_METHOD \
    NSLog(@"%@ should implement %@ method", NSStringFromClass([self class]), NSStringFromSelector(_cmd));

#if __has_feature(objc_arc)
    #define LU_WEAK __weak
    #define LU_RETAIN(obj) obj
    #define LU_RELEASE(obj)
    #define LU_AUTORELEASE(obj) obj
    #define LU_SUPER_DEALLOC
#else
    #define LU_WEAK
    #define LU_RETAIN(obj) [(obj) retain]
    #define LU_RELEASE(obj) [(obj) release]
    #define LU_AUTORELEASE(obj) [(obj) autorelease]
    #define LU_SUPER_DEALLOC [super dealloc];
#endif
//
//  LUStacktraceTest.m
//  LunarConsole
//
//  Created by Alex Lementuev on 11/23/15.
//  Copyright © 2015 Space Madness. All rights reserved.
//

#import <XCTest/XCTest.h>

#import "LUStacktrace.h"

@interface LUStacktraceTest : XCTestCase

@end

@implementation LUStacktraceTest

- (void)testOptimize
{
    NSString *stackTrace = @"UnityEngine.Debug:LogError(Object)\n" \
            "Test:Method(String) (at /Users/lunar-unity-console/Project/Assets/Scripts/Test.cs:30)\n" \
            "<LogMessages>c__Iterator0:MoveNext() (at /Users/lunar-unity-console/Project/Assets/Logger.cs:85)\n" \
            "UnityEngine.MonoBehaviour:StartCoroutine(IEnumerator)\n" \
            "Logger:LogMessages() (at /Users/lunar-unity-console/Project/Assets/Logger.cs:66)\n" \
            "UnityEngine.EventSystems.EventSystem:Update()";
    
    NSString *expected = @"UnityEngine.Debug:LogError(Object)\n" \
            "Test:Method(String) (at Assets/Scripts/Test.cs:30)\n" \
            "<LogMessages>c__Iterator0:MoveNext() (at Assets/Logger.cs:85)\n" \
            "UnityEngine.MonoBehaviour:StartCoroutine(IEnumerator)\n" \
            "Logger:LogMessages() (at Assets/Logger.cs:66)\n" \
            "UnityEngine.EventSystems.EventSystem:Update()";
    
    NSString *actual = [LUStacktrace optimizeStacktrace:stackTrace];
    XCTAssertEqualObjects(expected, actual);
}

@end

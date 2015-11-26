//
//  LUStackTrace.m
//  LunarConsole
//
//  Created by Alex Lementuev on 11/23/15.
//  Copyright © 2015 Space Madness. All rights reserved.
//

#import "LUStacktrace.h"

static NSString * const MARKER_AT = @" (at ";
static NSString * const MARKER_ASSETS = @"/Assets/";

@implementation LUStacktrace

+ (NSString *)optimizeStacktrace:(NSString *)stacktrace
{
    if (stacktrace.length > 0)
    {
        NSArray *lines = [stacktrace componentsSeparatedByString:@"\n"];
        NSMutableArray *newLines = [NSMutableArray arrayWithCapacity:lines.count];
        for (NSString *line in lines)
        {
            [newLines addObject:[self optimizeLine:line]];
        }
        
        return [newLines componentsJoinedByString:@"\n"];
    }
    
    return nil;
}
             
+ (NSString *)optimizeLine:(NSString *)line
{
    NSRange startRange = [line rangeOfString:MARKER_AT];
    if (startRange.location == NSNotFound) return line;
    
    NSRange endRange = [line rangeOfString:MARKER_ASSETS options:NSBackwardsSearch];
    if (endRange.location == NSNotFound) return line;
    
    NSString *s1 = [line substringWithRange:NSMakeRange(0, startRange.location + startRange.length)];
    NSString *s2 = [line substringFromIndex:endRange.location + 1];
    
    return [s1 stringByAppendingString:s2];
}

@end

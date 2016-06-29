//
//  XCTestCase+Operations.m
//  LunarConsoleApp
//
//  Created by Alex Lementuev on 3/24/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "XCTestCase+Operations.h"

@implementation XCTestCase (Operations)

- (void)waitUntilElementExists:(XCUIElement *)element timeout:(NSTimeInterval)timeout
{
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"exists == 1"];
    [self expectationForPredicate:predicate evaluatedWithObject:element handler:nil];
    [self waitForExpectationsWithTimeout:timeout handler:nil];
}

@end

//
//  XCTestCase+Operations.h
//  LunarConsoleApp
//
//  Created by Alex Lementuev on 3/24/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import <XCTest/XCTest.h>

@interface XCTestCase (Operations)

- (void)waitUntilElementExists:(XCUIElement *)element timeout:(NSTimeInterval)timeout;

@end

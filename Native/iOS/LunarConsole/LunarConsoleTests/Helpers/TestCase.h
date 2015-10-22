//
//  TestCase.h
//  LunarConsole
//
//  Created by Alex Lementuev on 10/22/15.
//  Copyright (c) 2015 Space Madness. All rights reserved.
//

#import <XCTest/XCTest.h>

#import "Lunar.h"

@interface TestCase : XCTestCase

@property (nonatomic, readonly) NSMutableArray * result;

- (void)addResult:(id)obj;
- (void)assertResult:(id)first, ...;

@end

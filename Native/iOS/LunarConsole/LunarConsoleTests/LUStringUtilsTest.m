//
//  LUStringUtilsTest.m
//  LunarConsole
//
//  Created by Alex Lementuev on 4/20/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import <XCTest/XCTest.h>

#import "Lunar.h"

inline static BOOL isEpsilonEqual(float a, float b)
{
    return fabsf(a - b) < 0.00001f;
}

@interface LUStringUtilsTest : XCTestCase

@end

@implementation LUStringUtilsTest

#pragma mark -
#pragma mark Integers

- (void)testParseInteger
{
    NSInteger result = 0;
    XCTAssertTrue(LUStringTryParseInteger(@"123456", &result));
    XCTAssertEqual(123456, result);
}

- (void)testParseNegativeInteger
{
    NSInteger result = 0;
    XCTAssertTrue(LUStringTryParseInteger(@"-123456", &result));
    XCTAssertEqual(-123456, result);
}

- (void)testParseInvalidInteger
{
    NSInteger result = 0;
    XCTAssertFalse(LUStringTryParseInteger(@"x123456", &result));
    XCTAssertFalse(LUStringTryParseInteger(@"123x456", &result));
    XCTAssertFalse(LUStringTryParseInteger(@"123456x", &result));
    XCTAssertFalse(LUStringTryParseInteger(@"", &result));
}

- (void)testParseFloatAsInteger
{
    NSInteger result = 0;
    XCTAssertFalse(LUStringTryParseInteger(@"3.14", &result));
}

#pragma mark -
#pragma mark Floats

- (void)testParseFloat
{
    float result = 0;
    XCTAssertTrue(LUStringTryParseFloat(@"123.456", &result));
    XCTAssertTrue(isEpsilonEqual(123.456, result));
}

- (void)testParseNegativeFloat
{
    float result = 0;
    XCTAssertTrue(LUStringTryParseFloat(@"-123.456", &result));
    XCTAssertTrue(isEpsilonEqual(-123.456, result));
}

- (void)testParseInvalidFloat
{
    NSInteger result = 0;
    XCTAssertFalse(LUStringTryParseInteger(@"x123.456", &result));
    XCTAssertFalse(LUStringTryParseInteger(@"12.3x456", &result));
    XCTAssertFalse(LUStringTryParseInteger(@"123.456x", &result));
    XCTAssertFalse(LUStringTryParseInteger(@"", &result));
}

#pragma mark -
#pragma mark Serialization

- (void)testDictionaryToStringSerialization
{
    NSDictionary *dict = @{
       @"key1" : @"value",
       @"key2" : @"value with whitespace",
       @"key3" : @"value with\nlinebreak",
       @"key4" : @"value with \"quotes\"",
       @"key5" : @"value with: separator",
       @"key6" : @""
    };
    
    NSString *expected = @"key3:value with\\nlinebreak\nkey1:value\nkey6:\nkey4:value with \"quotes\"\nkey2:value with whitespace\nkey5:value with: separator";
    NSString *actual = LUSerializeDictionaryToString(dict);
    
    XCTAssertEqualObjects(expected, actual);
}

@end

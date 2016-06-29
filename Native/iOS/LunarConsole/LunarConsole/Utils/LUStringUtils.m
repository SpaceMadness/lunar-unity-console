//
//  LUStringUtils.m
//  LunarConsole
//
//  Created by Alex Lementuev on 4/20/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "LUStringUtils.h"

#import "Lunar.h"

static inline NSNumber *LUNumberFromString(NSString *str)
{
    NSNumberFormatter *numberFormatter = [NSNumberFormatter new];
    numberFormatter.numberStyle = NSNumberFormatterDecimalStyle;
    NSNumber *number = [numberFormatter numberFromString:str];
    LU_RELEASE(numberFormatter);
    
    return number;
}

BOOL LUStringTryParseInteger(NSString *str, NSInteger *outResult)
{
    NSNumber *number = LUNumberFromString(str);
    if (number)
    {
        // FIXME: find a better way of telling if number is integer
        NSString *decimalSeparator = [[NSLocale currentLocale] objectForKey:NSLocaleDecimalSeparator];
        if ([str rangeOfString:decimalSeparator].location == NSNotFound)
        {
            if (outResult) *outResult = number.integerValue;
            return YES;
        }
    }
    
    return NO;
}

BOOL LUStringTryParseFloat(NSString *str, float *outResult)
{
    NSNumber *number = LUNumberFromString(str);
    if (number)
    {
        if (outResult) *outResult = number.floatValue;
        return YES;
    }
    
    return NO;
}

NSString *LUSerializeDictionaryToString(NSDictionary *data)
{
    NSMutableString *result = [NSMutableString string];
    NSInteger index = 0;
    for (id key in data)
    {
        id value = [[data objectForKey:key] description];
        value = [value stringByReplacingOccurrencesOfString:@"\n" withString:@"\\n"]; // we use new lines as separators
        [result appendFormat:@"%@:%@", key, value];
        if (++index < data.count)
        {
            [result appendString:@"\n"];
        }
    }
    
    return result;
}
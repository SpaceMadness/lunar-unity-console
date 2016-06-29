//
//  LUStringUtils.h
//  LunarConsole
//
//  Created by Alex Lementuev on 4/20/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import <Foundation/Foundation.h>

BOOL LUStringTryParseInteger(NSString *str, NSInteger *outResult);
BOOL LUStringTryParseFloat(NSString *str, float *outResult);
NSString *LUSerializeDictionaryToString(NSDictionary *data);
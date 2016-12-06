//
//  LUUtils.h
//  LunarConsole
//
//  Created by Alex Lementuev on 11/26/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "LUAssert.h"
#import "LUAvailability.h"
#import "LUDefines.h"
#import "LUFileUtils.h"
#import "LUImageUtils.h"
#import "LULittleHelper.h"
#import "LUMutableArray.h"
#import "LUObject.h"
#import "LUSerializableObject.h"
#import "LUSerializationUtils.h"
#import "LUSortedList.h"
#import "LUStacktrace.h"
#import "LUStringUtils.h"
#import "LUThreading.h"

LU_INLINE BOOL
LUFloatApprox(float a, float b)
{
    return fabsf(a - b) < 0.00001;
}

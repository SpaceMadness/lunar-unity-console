//
//  LunarThreading.m
//  LunarConsole
//
//  Created by Alex Lementuev on 8/5/15.
//  Copyright © 2015 Space Madness. All rights reserved.
//

#import "LUThreading.h"

#import "LUAssert.h"

void lunar_dispatch_main(dispatch_block_t block)
{
    LUAssert(block != nil);
    if (block != nil)
    {
        if ([NSThread isMainThread])
        {
            block();
        }
        else
        {
            dispatch_async(dispatch_get_main_queue(), block);
        }
    }
}
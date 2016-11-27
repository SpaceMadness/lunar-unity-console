//
//  LUAction.m
//  LunarConsole
//
//  Created by Alex Lementuev on 2/24/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "Lunar.h"

#import "LUAction.h"

@implementation LUAction

+ (instancetype)actionWithId:(int)actionId name:(NSString *)name
{
    return [[self alloc] initWithId:actionId name:name];
}

@end

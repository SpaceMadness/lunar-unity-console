//
//  LUAction.h
//  LunarConsole
//
//  Created by Alex Lementuev on 2/24/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "LUEntry.h"

@interface LUAction : LUEntry

+ (instancetype)actionWithId:(int)actionId name:(NSString *)name;

@end

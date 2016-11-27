//
//  LUCVarFactory.h
//  LunarConsole
//
//  Created by Alex Lementuev on 4/7/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "LUCVar.h"

@interface LUCVarFactory : NSObject

+ (LUCVar *)variableWithId:(int)entryId name:(NSString *)name value:(NSString *)value type:(LUCVarType)type;

@end

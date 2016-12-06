//
//  LUSerializableObject.h
//  LunarConsole
//
//  Created by Alex Lementuev on 12/5/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface LUSerializableObject : NSObject

+ (instancetype)loadFromFile:(NSString *)filename;
- (BOOL)save;

@end

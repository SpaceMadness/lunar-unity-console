//
//  LUSerializableObject+Inheritance.h
//  LunarConsole
//
//  Created by Alex Lementuev on 12/28/18.
//  Copyright © 2018 Space Madness. All rights reserved.
//

#import "LUSerializableObject.h"

NS_ASSUME_NONNULL_BEGIN

@interface LUSerializableObject (Inheritance)

- (BOOL)decodeBool:(NSCoder *)coder forKey:(NSString *)key defaultValue:(BOOL)defaultValue;
- (double)decodeDouble:(NSCoder *)coder forKey:(NSString *)key defaultValue:(double)defaultValue;
- (NSInteger)decodeInteger:(NSCoder *)coder forKey:(NSString *)key defaultValue:(NSInteger)defaultValue;

@end

NS_ASSUME_NONNULL_END

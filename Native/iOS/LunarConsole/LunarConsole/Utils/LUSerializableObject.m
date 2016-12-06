//
//  LUSerializableObject.m
//  LunarConsole
//
//  Created by Alex Lementuev on 12/5/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "LUSerializableObject.h"

#import "Lunar.h"

@interface LUSerializableObject () <NSCoding>
{
    NSString * _filename;
}

@end

@implementation LUSerializableObject

+ (instancetype)loadFromFile:(NSString *)filename
{
    id object = LUDeserializeObject(filename);
    if (object != nil)
    {
        [object setFilename:filename];
        return object;
    }
    
    return [[self alloc] initWithFilename:filename];
}

- (instancetype)initWithFilename:(NSString *)filename
{
    self = [super init];
    if (self)
    {
        _filename = filename;
        [self initDefaults];
    }
    return self;
}

#pragma mark -
#pragma mark NSCoding

- (instancetype)initWithCoder:(NSCoder *)aDecoder
{
    self = [super init];
    if (self)
    {
        [self initDefaults];
        
        NSInteger version = [aDecoder decodeIntegerForKey:@"version"];
        if (version == [[self class] version])
        {
            [self deserializeWithDecoder:aDecoder];
        }
    }
    return self;
}

- (void)encodeWithCoder:(NSCoder *)aCoder
{
    [aCoder encodeInteger:[[self class] version] forKey:@"version"];
    [self serializeWithCoder:aCoder];
}

#pragma mark -
#pragma mark Save

- (BOOL)save
{
    return LUSerializeObject(self, _filename);
}

#pragma mark -
#pragma mark Inheritance

- (void)initDefaults
{
}

- (void)serializeWithCoder:(NSCoder *)coder
{
}

- (void)deserializeWithDecoder:(NSCoder *)decoder
{
}

#pragma mark -
#pragma mark Getters/Setters

- (void)setFilename:(NSString *)filename
{
    _filename = filename;
}

@end

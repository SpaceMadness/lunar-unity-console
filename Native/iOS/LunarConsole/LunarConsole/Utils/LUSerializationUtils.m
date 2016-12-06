//
//  LUSerializationUtils.m
//  LunarConsole
//
//  Created by Alex Lementuev on 12/5/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "LUSerializationUtils.h"

#import "Lunar.h"

BOOL LUSerializeObject(id object, NSString *filename)
{
    NSData *data = [NSKeyedArchiver archivedDataWithRootObject:object];
    if (data == nil)
    {
        return NO;
    }
    
    NSString *path = LUGetDocumentsFile(filename);
    [data writeToFile:path atomically:YES];
    
    return YES;
}

id LUDeserializeObject(NSString *filename)
{
    NSString *path = LUGetDocumentsFile(filename);
    NSData *data = [NSData dataWithContentsOfFile:path];
    if (data == nil)
    {
        return nil;
    }
    
    return [NSKeyedUnarchiver unarchiveObjectWithData:data];
}

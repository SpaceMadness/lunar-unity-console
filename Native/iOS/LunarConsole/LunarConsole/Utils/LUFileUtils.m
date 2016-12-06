//
//  LUFileUtils.m
//  LunarConsole
//
//  Created by Alex Lementuev on 12/5/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "LUFileUtils.h"

NSString *LUGetDocumentsDir(BOOL createIfNeccessary)
{
    NSArray *searchPaths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, createIfNeccessary);
    return [searchPaths objectAtIndex:0];
}

NSString *LUGetDocumentsFile(NSString *name, BOOL createIfNeccessary)
{
    NSString *documentsDir = LUGetDocumentsDir(createIfNeccessary);
    return [documentsDir stringByAppendingPathComponent:name];
}

BOOL LUFileExists(NSString *path)
{
    return path != nil && [[NSFileManager defaultManager] fileExistsAtPath:path];
}

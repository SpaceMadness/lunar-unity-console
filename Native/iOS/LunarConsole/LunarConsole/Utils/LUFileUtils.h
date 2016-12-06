//
//  LUFileUtils.h
//  LunarConsole
//
//  Created by Alex Lementuev on 12/5/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import <Foundation/Foundation.h>

NSString *LUGetDocumentsDir(BOOL createIfNeccessary);
NSString *LUGetDocumentsFile(NSString *name, BOOL createIfNeccessary);

BOOL LUFileExists(NSString *path);

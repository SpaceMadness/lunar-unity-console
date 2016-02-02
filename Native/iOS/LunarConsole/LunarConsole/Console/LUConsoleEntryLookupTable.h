//
//  LUConsoleEntryLookupTable.h
//  LunarConsole
//
//  Created by Alex Lementuev on 1/25/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import <Foundation/Foundation.h>

@class LUConsoleEntry;
@class LUConsoleCollapsedEntry;

@interface LUConsoleEntryLookupTable : NSObject

- (LUConsoleCollapsedEntry *)addEntry:(LUConsoleEntry *)entry;
- (void)removeEntry:(LUConsoleCollapsedEntry *)entry;
- (void)clear;

@end

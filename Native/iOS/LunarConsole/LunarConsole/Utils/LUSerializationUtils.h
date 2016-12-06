//
//  LUSerializationUtils.h
//  LunarConsole
//
//  Created by Alex Lementuev on 12/5/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import <Foundation/Foundation.h>

BOOL LUSerializeObject(id object, NSString *filename);
id LUDeserializeObject(NSString *filename);

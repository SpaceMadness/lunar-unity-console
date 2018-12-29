//
//  LUSerializationUtils.m
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2018 Alex Lementuev, SpaceMadness.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

#import "LUSerializationUtils.h"

#import "Lunar.h"

BOOL LUSerializeObject(id object, NSString *path)
{
	if (path.length == 0)
	{
		return NO;
	}
	
    NSData *data = [NSKeyedArchiver archivedDataWithRootObject:object];
    if (data == nil)
    {
        return NO;
    }
    
    return [data writeToFile:path atomically:YES];
}

id LUDeserializeObject(NSString *path)
{
	if (path.length == 0)
	{
		return nil;
	}
	
    NSData *data = [NSData dataWithContentsOfFile:path];
    if (data == nil)
    {
        return nil;
    }
    
    return [NSKeyedUnarchiver unarchiveObjectWithData:data];
}

id LUDecodeJson(NSString *json)
{
	if (json == nil) {
		return nil;
	}
	
	NSData *data = [json dataUsingEncoding:NSUTF8StringEncoding];
	return [NSJSONSerialization JSONObjectWithData:data options:0 error:nil];
}

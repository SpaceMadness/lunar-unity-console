//
//  LULittleHelper.m
//  LunarConsole
//
//  Created by Alex Lementuev on 11/12/15.
//  Copyright © 2015 Space Madness. All rights reserved.
//

#import "LULittleHelper.h"

#import "Lunar.h"

void LUDisplayAlertView(NSString *title, NSString *message)
{
    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:title
                                                        message:message
                                                       delegate:nil
                                              cancelButtonTitle:@"OK"
                                              otherButtonTitles:nil];
    [alertView show];
    LU_RELEASE(alertView);
}
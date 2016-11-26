//
//  LUImageUtils.c
//  LunarConsole
//
//  Created by Alex Lementuev on 11/26/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "LUImageUtils.h"
#import "Lunar.h"

UIImage *LUGet3SlicedImage(NSString *name)
{
    UIImage *image = [UIImage imageNamed:name];
    LUAssertMsgv(image != nil, @"Can't load image: %@", name);
    LUAssertMsgv(((int)image.size.width) % 3 == 0, @"3 sliced image has wrong width: %g", image.size.width);
    return [image stretchableImageWithLeftCapWidth:image.size.width / 3 topCapHeight:0];
}

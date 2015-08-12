//
//  LULogTypeButton.m
//  LunarConsole
//
//  Created by Alex Lementuev on 8/9/15.
//  Copyright (c) 2015 Space Madness. All rights reserved.
//

#import "LULogTypeButton.h"

#import "Lunar.h"

static const NSUInteger kCountMax = 999;

@interface LULogTypeButton ()
{
    NSString * _initialText;
}

@end

@implementation LULogTypeButton

- (void)dealloc
{
    LU_RELEASE(_initialText);
    LU_SUPER_DEALLOC
}

- (void)awakeFromNib
{
    [super awakeFromNib];
    
    _initialText = LU_RETAIN(self.titleLabel.text);
    _count = INT32_MAX;

    // text color
    [self setTitleColor:[LUTheme mainTheme].logButtonTitleColor forState:UIControlStateNormal];
    [self setTitleColor:[LUTheme mainTheme].logButtonTitleSelectedColor forState:UIControlStateSelected];
    
    // images
    UIImage *normalImage = [self imageForState:UIControlStateNormal];
    UIImage *selectedImage = [self image:normalImage changeAlpha:0.1];
    [self setImage:selectedImage forState:UIControlStateSelected];
    
    self.count = 0;
    self.on = NO;
}

- (void)setCount:(NSUInteger)count
{
    if (_count != count)
    {
        if (count < kCountMax)
        {
            NSString *countText = [[NSString alloc] initWithFormat:@"%ld", (unsigned long)count];
            [self setCountText:countText];
            LU_RELEASE(countText);
        }
        else if (_count < kCountMax)
        {
            [self setCountText:_initialText];
        }
        
        _count = count;
    }
}

#pragma mark -
#pragma mark Image Helpers

- (UIImage *)image:(UIImage *)image changeAlpha:(CGFloat)alpha
{
    UIGraphicsBeginImageContextWithOptions(image.size, NO, image.scale);
    
    CGContextRef ctx = UIGraphicsGetCurrentContext();
    CGRect area = CGRectMake(0, 0, image.size.width, image.size.height);
    
    CGContextScaleCTM(ctx, 1, -1);
    CGContextTranslateCTM(ctx, 0, -area.size.height);
    
    CGContextSetBlendMode(ctx, kCGBlendModeMultiply);
    
    CGContextSetAlpha(ctx, alpha);
    
    CGContextDrawImage(ctx, area, image.CGImage);
    
    UIImage *result = UIGraphicsGetImageFromCurrentImageContext();
    
    UIGraphicsEndImageContext();
    
    return result;
}

#pragma mark -
#pragma mark Properties

- (void)setCountText:(NSString *)text
{
    [self setTitle:text forState:UIControlStateNormal];
    [self setTitle:text forState:UIControlStateSelected];
}

@end

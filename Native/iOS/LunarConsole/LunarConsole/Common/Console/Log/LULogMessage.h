//
//  LULogMessage.h
//  LunarConsole
//
//  Created by Alex Lementuev on 5/14/20.
//  Copyright © 2020 Space Madness. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@class LUAttributedTextSkin;

@interface LURichTextTag : NSObject

@property (nonatomic, readonly) NSRange range;

- (instancetype)initWithRange:(NSRange)range;

@end

typedef enum : NSUInteger {
    LURichTextStyleBold,
    LURichTextStyleItalic
} LURichTextStyle;

@interface LURichTextStyleTag : LURichTextTag

@property (nonatomic, readonly) LURichTextStyle style;

- (instancetype)initWithStyle:(LURichTextStyle)style range:(NSRange)range;

@end

@interface LURichTextColorTag : LURichTextTag

@property (nonatomic, readonly) NSUInteger color;

- (instancetype)initWithColor:(NSUInteger)color range:(NSRange)range;

@end

@interface LULogMessage : NSObject

@property (nonatomic, readonly, nullable) NSString *text;
@property (nonatomic, readonly, nullable) NSArray<LURichTextTag *> *tags;
@property (nonatomic, readonly) NSUInteger length;

- (instancetype)initWithText:(nullable NSString *)text tags:(NSArray<LURichTextTag *> * _Nullable)tags;

+ (instancetype)fromRichText:(nullable NSString *)text;

@end

@interface LULogMessage (AttributedString)

- (NSAttributedString *)createAttributedTextWithSkin:(LUAttributedTextSkin *)skin;

@end

NS_ASSUME_NONNULL_END

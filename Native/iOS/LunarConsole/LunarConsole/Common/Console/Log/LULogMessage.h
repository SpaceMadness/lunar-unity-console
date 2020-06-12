//
//  LULogMessage.h
//  LunarConsole
//
//  Created by Alex Lementuev on 5/14/20.
//  Copyright © 2020 Space Madness. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

typedef enum : NSUInteger {
    LURichTextTagFlagBold   = 0x01,
    LURichTextTagFlagItalic = 0x02,
    LURichTextTagFlagColor  = 0x04,
} LURichTextTagFlags;

@interface LURichTextTag : NSObject

@property (nonatomic, readonly) LURichTextTagFlags flags;
@property (nonatomic, readonly, nullable) NSString *attribute;
@property (nonatomic, readonly) NSRange range;

- (instancetype)initWithFlags:(LURichTextTagFlags)flags attribute:(NSString * _Nullable)attribute range:(NSRange)range;

@end

typedef enum : NSUInteger {
    LURichTextStyleBold,
    LURichTextStyleItalic
} LURichTextStyle;

@interface LURichTextStyleTag : NSObject

@property (nonatomic, readonly) LURichTextStyle style;
@property (nonatomic, readonly) NSRange range;

- (instancetype)initWithStyle:(LURichTextStyle)style range:(NSRange)range;

@end

@interface LULogMessage : NSObject

@property (nonatomic, readonly, nullable) NSString *text;
@property (nonatomic, readonly, nullable) NSArray<LURichTextTag *> *tags;
@property (nonatomic, readonly) NSUInteger length;

- (instancetype)initWithText:(nullable NSString *)text tags:(NSArray<LURichTextTag *> * _Nullable)tags;

+ (instancetype)fromRichText:(nullable NSString *)text;

@end

NS_ASSUME_NONNULL_END

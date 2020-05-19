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
    LURichTextTagTypeBold,
    LURichTextTagTypeItalic,
    LURichTextTagTypeColor,
} LURichTextTagType;

@interface LURichTextTag : NSObject

@property (nonatomic, readonly) LURichTextTagType type;
@property (nonatomic, readonly, nullable) NSString *attribute;
@property (nonatomic, readonly) NSRange range;

- (instancetype)initWithType:(LURichTextTagType)type attribute:(NSString * _Nullable)attribute range:(NSRange)range;

@end

@interface LULogMessage : NSObject

@property (nonatomic, readonly, nullable) NSString *text;
@property (nonatomic, readonly, nullable) NSArray<LURichTextTag *> *tags;
@property (nonatomic, readonly) NSUInteger length;

- (instancetype)initWithText:(nullable NSString *)text tags:(NSArray<LURichTextTag *> * _Nullable)tags;

+ (instancetype)fromRichText:(nullable NSString *)text;

@end

NS_ASSUME_NONNULL_END

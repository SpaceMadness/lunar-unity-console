//
//  LULogMessage.h
//  LunarConsole
//
//  Created by Alex Lementuev on 5/14/20.
//  Copyright © 2020 Space Madness. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface LULogMessage : NSObject

@property (nonatomic, readonly, nullable) NSString *text;
@property (nonatomic, readonly, nullable) NSAttributedString *attributedText;
@property (nonatomic, readonly) NSUInteger length;

- (instancetype)initWithText:(nullable NSString *)text attributedText:(nullable NSAttributedString *)attributedText;

+ (instancetype)fromRichText:(nullable NSString *)text;

@end

NS_ASSUME_NONNULL_END

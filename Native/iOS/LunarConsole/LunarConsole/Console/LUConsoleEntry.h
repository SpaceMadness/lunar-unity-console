//
//  LunarLogEntry.h
//  LunarConsole
//
//  Created by Alex Lementuev on 8/3/15.
//  Copyright (c) 2015 Space Madness. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef enum : uint8_t {
    LUConsoleLogTypeError,
    LUConsoleLogTypeAssert,
    LUConsoleLogTypeWarning,
    LUConsoleLogTypeLog,
    LUConsoleLogTypeException
} LUConsoleLogType;

typedef uint8_t LUConsoleLogTypeMask;

#define LU_CONSOLE_LOG_TYPE_COUNT 5
#define LU_CONSOLE_LOG_TYPE_MASK(TYPE)     (1 << (TYPE))

#define LU_IS_CONSOLE_LOG_TYPE_VALID(TYPE) ((TYPE) >= 0 && (TYPE) < LU_CONSOLE_LOG_TYPE_COUNT)
#define LU_IS_CONSOLE_LOG_TYPE_ERROR(TYPE) ((TYPE) == LUConsoleLogTypeException || \
                                            (TYPE) == LUConsoleLogTypeError || \
                                            (TYPE) == LUConsoleLogTypeAssert)

@interface LUConsoleEntry : NSObject

@property (nonatomic, readonly) LUConsoleLogType type;
@property (nonatomic, readonly) NSString * message;
@property (nonatomic, readonly) NSString * stackTrace;

+ (instancetype)entryWithType:(LUConsoleLogType)type message:(NSString *)message stackTrace:(NSString *)stackTrace;
- (instancetype)initWithType:(LUConsoleLogType)type message:(NSString *)message stackTrace:(NSString *)stackTrace;

- (UITableViewCell *)tableView:(UITableView *)tableView cellAtIndex:(NSUInteger)index;
- (CGSize)cellSizeForTableView:(UITableView *)tableView;

@end

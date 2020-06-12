//
//  LULogMessage.m
//  LunarConsole
//
//  Created by Alex Lementuev on 5/14/20.
//  Copyright © 2020 Space Madness. All rights reserved.
//

#import "LULogMessage.h"

@interface LURichTextTagInfo : NSObject

@property (nonatomic, readonly) NSString *name;
@property (nonatomic, readonly) NSString *attribute;
@property (nonatomic, readonly, getter=isOpen) BOOL open;
@property (nonatomic, readonly) NSUInteger position;

- (instancetype)initWithName:(NSString *)name attribute:(NSString *)attributes open:(BOOL)open position:(NSUInteger)position;

@end

@implementation LURichTextTagInfo

- (instancetype)initWithName:(NSString *)name attribute:(NSString *)attribute open:(BOOL)open position:(NSUInteger)position
{
    self = [super init];
    if (self)
    {
        _name = name;
        _attribute = attribute;
        _open = open;
        _position = position;
    }
    return self;
}

@end

static inline BOOL _isvalidTagName(NSString *name)
{
    return [name isEqualToString:@"b"] || [name isEqualToString:@"i"] || [name isEqualToString:@"color"];
}

static LURichTextTagInfo * _tryCaptureTag(NSString *str, NSUInteger position, NSUInteger* iterPtr)
{
    NSUInteger end = *iterPtr;
    BOOL isOpen = YES;
    if (end < str.length && [str characterAtIndex:end] == '/')
    {
        isOpen = NO;
        ++end;
    }
    
    NSUInteger start = end;
    BOOL found = NO;
    while (end < str.length)
    {
        unichar chr = [str characterAtIndex:end++];
        if (chr == '>')
        {
            found = YES;
            break;
        }
    }
    
    if (!found)
    {
        return nil;
    }
    
    NSString *capture = [str substringWithRange:NSMakeRange(start, end - 1 - start)];
    NSArray<NSString *> *tokens = [capture componentsSeparatedByString:@"="];
    if (tokens.count != 1 && tokens.count != 2)
    {
        return nil;
    }
    
    NSString *name = tokens[0];
    if (!_isvalidTagName(name))
    {
        return nil;
    }
    
    NSString *attribute = tokens.count > 1 ? tokens[1] : nil;
    
    *iterPtr = end;
    return [[LURichTextTagInfo alloc] initWithName:name attribute:attribute open:isOpen position:position];
}

@implementation LURichTextTag

- (instancetype)initWithFlags:(LURichTextTagFlags)flags attribute:(NSString * _Nullable)attribute range:(NSRange)range
{
    self = [super init];
    if (self)
    {
        _flags = flags;
        _attribute = attribute;
        _range = range;
    }
    return self;
}

#pragma mark -
#pragma mark Equality

- (BOOL)isEqual:(id)object
{
    if ([object isKindOfClass:[self class]]) {
        LURichTextTag *other = object;
        return _flags == other.flags &&
        ((_attribute == nil && other.attribute == nil) || [_attribute isEqualToString:other.attribute]) &&
        NSEqualRanges(_range, other.range);
    }
    
    return NO;
}

#pragma mark -
#pragma mark Description

- (NSString *)description
{
    return [NSString stringWithFormat:@"flags=%ld attribute=%@ range=%@", _flags, _attribute, NSStringFromRange(_range)];
}

@end

@implementation LURichTextStyleTag

- (instancetype)initWithStyle:(LURichTextStyle)style range:(NSRange)range
{
    self = [super init];
    if (self)
    {
        _style = style;
        _range = range;
    }
    return self;
}

#pragma mark -
#pragma mark Equality

- (BOOL)isEqual:(id)object
{
    if ([object isKindOfClass:[self class]]) {
        LURichTextStyleTag *other = object;
        return _style == other.style && NSEqualRanges(_range, other.range);
    }
    
    return NO;
}

#pragma mark -
#pragma mark Description

- (NSString *)description
{
    return [NSString stringWithFormat:@"style=%ld range=%@", _style, NSStringFromRange(_range)];
}

@end

@implementation LULogMessage

- (instancetype)initWithText:(nullable NSString *)text tags:(NSArray<LURichTextTag *> *)tags
{
    self = [super init];
    if (self)
    {
        _text = text;
        _tags = tags;
    }
    return self;
}

#pragma mark -
#pragma mark Rich Text

+ (instancetype)fromRichText:(NSString *)text
{
    NSMutableArray<LURichTextTag *> *tags = nil;
    NSMutableArray<LURichTextTagInfo *> *stack = nil;
    NSUInteger i = 0;
    
    unichar buffer[text.length];
    NSUInteger bufferSize = 0;
    
    while (i < text.length)
    {
        unichar chr = [text characterAtIndex:i++];
        if (chr == '<')
        {
            LURichTextTagInfo *tag = _tryCaptureTag(text, bufferSize, &i);
            if (tag)
            {
                if (tag.isOpen)
                {
                    if (stack == nil) stack = [NSMutableArray new];
                    [stack addObject:tag];
                }
                else if (stack.count > 0)
                {
                    LURichTextTagInfo *opposingTag = stack.lastObject;
                    [stack removeLastObject];
                    
                    // if tags don't match - just use raw string
                    if (![tag.name isEqualToString:opposingTag.name])
                    {
                        continue;
                    }
                    
                    // create rich text tag
                    NSInteger len = bufferSize - opposingTag.position;
                    if (len > 0)
                    {
                        NSRange range = NSMakeRange(opposingTag.position, len);
                        if (tags == nil) tags = [NSMutableArray new];
                        if ([tag.name isEqualToString:@"b"])
                        {
                            [tags addObject:[[LURichTextTag alloc] initWithFlags:LURichTextTagFlagBold attribute:nil range:range]];
                        }
                        else if ([tag.name isEqualToString:@"i"])
                        {
                            [tags addObject:[[LURichTextTag alloc] initWithFlags:LURichTextTagFlagItalic attribute:nil range:range]];
                        }
                        else if ([tag.name isEqualToString:@"color"])
                        {
                            NSString *color = opposingTag.attribute;
                            if (color != nil)
                            {
                                [tags addObject:[[LURichTextTag alloc] initWithFlags:LURichTextTagFlagColor attribute:color range:range]];
                            }
                        }
                    }
                }
            }
            else
            {
                buffer[bufferSize++] = chr;
            }
        }
        else
        {
            buffer[bufferSize++] = chr;
        }
    }
    
    if (tags && bufferSize > 0)
    {
        return [[self alloc] initWithText:[[NSString alloc] initWithCharacters:buffer length:bufferSize] tags:tags];
    }
    
    if (bufferSize < text.length)
    {
        text = [[NSString alloc] initWithCharacters:buffer length:bufferSize];
    }
    
    return [[self alloc] initWithText:text tags:nil];
}

#pragma mark -
#pragma mark Equality

- (BOOL)isEqual:(id)object
{
    if ([object isKindOfClass:[self class]]) {
        LULogMessage *other = object;
        return ((_text == nil && other.text == nil) || [_text isEqual:other.text]) &&
               ((_tags == nil && other.tags == nil) || [_tags isEqualToArray:other.tags]);
    }
    
    if ([object isKindOfClass:[NSString class]])
    {
        NSString *other = object;
        return [_text isEqualToString:other];
    }

    return false;
}

#pragma mark -
#pragma mark Description

- (NSString *)description
{
    return _text;
}

#pragma mark -
#pragma mark Properties

- (NSUInteger)length
{
    return _text.length;
}

@end

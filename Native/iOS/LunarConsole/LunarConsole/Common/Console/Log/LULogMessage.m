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

static LURichTextTagInfo * _tryCaptureTag(NSString *str, NSUInteger pos, NSUInteger* iterPtr)
{
    NSUInteger iter = *iterPtr;
    BOOL isOpen = YES;
    if (iter < str.length && [str characterAtIndex:iter] == '/')
    {
        isOpen = NO;
        ++iter;
    }
    
    NSUInteger start = iter;
    while (iter < str.length)
    {
        unichar chr = [str characterAtIndex:iter++];
        if (chr == '>')
        {
            break;
        }
    }
    
    NSString *token = [str substringWithRange:NSMakeRange(start, iter - 1 - start)];
    NSArray<NSString *> *tokens = [token componentsSeparatedByString:@"="];
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
    
    *iterPtr = iter;
    return [[LURichTextTagInfo alloc] initWithName:name attribute:attribute open:isOpen position:start];
}

@implementation LULogMessage

- (instancetype)initWithText:(NSString *)text attributedText:(NSAttributedString *)attributedText
{
    self = [super init];
    if (self)
    {
        _text = text;
        _attributedText = attributedText;
    }
    return self;
}

#pragma mark -
#pragma mark Rich Text

+ (instancetype)fromRichText:(NSString *)text
{
    NSMutableDictionary *attributes = nil;
    NSMutableArray<LURichTextTagInfo *> *tags = nil;
    NSMutableString *raw = nil;;
    NSUInteger iter = 0;
    NSUInteger start = 0;
    NSUInteger end = 0;
    while (iter < text.length)
    {
        unichar chr = [text characterAtIndex:iter++];
        if (chr == '<')
        {
            LURichTextTagInfo *tag = _tryCaptureTag(text, end, &iter);
            if (tag)
            {
                // copy string chunk to raw output
                if (raw == nil) raw = [NSMutableString new];
                NSInteger len = end - start;
                if (len > 0) [raw appendString:[text substringWithRange:NSMakeRange(start, len)]];
                start = end = iter;
                
                if (tag.isOpen)
                {
                    if (tags == nil) tags = [NSMutableArray new];
                    [tags addObject:tag];
                }
                else if (tags.count > 0)
                {
                    LURichTextTagInfo *openTag = tags.lastObject;
                    
                    // if tags don't match - just use raw string
                    if (![tag.name isEqualToString:openTag.name])
                    {
                        return [[self alloc] initWithText:text attributedText:nil];
                    }
                    
                    // apply attributes
                }
            }
            else
            {
                ++end;
            }
        }
        else
        {
            ++end;
        }
    }
    
    if (attributes)
    {
        NSInteger len = end - start;
        if (len > 0) [raw appendString:[text substringWithRange:NSMakeRange(start, len)]];
        
        NSAttributedString *attributedString =  [[NSAttributedString alloc] initWithString:raw attributes:attributes];
        return [[self alloc] initWithText:raw attributedText:attributedString];
    }
    
    return [[self alloc] initWithText:text attributedText:nil];
}

#pragma mark -
#pragma mark Equality

- (BOOL)isEqual:(id)object
{
    if ([object isKindOfClass:[self class]]) {
        LULogMessage *other = object;
        return ((_text == nil && other.text == nil) || [_text isEqual:other.text]) &&
               ((_attributedText == nil && other.attributedText == nil) || [_attributedText isEqual:other.attributedText]);
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

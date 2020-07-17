//
//  LULogMessage.m
//  LunarConsole
//
//  Created by Alex Lementuev on 5/14/20.
//  Copyright © 2020 Space Madness. All rights reserved.
//

#import "LULogMessage.h"
#import "LUStringUtils.h"
#import "LUTheme.h"

#import <UIKit/UIKit.h>

static NSUInteger _parseColor(NSString *value);

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

- (instancetype)initWithRange:(NSRange)range {
    self = [super init];
    if (self) {
        _range = range;
    }
    return self;
}

#pragma mark -
#pragma mark Equality

- (BOOL)isEqual:(id)object {
    if ([object isKindOfClass:[self class]]) {
        LURichTextTag *other = object;
        return NSEqualRanges(_range, other.range);
    }
    
    return NO;
}

#pragma mark -
#pragma mark Description

- (NSString *)description {
    return [NSString stringWithFormat:@"range=%@", NSStringFromRange(_range)];
}

@end

@implementation LURichTextStyleTag

- (instancetype)initWithStyle:(LURichTextStyle)style range:(NSRange)range {
    self = [super initWithRange:range];
    if (self) {
        _style = style;
    }
    return self;
}

#pragma mark -
#pragma mark Equality

- (BOOL)isEqual:(id)object {
    if ([object isKindOfClass:[self class]]) {
        LURichTextStyleTag *other = object;
        return _style == other.style && [super isEqual:object];
    }
    
    return NO;
}

#pragma mark -
#pragma mark Description

- (NSString *)description {
    return [NSString stringWithFormat:@"style=%ld range=%@", _style, NSStringFromRange(self.range)];
}

@end

@implementation LURichTextColorTag

- (instancetype)initWithColor:(NSUInteger)color range:(NSRange)range {
    self = [super initWithRange:range];
    if (self) {
        _color = color;
    }
    return self;
}

#pragma mark -
#pragma mark Equality

- (BOOL)isEqual:(id)object {
    if ([object isKindOfClass:[self class]]) {
        LURichTextColorTag *other = object;
        return _color == other.color && [super isEqual:object];
    }
    
    return NO;
}

#pragma mark -
#pragma mark Description

- (NSString *)description {
    return [NSString stringWithFormat:@"color=0x%02lx range=%@", (unsigned long)_color, NSStringFromRange(self.range)];
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
    NSMutableArray<LURichTextTag*> *tags = nil;
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
                            [tags addObject:[[LURichTextStyleTag alloc] initWithStyle:LURichTextStyleBold range:range]];
                        }
                        else if ([tag.name isEqualToString:@"i"])
                        {
                            [tags addObject:[[LURichTextStyleTag alloc] initWithStyle:LURichTextStyleItalic range:range]];
                        }
                        else if ([tag.name isEqualToString:@"color"])
                        {
                            NSString *colorValue = opposingTag.attribute;
                            if (colorValue != nil)
                            {
                                NSUInteger color = _parseColor(colorValue);
                                [tags addObject:[[LURichTextColorTag alloc] initWithColor:color range:range]];
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

- (NSAttributedString *)createAttributedTextWithSkin:(LUAttributedTextSkin *)skin {
    return [self createAttributedTextWithSkin:skin attributes:nil];
}

- (NSAttributedString *)createAttributedTextWithSkin:(LUAttributedTextSkin *)skin attributes:(nullable NSDictionary<NSAttributedStringKey, id> *)attrs {
    if (_tags.count > 0) {
        NSMutableAttributedString *attributedString = [[NSMutableAttributedString alloc] initWithString:_text attributes:attrs];
        [attributedString addAttribute:NSFontAttributeName value:skin.regularFont range:NSMakeRange(0, _text.length)];
        for (LURichTextTag *tag in _tags) {
            if ([tag isKindOfClass:[LURichTextColorTag class]]) {
                [attributedString addAttribute:NSForegroundColorAttributeName value:[UIColor redColor] range:tag.range];
            }
            else if ([tag isKindOfClass:[LURichTextStyleTag class]]) {
                LURichTextStyleTag *styleTag = (LURichTextStyleTag *) tag;
                
                UIFont *font;
                if (styleTag.style == LURichTextStyleBold)
                    font = skin.boldFont;
                else if (styleTag.style == LURichTextStyleItalic)
                    font = skin.italicFont;
                else
                    font = skin.regularFont;
                
                [attributedString addAttribute:NSFontAttributeName value:font range:tag.range];
            }
        }
        return attributedString;
    }
    
    return nil;
}

@end

static NSUInteger _parseColor(NSString *value) {
    if (value == nil) {
        return 0xff00ffff;
    }
    
    if ([value hasPrefix:@"#"]) {
        NSInteger result;
        if (LUStringTryParseHex([value substringFromIndex:1], &result)) {
            return result;
        }
        return 0xff00ffff;
    }
    
    static NSDictionary * colorLookup = nil;
    if (colorLookup == nil) {
        colorLookup = @{
            @"aqua"      : @0x00ffffff,
            @"black"     : @0x000000ff,
            @"blue"      : @0x0000ffff,
            @"brown"     : @0xa52a2aff,
            @"cyan"      : @0x00ffffff,
            @"darkblue"  : @0x0000a0ff,
            @"fuchsia"   : @0xff00ffff,
            @"green"     : @0x008000ff,
            @"grey"      : @0x808080ff,
            @"lightblue" : @0xadd8e6ff,
            @"lime"      : @0x00ff00ff,
            @"magenta"   : @0xff00ffff,
            @"maroon"    : @0x800000ff,
            @"navy"      : @0x000080ff,
            @"olive"     : @0x808000ff,
            @"orange"    : @0xffa500ff,
            @"purple"    : @0x800080ff,
            @"red"       : @0xff0000ff,
            @"silver"    : @0xc0c0c0ff,
            @"teal"      : @0x008080ff,
            @"white"     : @0xffffffff,
            @"yellow"    : @0xffff00ff
        };
    }
    
    NSNumber *colorNumber = colorLookup[value];
    if (colorNumber) {
        return [colorNumber integerValue];
    }
    
    return 0xff00ffff;
}

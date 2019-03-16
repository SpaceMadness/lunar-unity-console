//
//  LUPluginSettings.m
//  LunarConsole
//
//  Created by Alex Lementuev on 3/15/19.
//  Copyright © 2019 Space Madness. All rights reserved.
//

#import "LUPluginSettings.h"

static LUConsoleGesture parseGesture(id value) {
	if ([value isKindOfClass:[NSString class]]) {
		return LUConsoleGestureNone;
	}
	return (LUConsoleGesture) [value intValue];
}

static LUDisplayMode parseDisplayMode(id value) {
	if ([value isKindOfClass:[NSString class]]) {
		return LUDisplayModeNone;
	}
	return (LUDisplayMode) [value intValue];
}

@implementation LUExceptionWarningSettings

- (instancetype)initWithDictionary:(NSDictionary *)dict {
	self = [super init];
	if (self) {
		_displayMode = parseDisplayMode(dict[@"displayMode"]);
	}
	return self;
}

#pragma mark -
#pragma mark NSCoding

- (void)encodeWithCoder:(NSCoder *)coder {
}

- (nullable instancetype)initWithCoder:(NSCoder *)decoder {
	self = [super init];
	if (self) {
	}
	return self;
}

#pragma mark -
#pragma mark Equality

- (BOOL)isEqual:(id)object {
	if (self == object) {
		return YES;
	}
	
	if (![object isKindOfClass:[self class]]) {
		return NO;
	}
	
	LUExceptionWarningSettings *other = object;
	return self.displayMode == other.displayMode;
}

@end

@implementation LUColor

- (instancetype)initWithDictionary:(NSDictionary *)dict {
	self = [super init];
	if (self) {
		_r = (UInt8) [dict[@"r"] intValue];
		_g = (UInt8) [dict[@"g"] intValue];
		_b = (UInt8) [dict[@"b"] intValue];
		_a = (UInt8) [dict[@"a"] intValue];
	}
	return self;
}

#pragma mark -
#pragma mark NSCoding

- (void)encodeWithCoder:(NSCoder *)coder {
}

- (nullable instancetype)initWithCoder:(NSCoder *)decoder {
	self = [super init];
	if (self) {
	}
	return self;
}

#pragma mark -
#pragma mark Equality

- (BOOL)isEqual:(id)object {
	if (self == object) {
		return YES;
	}
	
	if (![object isKindOfClass:[self class]]) {
		return NO;
	}
	
	LUColor *other = object;
	return self.r == other.r && self.g == other.g && self.b == other.b && self.a == other.a;
}

@end

@implementation LULogColors

- (instancetype)initWithDictionary:(NSDictionary *)dict {
	self = [super init];
	if (self) {
		_exception = [[LULogEntryColors alloc] initWithDictionary:dict[@"exception"]];
		_error = [[LULogEntryColors alloc] initWithDictionary:dict[@"error"]];
		_warning = [[LULogEntryColors alloc] initWithDictionary:dict[@"warning"]];
		_debug = [[LULogEntryColors alloc] initWithDictionary:dict[@"debug"]];
	}
	return self;
}

#pragma mark -
#pragma mark NSCoding

- (void)encodeWithCoder:(NSCoder *)coder {
}

- (nullable instancetype)initWithCoder:(NSCoder *)decoder {
	self = [super init];
	if (self) {
	}
	return self;
}

#pragma mark -
#pragma mark Equality

- (BOOL)isEqual:(id)object {
	if (self == object) {
		return YES;
	}
	
	if (![object isKindOfClass:[self class]]) {
		return NO;
	}
	
	LULogColors *other = object;
	return [self.exception isEqual:other.exception] &&
		   [self.error isEqual:other.error] &&
		   [self.warning isEqual:other.warning] &&
		   [self.debug isEqual:other.debug];
}

@end

@implementation LULogEntryColors

- (instancetype)initWithDictionary:(NSDictionary *)dict {
	self = [super init];
	if (self) {
		_foreground = [[LUColor alloc] initWithDictionary:dict[@"foreground"]];
		_background = [[LUColor alloc] initWithDictionary:dict[@"background"]];
	}
	return self;
}

#pragma mark -
#pragma mark NSCoding

- (void)encodeWithCoder:(NSCoder *)coder {
}

- (nullable instancetype)initWithCoder:(NSCoder *)decoder {
	self = [super init];
	if (self) {
	}
	return self;
}

#pragma mark -
#pragma mark Equality

- (BOOL)isEqual:(id)object {
	if (self == object) {
		return YES;
	}
	
	if (![object isKindOfClass:[self class]]) {
		return NO;
	}
	
	LULogEntryColors *other = object;
	return [self.foreground isEqual:other.foreground] &&
		   [self.background isEqual:other.background];
}

@end

@implementation LULogOverlaySettings

- (instancetype)initWithDictionary:(NSDictionary *)dict {
	self = [super init];
	if (self) {
		_enabled = [dict[@"enabled"] boolValue];
		_maxVisibleLines = [dict[@"maxVisibleLines"] intValue];
		_timeout = [dict[@"timeout"] doubleValue];
		_colors = [[LULogColors alloc] initWithDictionary:dict[@"colors"]];
	}
	return self;
}

#pragma mark -
#pragma mark NSCoding

- (void)encodeWithCoder:(NSCoder *)coder {
}

- (nullable instancetype)initWithCoder:(NSCoder *)decoder {
	self = [super init];
	if (self) {
	}
	return self;
}

#pragma mark -
#pragma mark Equality

- (BOOL)isEqual:(id)object {
	if (self == object) {
		return YES;
	}
	
	if (![object isKindOfClass:[self class]]) {
		return NO;
	}
	
	LULogOverlaySettings *other = object;
	return [self.colors isEqual:other.colors] &&
		   self.isEnabled == other.isEnabled &&
		   self.maxVisibleLines == other.maxVisibleLines &&
		   self.timeout == other.timeout;
}

@end

@implementation LUPluginSettings

- (instancetype)initWithDictionary:(NSDictionary *)dict {
	self = [super init];
	if (self) {
		_exceptionWarning = [[LUExceptionWarningSettings alloc] initWithDictionary:dict[@"exceptionWarning"]];
		_logOverlay = [[LULogOverlaySettings alloc] initWithDictionary:dict[@"logOverlay"]];
		_capacity = [dict[@"capacity"] intValue];
		_trim = [dict[@"trim"] intValue];
		_gesture = parseGesture(dict[@"gesture"]);
		_removeRichTextTags = [dict[@"removeRichTextTags"] boolValue];
		_removeRichTextTags = [dict[@"removeRichTextTags"] boolValue];
		_sortActions = [dict[@"sortActions"] boolValue];
		_sortVariables = [dict[@"sortVariables"] boolValue];
		_emails = dict[@"emails"];
	}
	return self;
}

#pragma mark -
#pragma mark NSCoding

- (void)encodeWithCoder:(NSCoder *)coder {
}

- (nullable instancetype)initWithCoder:(NSCoder *)decoder {
	self = [super init];
	if (self) {
	}
	return self;
}

#pragma mark -
#pragma mark Equality

- (BOOL)isEqual:(id)object {
	if (self == object) {
		return YES;
	}
	
	if (![object isKindOfClass:[self class]]) {
		return NO;
	}
	
	LUPluginSettings *other = object;
	return [self.logOverlay isEqual:other.logOverlay] &&
		   [self.exceptionWarning isEqual:other.exceptionWarning] &&
		   self.capacity == other.capacity &&
		   self.trim == other.trim &&
	       self.gesture == other.gesture &&
	       self.removeRichTextTags == other.removeRichTextTags &&
	       self.sortActions == other.sortActions &&
	       self.sortVariables == other.sortVariables &&
		   [self.emails isEqualToArray:other.emails];
}

@end

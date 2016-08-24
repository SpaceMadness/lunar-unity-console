//
//  LUConsoleSettingsController.m
//  LunarConsole
//
//  Created by Alex Lementuev on 8/22/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import <objc/runtime.h>

#import "Lunar.h"

#import "LUConsoleSettingsController.h"

static const NSInteger kTagName = 1;
static const NSInteger kTagControl = 2;

static NSString * const kSettingsEntryTypeBool = @"Bool";

static NSDictionary * _propertyTypeLookup;

@interface LUConsoleSettingsController () <UITableViewDataSource>
{
    NSArray * _entries;
    LUConsolePluginSettings * _settings;
}

@property (nonatomic, assign) IBOutlet UIView * bottomBarView;

@end

@implementation LUConsoleSettingsController

- (instancetype)initWithSettings:(LUConsolePluginSettings *)settings
{
    self = [super initWithNibName:NSStringFromClass([self class]) bundle:nil];
    if (self)
    {
        _settings = LU_RETAIN(settings);
        _entries = LU_RETAIN([LUConsoleSettingsEntry listSettingsEntries:settings]);
        
    }
    return self;
}

- (void)dealloc
{
    LU_RELEASE(_entries);
    LU_RELEASE(_settings);
    LU_SUPER_DEALLOC
}

#pragma mark -
#pragma mark UITableViewDataSource

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return _entries.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    LUConsoleSettingsEntry *entry = [_entries objectAtIndex:indexPath.row];
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:entry.type];
    if (cell == nil)
    {
        cell = [self loadCellForType:entry.type];
    }
    
    UILabel *name = [cell.contentView viewWithTag:kTagName];
    LUAssert(name);
    
    name.text = entry.title;
    
    if (entry.type == kSettingsEntryTypeBool)
    {
        UISwitch *swtch = [cell.contentView viewWithTag:kTagControl];
        LUAssert(swtch);
        
        swtch.on = [entry.value boolValue];
    }
    
    return cell;
}

#pragma mark -
#pragma mark Actions

- (IBAction)onClose:(id)sender
{
    if ([_delegate respondsToSelector:@selector(consoleSettingsControllerDidClose:)])
    {
        [_delegate consoleSettingsControllerDidClose:self];
    }
}

#pragma mark -
#pragma mark Helpers

- (UITableViewCell *)loadCellForType:(NSString *)type
{
    NSString *nibName = [NSString stringWithFormat:@"LUSettingsTableCell%@", type];
    return (UITableViewCell *) [[NSBundle mainBundle] loadNibNamed:nibName owner:self options:nil].firstObject;
}

@end

@implementation LUConsoleSettingsEntry

- (instancetype)initWithName:(NSString *)name value:(id)value type:(NSString *)type title:(NSString *)title
{
    self = [super init];
    if (self)
    {
        _name = LU_RETAIN(name);
        _title = LU_RETAIN(title);
        _value = LU_RETAIN(value);
        _initialValue = LU_RETAIN(value);
        _type = LU_RETAIN(type);
    }
    return self;
}

- (void)dealloc
{
    LU_RELEASE(_name);
    LU_RELEASE(_title);
    LU_RELEASE(_value);
    LU_RELEASE(_initialValue);
    LU_RELEASE(_type);
    LU_SUPER_DEALLOC
}

#pragma mark -
#pragma mark Introspection

+ (NSString *)propertyTypeName:(objc_property_t)property
{
    const char *cattributes = property_getAttributes(property);
    if (cattributes)
    {
        NSString *attributes = [NSString stringWithUTF8String:cattributes];
        NSArray *components = [attributes componentsSeparatedByString:@","];
        for (NSString *component in components)
        {
            if (component.length > 1 && [component hasPrefix:@"T"])
            {
                if (_propertyTypeLookup == nil)
                {
                    _propertyTypeLookup = [[NSDictionary alloc] initWithObjectsAndKeys:
                                           kSettingsEntryTypeBool, @"c",
                                           nil];
                }
                
                NSString *typeShort = [component substringFromIndex:1];
                return _propertyTypeLookup[typeShort];
            }
        }
    }
    
    return nil;
}

+ (NSArray *)listSettingsEntries:(LUConsolePluginSettings *)settings
{
    unsigned int propertyCount;
    objc_property_t *properties = class_copyPropertyList([settings class], &propertyCount);
    
    NSMutableArray *entries = [NSMutableArray arrayWithCapacity:propertyCount];
    for (int i = 0; i < propertyCount; i++)
    {
        objc_property_t property = properties[i];
        const char *cname = property_getName(property);
        if (cname)
        {
            NSString *name = [NSString stringWithUTF8String:cname];
            NSString *title = [self titleFromName:name];
            NSString *type = [self propertyTypeName:property];
            id value = [settings valueForKey:name];
            
            LUConsoleSettingsEntry *entry = [[LUConsoleSettingsEntry alloc] initWithName:name value:value type:type title:title];
            [entries addObject:entry];
            LU_RELEASE(entry);
        }
    }
    free(properties);
    return entries;
}

+ (NSString *)titleFromName:(NSString *)name
{
    NSMutableString *title = [NSMutableString string];
    [title appendFormat:@"%c", [name characterAtIndex:0] ^ ' ']; // invert case
    
    for (NSUInteger i = 1; i < name.length; ++i)
    {
        unichar chr = [name characterAtIndex:i];
        if (chr >= 'A' && chr <= 'Z')
        {
            [title appendString:@" "];
        }
        [title appendFormat:@"%c", chr];
        
    }
    return title;
}

#pragma mark -
#pragma mark Properties

- (BOOL)isChanged
{
    return ![_value isEqual:_initialValue];
}

@end

//
//  LUConsoleSettingsController.m
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2016 Alex Lementuev, SpaceMadness.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
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

@property (nonatomic, assign) IBOutlet UIView * contentView;
@property (nonatomic, assign) IBOutlet UIView * bottomBarView;
@property (nonatomic, assign) IBOutlet UILabel * titleLabel;
@property (nonatomic, assign) IBOutlet UITableView * tableView;

@end

@implementation LUConsoleSettingsController

+ (void)load
{
    if (!LU_IOS_MIN_VERSION_AVAILABLE)
    {
        return;
    }
    
    if ([self class] == [LUConsoleSettingsController class])
    {
        // force linker to add these classes for Interface Builder
        [LUSwitch class];
    }
}

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
#pragma mark View

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    // colors
    self.view.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.5];
    
    LUTheme *theme = [LUTheme mainTheme];
    
    _contentView.backgroundColor =
    _bottomBarView.backgroundColor =
    _tableView.backgroundColor = theme.tableColor;
    
    _contentView.layer.borderColor = [[UIColor colorWithRed:0.37 green:0.37 blue:0.37 alpha:1.0] CGColor];
    _contentView.layer.borderWidth = 2;
    
    _titleLabel.textColor = theme.cellLog.textColor;
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
    
    LUTheme *theme = [LUTheme mainTheme];
    
    cell.contentView.backgroundColor = indexPath.row % 2 == 0 ? theme.backgroundColorLight : theme.backgroundColorDark;
    
    UILabel *nameLabel = [cell.contentView viewWithTag:kTagName];
    LUAssert(nameLabel);
    
    nameLabel.font = theme.font;
    nameLabel.textColor = theme.cellLog.textColor;
    nameLabel.text = entry.title;
    
    if (entry.type == kSettingsEntryTypeBool)
    {
        LUSwitch *swtch = [cell.contentView viewWithTag:kTagControl];
        LUAssert(swtch);
        
        swtch.on = [entry.value boolValue];
        swtch.userData = entry;
        [swtch addTarget:self action:@selector(onToggleBoolean:) forControlEvents:UIControlEventValueChanged];
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
#pragma mark

- (void)onToggleBoolean:(LUSwitch *)swtch
{
    LUConsoleSettingsEntry *entry = swtch.userData;
    entry.value = swtch.isOn ? @YES : @NO;
}

#pragma mark -
#pragma mark Helpers

- (UITableViewCell *)loadCellForType:(NSString *)type
{
    NSString *nibName = [NSString stringWithFormat:@"LUSettingsTableCell%@", type];
    return (UITableViewCell *) [[NSBundle mainBundle] loadNibNamed:nibName owner:self options:nil].firstObject;
}

#pragma mark -
#pragma mark Properties

- (NSArray *)changedEntries
{
    NSMutableArray *result = [NSMutableArray array];
    for (LUConsoleSettingsEntry *entry in _entries)
    {
        if (entry.isChanged) [result addObject:entry];
    }
    return result;
}

@end

@implementation LUConsoleSettingsEntry

- (instancetype)initWithName:(NSString *)name value:(id)value type:(NSString *)type title:(NSString *)title
{
    self = [super init];
    if (self)
    {
        if (name == nil ||
            value == nil ||
            type == nil)
        {
            LU_RELEASE(self);
            self = nil;
            return nil;
        }
        
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
                                           kSettingsEntryTypeBool, @"B",
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
            if (type == nil)
            {
                NSLog(@"LunarMobileConsole: unable to resolve the type of property '%@'", name);
                continue;
            }
            
            id value = [settings valueForKey:name];
            
            LUConsoleSettingsEntry *entry = [[LUConsoleSettingsEntry alloc] initWithName:name value:value type:type title:title];
            if (entry == nil)
            {
                NSLog(@"LunarMobileConsole: unable to create setting entry '%@'", name);
                continue;
            }
            
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

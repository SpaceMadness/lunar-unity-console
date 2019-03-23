//
//  LUConsoleSettingsController.m
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2019 Alex Lementuev, SpaceMadness.
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
static const NSInteger kTagButton = 2;
static const NSInteger kTagInput = 3;
static const NSInteger kTagSwitch = 4;

typedef enum : NSUInteger {
	LUSettingTypeBool,
	LUSettingTypeInt,
	LUSettingTypeDouble,
	LUSettingTypeEnum
} LUSettingType;

static NSDictionary * _propertyTypeLookup;
static NSArray * _proOnlyFeaturesLookup;

@interface LUConsoleSetting : NSObject

@property (nonatomic, readonly, weak) id target;
@property (nonatomic, readonly) NSString *name;
@property (nonatomic, readonly) LUSettingType type;
@property (nonatomic, readonly) NSString *title;
@property (nonatomic, strong) id value;
@property (nonatomic, assign) BOOL boolValue;
@property (nonatomic, assign) int intValue;
@property (nonatomic, assign) double doubleValue;

@property (nonatomic, readonly, nullable) NSArray<NSString *> *values;
@property (nonatomic, readonly) BOOL proOnly;

- (instancetype)initWithTarget:(id)target name:(NSString *)name type:(LUSettingType)type title:(NSString *)title;
- (instancetype)initWithTarget:(id)target name:(NSString *)name type:(LUSettingType)type title:(NSString *)title values:(nullable NSArray<NSString *> *)values;

@end

@implementation LUConsoleSetting

- (instancetype)initWithTarget:(id)target name:(NSString *)name type:(LUSettingType)type title:(NSString *)title {
	return [self initWithTarget:target name:name type:type title:title values:nil];
}
	
- (instancetype)initWithTarget:(id)target name:(NSString *)name type:(LUSettingType)type title:(NSString *)title values:(nullable NSArray<NSString *> *)values {
	self = [super init];
	if (self) {
		_target = target;
		_name = name;
		_type = type;
		_title = title;
		_values = values;
	}
	return self;
}

- (id)value {
	return [_target valueForKey:_name];
}

- (void)setValue:(id)value {
	[_target setValue:value forKey:_name];
}

- (BOOL)boolValue {
	return [[self value] boolValue];
}

- (void)setBoolValue:(BOOL)boolValue {
	[self setValue:[NSNumber numberWithBool:boolValue]];
}

- (int)intValue {
	return [[self value] intValue];
}

- (void)setIntValue:(int)intValue {
	[self setValue:[NSNumber numberWithInt:intValue]];
}

- (double)doubleValue {
	return [[self value] doubleValue];
}

- (void)setDoubleValue:(double)doubleValue {
	[self setValue:[NSNumber numberWithDouble:doubleValue]];
}

@end

@interface LUConsoleSettingsSection : NSObject

@property (nonatomic, readonly) NSString *title;
@property (nonatomic, readonly) NSArray<LUConsoleSetting *> *entries;

- (instancetype)initWithTitle:(NSString *)title entries:(NSArray<LUConsoleSetting *> *)entries;

@end

@implementation LUConsoleSettingsSection

- (instancetype)initWithTitle:(NSString *)title entries:(NSArray<LUConsoleSetting *> *)entries {
	self = [super init];
	if (self) {
		_title = title;
		_entries = entries;
	}
	return self;
}

@end

@interface LUConsoleSettingsController () <UITableViewDataSource, LUConsolePopupControllerDelegate> {
    NSArray<LUConsoleSettingsSection *> * _sections;
    LUPluginSettings * _settings;
}

@property (nonatomic, weak) IBOutlet UITableView * tableView;

@end

@implementation LUConsoleSettingsController

- (instancetype)initWithSettings:(LUPluginSettings *)settings {
    self = [super initWithNibName:NSStringFromClass([self class]) bundle:nil];
    if (self)
    {
        _settings = settings;
        _sections = [[self class] listSections:settings];
    }
    return self;
}


#pragma mark -
#pragma mark View

- (void)viewDidLoad {
    [super viewDidLoad];
    
    LUTheme *theme = [LUTheme mainTheme];
    _tableView.backgroundColor = theme.tableColor;
    _tableView.rowHeight = 43;
    
    self.popupTitle = @"Settings";
    self.popupIcon = theme.settingsIconImage;
}

#pragma mark -
#pragma mark UITableViewDataSource

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
	return _sections.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return _sections[section].entries.count;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
	return _sections[section].title;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	LUConsoleSetting *setting = _sections[indexPath.section].entries[indexPath.row];
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"Setting Cell"];
    if (cell == nil)
    {
        cell = (UITableViewCell *) [[NSBundle mainBundle] loadNibNamed:@"LUSettingsTableCell" owner:self options:nil].firstObject;
    }
    
    LUTheme *theme = [LUTheme mainTheme];
    
    cell.contentView.backgroundColor = indexPath.row % 2 == 0 ? theme.backgroundColorLight : theme.backgroundColorDark;
    
    UILabel *nameLabel = [cell.contentView viewWithTag:kTagName];
	LUButton *enumButton = [cell.contentView viewWithTag:kTagButton];
	UITextField *inputField = [cell.contentView viewWithTag:kTagInput];
	LUSwitch *boolSwitch = [cell.contentView viewWithTag:kTagSwitch];
	
	enumButton.hidden = YES;
	inputField.hidden = YES;
	boolSwitch.hidden = YES;
	
    BOOL available = LUConsoleIsFullVersion || !setting.proOnly;
    
    nameLabel.font = theme.font;
    nameLabel.textColor = available ? theme.cellLog.textColor : theme.settingsTextColorUnavailable;
    nameLabel.text = setting.title;
	
	switch (setting.type) {
		case LUSettingTypeBool:
			boolSwitch.hidden = NO;
			boolSwitch.on = [setting boolValue];
			boolSwitch.userData = setting;
			[boolSwitch addTarget:self action:@selector(onToggleBoolean:) forControlEvents:UIControlEventValueChanged];
			boolSwitch.enabled = available;
			break;
		case LUSettingTypeInt:
			inputField.hidden = NO;
			inputField.text = [NSString stringWithFormat:@"%d", [setting intValue]];
			break;
		case LUSettingTypeDouble:
			inputField.hidden = NO;
			inputField.text = [NSString stringWithFormat:@"%g", [setting doubleValue]];
			break;
		case LUSettingTypeEnum:
			enumButton.hidden = NO;
			int index = [setting intValue];
			[enumButton setTitle:setting.values[index] forState:UIControlStateNormal];
			enumButton.titleLabel.font = theme.enumButtonFont;
			enumButton.userData = setting;
			[enumButton setTitleColor:theme.enumButtonTitleColor forState:UIControlStateNormal];
			if (enumButton.allTargets.count == 0) {
				[enumButton addTarget:self action:@selector(enumButtonClicked:) forControlEvents:UIControlEventTouchUpInside];
			}
			break;
	}
    
    return cell;
}

- (void)enumButtonClicked:(LUButton *)button {
	LUConsoleSetting *setting = button.userData;
	
	LUEnumPickerViewController *picker = [[LUEnumPickerViewController alloc] initWithValues:setting.values initialIndex:[setting intValue]];
	picker.userData = setting;
	
	LUConsolePopupController *popupController = [[LUConsolePopupController alloc] initWithContentController:picker];
	[popupController presentFromController:self.parentViewController animated:YES];
	popupController.popupDelegate = self;
}

#pragma mark -
#pragma mark UITableViewDelegate

- (void)tableView:(UITableView *)tableView willDisplayHeaderView:(UIView *)view forSection:(NSInteger)section
{
	if ([view isKindOfClass:[UITableViewHeaderFooterView class]])
	{
		LUTheme *theme = [LUTheme mainTheme];
		
		UITableViewHeaderFooterView *headerView = (UITableViewHeaderFooterView *)view;
		headerView.textLabel.font = theme.actionsGroupFont;
		headerView.textLabel.textColor = theme.actionsGroupTextColor;
		headerView.contentView.backgroundColor = theme.actionsGroupBackgroundColor;
	}
}

#pragma mark -
#pragma mark LUConsolePopupControllerDelegate

- (void)popupControllerDidDismiss:(LUConsolePopupController *)controller {
	LUEnumPickerViewController *pickerController = (LUEnumPickerViewController *) controller.contentController;
	
	LUConsoleSetting *setting = pickerController.userData;
	setting.intValue = (int) pickerController.selectedIndex;
	
	[controller dismissAnimated:YES];
}

#pragma mark -
#pragma mark Controls

- (void)onToggleBoolean:(LUSwitch *)swtch {
    LUConsoleSetting *setting = swtch.userData;
    setting.value = swtch.isOn ? @YES : @NO;
    [self settingEntryDidChange:setting];
}

#pragma mark -
#pragma mark Entries

+ (NSArray<LUConsoleSettingsSection *> *)listSections:(LUPluginSettings *)settings {
	return @[
	  [[LUConsoleSettingsSection alloc] initWithTitle:@"Exception Warning" entries:@[
        [[LUConsoleSetting alloc] initWithTarget:settings.exceptionWarning name:@"displayMode" type:LUSettingTypeEnum title:@"Display Mode" values:@[@"None", @"Errors", @"Exceptions", @"All"]]
	  ]],
	  [[LUConsoleSettingsSection alloc] initWithTitle:@"Log Overlay" entries:@[
		[[LUConsoleSetting alloc] initWithTarget:settings.logOverlay name:@"enabled" type:LUSettingTypeBool title:@"Enabled"],
		[[LUConsoleSetting alloc] initWithTarget:settings.logOverlay name:@"maxVisibleLines" type:LUSettingTypeInt title:@"Max Visible Lines"],
		[[LUConsoleSetting alloc] initWithTarget:settings.logOverlay name:@"timeout" type:LUSettingTypeDouble title:@"Timeout"]
	  ]],
	];
}

- (void)settingEntryDidChange:(LUConsoleSetting *)entry {
//    [_settings setValue:entry.value forKey:entry.name];
//    [_settings save];
}

@end

//
//  LUActionControllerBase_Inheritance.h
//  LunarConsole
//
//  Created by Alex Lementuev on 2/6/17.
//  Copyright © 2017 Space Madness. All rights reserved.
//

#import "LUActionControllerBase.h"

@interface LUActionControllerBase ()

@property (nonatomic, weak) IBOutlet UIView       * noActionsWarningView;
@property (nonatomic, weak) IBOutlet UILabel      * noActionsWarningLabel;
@property (nonatomic, weak) IBOutlet UITableView  * tableView;
@property (nonatomic, weak) IBOutlet UISearchBar  * filterBar;
@property (nonatomic, weak) IBOutlet UIButton     * learnMoreButton;

- (void)updateNoActionWarningView;
- (void)setNoActionsWarningViewHidden:(BOOL)hidden;

@end

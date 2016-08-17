//
//  LUEntry.h
//  LunarConsole
//
//  Created by Alex Lementuev on 4/1/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LUEntry : NSObject

@property (nonatomic, readonly) int actionId; // FIXME: rename
@property (nonatomic, readonly) NSString *name;

- (instancetype)initWithId:(int)actionId name:(NSString *)name;

- (UITableViewCell *)tableView:(UITableView *)tableView cellAtIndex:(NSUInteger)index;
- (CGSize)cellSizeForTableView:(UITableView *)tableView;

- (NSComparisonResult)compare:(LUEntry *)other;

@end

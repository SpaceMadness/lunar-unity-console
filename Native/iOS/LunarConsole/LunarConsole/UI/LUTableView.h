//
//  LUTableView.h
//  LunarConsole
//
//  Created by Alex Lementuev on 11/12/15.
//  Copyright © 2015 Space Madness. All rights reserved.
//

#import <UIKit/UIKit.h>

@class LUTableView;

@protocol LUTableViewTouchDelegate <NSObject>

@optional
- (void)tableView:(LUTableView *)tableView touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event;
- (void)tableView:(LUTableView *)tableView touchesMoved:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event;
- (void)tableView:(LUTableView *)tableView touchesEnded:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event;
- (void)tableView:(LUTableView *)tableView touchesCancelled:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event;

@end

@interface LUTableView : UITableView

@property (nonatomic, assign) id<LUTableViewTouchDelegate> touchDelegate;

@end

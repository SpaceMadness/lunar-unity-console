//
//  LUEntry.m
//  LunarConsole
//
//  Created by Alex Lementuev on 4/1/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "LUEntry.h"

#import "Lunar.h"

@implementation LUEntry

- (instancetype)initWithId:(int)actionId name:(NSString *)name
{
    self = [super init];
    if (self)
    {
        if (name.length == 0)
        {
            NSLog(@"Can't create an entry: name is nil or empty");
            LU_RELEASE(self);
            self = nil;
            return nil;
        }
        
        _actionId = actionId;
        _name = LU_RETAIN(name);
    }
    return self;
}

- (void)dealloc
{
    LU_RELEASE(_name);
    LU_SUPER_DEALLOC
}

#pragma mark -
#pragma mark NSComparisonMethods

- (NSComparisonResult)compare:(LUEntry *)other
{
    return [_name compare:other.name];
}

#pragma mark -
#pragma mark Equality

- (BOOL)isEqual:(id)object
{
    if ([object isKindOfClass:[LUEntry class]])
    {
        LUEntry *entry = object;
        return self.actionId == entry.actionId && [self.name isEqualToString:entry.name];
    }
    
    return NO;
}

#pragma mark -
#pragma mark Description

- (NSString *)description
{
    return [NSString stringWithFormat:@"%d: %@", self.actionId, self.name];
}

#pragma mark -
#pragma mark UITableView

- (UITableViewCell *)tableView:(UITableView *)tableView cellAtIndex:(NSUInteger)index
{
    LU_SHOULD_IMPLEMENT_METHOD
    return nil;
}

- (CGSize)cellSizeForTableView:(UITableView *)tableView
{
    LU_SHOULD_IMPLEMENT_METHOD
    return CGSizeMake(CGRectGetWidth(tableView.bounds), 44);
}

@end

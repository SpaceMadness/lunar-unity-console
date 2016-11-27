//
//  LUActionRegistryFilter.h
//  LunarConsole
//
//  Created by Alex Lementuev on 3/11/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "LUActionRegistry.h"

@class LUActionRegistryFilter;

@protocol LUActionRegistryFilterDelegate <NSObject>

- (void)actionRegistryFilter:(LUActionRegistryFilter *)registryFilter didAddAction:(LUAction *)action atIndex:(NSUInteger)index;
- (void)actionRegistryFilter:(LUActionRegistryFilter *)registryFilter didRemoveAction:(LUAction *)action atIndex:(NSUInteger)index;
- (void)actionRegistryFilter:(LUActionRegistryFilter *)registry didRegisterVariable:(LUCVar *)variable atIndex:(NSUInteger)index;
- (void)actionRegistryFilter:(LUActionRegistryFilter *)registry didChangeVariable:(LUCVar *)variable atIndex:(NSUInteger)index;

@end

@interface LUActionRegistryFilter : NSObject

@property (nonatomic, readonly) LUActionRegistry *registry;
@property (nonatomic, readonly) BOOL isFiltering;
@property (nonatomic, assign) id<LUActionRegistryFilterDelegate> delegate;
@property (nonatomic, readonly) NSArray *actions;
@property (nonatomic, readonly) NSArray *variables;

+ (instancetype)filterWithActionRegistry:(LUActionRegistry *)actionRegistry;
- (instancetype)initWithActionRegistry:(LUActionRegistry *)actionRegistry;

- (BOOL)setFilterText:(NSString *)filterText;

@end

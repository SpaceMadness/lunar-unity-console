//
//  LUConsoleController.m
//  LunarConsole
//
//  Created by Alex Lementuev on 11/26/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "LUConsoleController.h"

#import "Lunar.h"

@interface LUConsoleController ()

@property (nonatomic, weak) IBOutlet UIScrollView *scrollView;
@property (nonatomic, weak) LUConsolePlugin *plugin;

@end

@implementation LUConsoleController

+ (instancetype)controllerWithPlugin:(LUConsolePlugin *)plugin
{
    return [[self alloc] initWithPlugin:plugin];
}

- (instancetype)initWithPlugin:(LUConsolePlugin *)plugin
{
    self = [super initWithNibName:NSStringFromClass([self class]) bundle:nil];
    if (self)
    {
        _plugin = plugin;
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    // give auto layout a chance to set frames
    dispatch_async(dispatch_get_main_queue(), ^{
        LUConsoleLogController *logController = [LUConsoleLogController controllerWithPlugin:_plugin];
        [self addPageController:logController];
        
        LUActionController *actionController = [LUActionController controllerWithActionRegistry:_plugin.actionRegistry];
        [self addPageController:actionController];
    });
}

- (void)addPageController:(UIViewController *)controller
{
    CGSize contentSize = _scrollView.contentSize;
    CGSize pageSize = _scrollView.bounds.size;
    
    [self addChildViewController:controller];
    controller.view.frame = CGRectMake(contentSize.width, 0, pageSize.width, pageSize.height);
    [_scrollView addSubview:controller.view];
    [controller didMoveToParentViewController:controller];
    
    contentSize.width += pageSize.width;
    contentSize.height = pageSize.height;
    
    _scrollView.contentSize = contentSize;
}

@end

//
//  LUConsoleController.m
//  LunarConsole
//
//  Created by Alex Lementuev on 11/26/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "LUConsoleController.h"

#import "Lunar.h"

static LUConsoleControllerState * _sharedControllerState;

@interface LUConsoleController () <LUConsoleLogControllerResizeDelegate, LUConsoleResizeControllerDelegate>

@property (nonatomic, weak) IBOutlet UIScrollView *scrollView; // we need to be able to use "paging" scrolling between differenct controllers

@property (nonatomic, weak) IBOutlet UIView *contentView; // the container view for controllers and "button" bar at the bottom

// we need to keep constraints in order to properly resize content view with auto layout (can't just set frames)
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *contentTrailingConstraint;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *contentBottomConstraint;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *contentLeadingConstraint;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *contentTopConstraint;

@property (nonatomic, weak) LUConsolePlugin *plugin;
@property (nonatomic, strong) NSArray<UIViewController *> *pageControllers;

@end

@implementation LUConsoleController

+ (void)load
{
    if (!LU_IOS_MIN_VERSION_AVAILABLE)
    {
        return;
    }
    
    if ([self class] == [LUConsoleLogController class])
    {
        // force linker to add these classes for Interface Builder
        [LUConsoleLogTypeButton class];
        [LUSwitch class];
        [LUTableView class];
        [LUPassTouchView class];
    }
}

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
    
    LUConsoleLogController *logController = [LUConsoleLogController controllerWithPlugin:_plugin];
    logController.version = _plugin.version;
    logController.resizeDelegate = self;
    
    LUActionController *actionController = [LUActionController controllerWithActionRegistry:_plugin.actionRegistry];
    
    _pageControllers = @[logController, actionController];
    [self setPageControllers:_pageControllers];
    
    // notify delegate
    if ([_delegate respondsToSelector:@selector(consoleControllerDidOpen:)])
    {
        [_delegate consoleControllerDidOpen:self];
    }
    
    self.contentView.translatesAutoresizingMaskIntoConstraints = NO;
    self.scrollView.translatesAutoresizingMaskIntoConstraints = NO;
    
    if ([LUConsoleControllerState sharedControllerState].hasCustomControllerFrame)
    {
        [self setControllerFrame:[LUConsoleControllerState sharedControllerState].controllerFrame];
    }
}

- (void)viewDidLayoutSubviews
{
    [super viewDidLayoutSubviews];
    
    // set paging
    CGSize pageSize = _scrollView.bounds.size;
    CGSize contentSize = CGSizeMake(_pageControllers.count * pageSize.width, pageSize.height);
    _scrollView.contentSize = contentSize;
}

- (void)setPageControllers:(NSArray<UIViewController *> *)controllers
{
    NSMutableArray *constraints = [NSMutableArray new];
    
    for (NSUInteger idx = 0; idx < controllers.count; ++idx)
    {
        UIViewController *controller = controllers[idx];
        UIViewController *prevController = idx > 0 ? controllers[idx - 1] : nil;
        UIViewController *nextController = idx < controllers.count - 1 ? controllers[idx + 1] : nil;
        
        controller.view.translatesAutoresizingMaskIntoConstraints = NO;
        
        // add child controller
        [self addChildViewController:controller];
        
        // add view
        [_scrollView addSubview:controller.view];
        
        // call notification
        [controller didMoveToParentViewController:controller];
        
        // width
        [constraints addObject:[NSLayoutConstraint constraintWithItem:controller.view
                                                            attribute:NSLayoutAttributeWidth
                                                            relatedBy:NSLayoutRelationEqual
                                                               toItem:_scrollView
                                                            attribute:NSLayoutAttributeWidth
                                                           multiplier:1.0                                      constant:0]];
        
        // height
        [constraints addObject:[NSLayoutConstraint constraintWithItem:controller.view
                                                            attribute:NSLayoutAttributeHeight
                                                            relatedBy:NSLayoutRelationEqual
                                                               toItem:_scrollView
                                                            attribute:NSLayoutAttributeHeight
                                                           multiplier:1.0                                      constant:0]];
        
        // vertical center
        [constraints addObject:[NSLayoutConstraint constraintWithItem:controller.view
                                                            attribute:NSLayoutAttributeCenterY
                                                            relatedBy:NSLayoutRelationEqual
                                                               toItem:_scrollView
                                                            attribute:NSLayoutAttributeCenterY
                                                           multiplier:1.0                                      constant:0]];
        
        // left
        if (prevController)
        {
            [constraints addObject:[NSLayoutConstraint constraintWithItem:controller.view
                                                                attribute:NSLayoutAttributeLeft
                                                                relatedBy:NSLayoutRelationEqual
                                                                   toItem:prevController.view
                                                                attribute:NSLayoutAttributeRight
                                                               multiplier:1.0
                                                                 constant:0]];
        }
        else
        {
            [constraints addObject:[NSLayoutConstraint constraintWithItem:controller.view
                                                                attribute:NSLayoutAttributeLeft
                                                                relatedBy:NSLayoutRelationEqual
                                                                   toItem:_scrollView
                                                                attribute:NSLayoutAttributeLeft
                                                               multiplier:1.0
                                                                 constant:0]];
        }
        
        // right
        if (nextController)
        {
            [constraints addObject:[NSLayoutConstraint constraintWithItem:controller.view
                                                                attribute:NSLayoutAttributeRight
                                                                relatedBy:NSLayoutRelationEqual
                                                                   toItem:nextController.view
                                                                attribute:NSLayoutAttributeLeft
                                                               multiplier:1.0
                                                                 constant:0]];
        }
        else
        {
            [constraints addObject:[NSLayoutConstraint constraintWithItem:controller.view
                                                                attribute:NSLayoutAttributeRight
                                                                relatedBy:NSLayoutRelationEqual
                                                                   toItem:_scrollView
                                                                attribute:NSLayoutAttributeRight
                                                               multiplier:1.0
                                                                 constant:0]];
        }
        
        
    }
    
    [NSLayoutConstraint activateConstraints:constraints];
}

#pragma mark -
#pragma mark Helpers

- (void)setContentHidden:(BOOL)hidden
{
    self.contentView.hidden = hidden;
}

- (void)setControllerFrame:(CGRect)frame
{
    self.contentTopConstraint.constant = CGRectGetMinY(frame);
    self.contentBottomConstraint.constant = frame.size.height;
    self.contentLeadingConstraint.constant = CGRectGetMinX(frame);
    self.contentTrailingConstraint.constant = frame.size.width;
    [self.view layoutIfNeeded];
    
    [LUConsoleControllerState sharedControllerState].controllerFrame = frame;
}

#pragma mark -
#pragma mark Actions

- (IBAction)onClose:(id)sender
{
    if ([_delegate respondsToSelector:@selector(consoleControllerDidClose:)])
    {
        [_delegate consoleControllerDidClose:self];
    }
}

#pragma mark -
#pragma mark LUConsoleLogControllerResizeDelegate

- (void)consoleLogControllerDidRequestResize:(LUConsoleLogController *)controller
{
    [self setContentHidden:YES];
    
    LUConsoleResizeController *resizeController = [LUConsoleResizeController new];
    resizeController.delegate = self;
    [self addChildController:resizeController withFrame:self.contentView.frame];
}

#pragma mark -
#pragma mark LUConsoleResizeControllerDelegate

- (void)consoleResizeControllerDidClose:(LUConsoleResizeController *)controller
{
    CGRect frame = controller.view.frame;
    
    [self removeChildController:controller];
    
    CGSize size = self.view.bounds.size;
    frame.size.width = size.width - CGRectGetMaxX(frame);
    frame.size.height = size.height - CGRectGetMaxY(frame);
    [self.view layoutIfNeeded];
    
    [self setControllerFrame:frame];
    
    [self setContentHidden:NO];
}

@end

@implementation LUConsoleControllerState

+ (instancetype)sharedControllerState
{
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        _sharedControllerState = [[self alloc] init];
    });
    
    return _sharedControllerState;
}

- (void)setControllerFrame:(CGRect)controllerFrame
{
    _hasCustomControllerFrame = YES;
    _controllerFrame = controllerFrame;
}

@end

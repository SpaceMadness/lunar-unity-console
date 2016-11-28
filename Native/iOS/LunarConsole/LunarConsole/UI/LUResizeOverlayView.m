//
//  LUResizeOverlayView.m
//  LunarConsole
//
//  Created by Alex Lementuev on 11/27/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

#import "LUResizeOverlayView.h"
#import "Lunar.h"

@interface LUResizeOverlayView ()

@property (weak, nonatomic) IBOutlet LUResizeBarView *topBar;
@property (weak, nonatomic) IBOutlet LUResizeBarView *bottomBar;
@property (weak, nonatomic) IBOutlet LUResizeBarView *leftBar;
@property (weak, nonatomic) IBOutlet LUResizeBarView *rightBar;

@end

@implementation LUResizeOverlayView

#pragma mark -
#pragma mark Actions

- (IBAction)onConfirm:(id)sender {
}

- (IBAction)onCancel:(id)sender {
}

@end

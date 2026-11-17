//
//  LUWindow.m
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2015-2021 Alex Lementuev, SpaceMadness.
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


#import "LUWindow.h"

#import "Lunar.h"

@implementation LUWindow

- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self)
    {
        if (@available(iOS 13.0, *))
        {
            // Under the UIScene lifecycle (Unity 6000.0.71+) a window without a scene is never shown (#235).
            if (self.windowScene == nil)
            {
                // ponytail: first foreground-active scene only; per-scene windows if iPad multi-window support is ever needed
                UIWindowScene *pick = nil;
                for (UIScene *scene in [UIApplication sharedApplication].connectedScenes)
                {
                    if (![scene isKindOfClass:[UIWindowScene class]]) continue;
                    if (scene.activationState == UISceneActivationStateForegroundActive) { pick = (UIWindowScene *)scene; break; }
                    if (pick == nil) pick = (UIWindowScene *)scene;
                }
                self.windowScene = pick;
            }
        }
    }
    return self;
}

- (BOOL)pointInside:(CGPoint)point withEvent:(UIEvent *)event
{
    return [self.rootViewController.view pointInside:point withEvent:event];
}

@end

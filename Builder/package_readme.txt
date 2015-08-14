# Lunar Unity Mobile Console

GitHub page:
https://github.com/SpaceMadness/lunar-unity-console

Asset store link:
https://www.assetstore.unity3d.com/#!/content/43800

Forum thread:
http://forum.unity3d.com/threads/lunar-mobile-console-high-performance-unity-ios-android-logger-built-with-native-platform-ui.347650/

Requires Unity 5.0 or later.

## Project Goals

Build a high-performance Unity iOS/Android logger using the native platform UI.  

## Platform Support
- iOS: requires iOS 7 or later
- Android: coming soon
  
## Key Benefits
- Heavily optimized native C/Objective-C code with a low memory footprint.
- Works well with a huge log amount (up to 100000 entries).
- Built with a native platform UI (does NOT rely on Unity GUI).
- Resolution independent (looks great on highres/retina displays).
- Does NOT modify scenes or add assets.
- Removes completely from the release build with a single mouse click (absolutely NO traces left).

## Features
- Instant error notification (never miss an unhandled exception again)
- Quick logger output access with a multi touch gesture
- Crystal clear font and a nice mobile-friendly interface
- Filter by text and log type
- Scroll lock and copy-to-clipboard options
- Automatic updates!
 
## Installation
- Automatic:  
  Unity Editor Menu:  Window ▶ Lunar Mobile Console ▶ Install...
  
- Manual:  
  Drag'n'Drop `LunarConsol.prefab` (Assets/LunarConsole/Scripts/LunarConsole.prefab) into your current scene's hierarchy and save your changes. You only need to do it once for your startup scene.

## Enable/Disable plugin for debug/release
- To disable:  
  Window ▶ Lunar Mobile Console ▶ Disable
- To re-enable:  
  Window ▶ Lunar Mobile Console ▶ Enable
  
  When disabled, the plugin source files and resources would NOT appear in the generated native platform project.
 
## Check for Updates
Window ▶ Lunar Mobile Console ▶ Check for updates...

## Bug Reports
Window ▶ Lunar Mobile Console ▶ Report bug...
 
## Contacts
For anything else: lunar.plugin@gmail.com

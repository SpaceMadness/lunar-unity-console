# Lunar Unity Mobile Console

Asset store link: [http://u3d.as/content/space-madness/lunar-mobile-console](http://bit.ly/1Ubzkpl)  
Requires Unity 5.0 or later.

**If you enjoy using the plugin - please, rate it on the Asset Store!**

<img width="465" src="https://cloud.githubusercontent.com/assets/786644/14592627/a7757736-04d5-11e6-9eef-62257823a83a.png">

## Table of Contents
- [About](#about)
  - [Platform Support](#platform-support)
  - [Key Benefits](#key-benefits)
  - [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
  - [Configuration](#configuration)
  - [Stack Trace Frames](#stack-trace-frames)
  - [Enabling/Disabling Plugin](#enablingdisabling-plugin)
  - [Build System Support](#build-system-support)
- [Troubleshooting](#troubleshooting)
- [Miscellaneous](#miscellaneous)
  - [Check for Updates](#check-for-updates)
  - [Bug Reports](#bug-reports)
  - [Contacts](#contacts)
  - [Social Media](#social-media)

## About
The project goal is to build a high-performance and lightweight Unity native iOS/Android logger.  

Visit [Unity Forums Thread](http://forum.unity3d.com/threads/lunar-mobile-console-high-performance-unity-ios-android-logger-built-with-native-platform-ui.347650/) for discussions and general questions.  

### Platform Support
- **iOS**: requires iOS 8 or later
- **Android**: requires API Level 9 or later
  
### Key Benefits
- Native C/Objective-C/Java code with a low memory footprint.
- Works well with huge logs amount (up to 65536 entries).
- Completely built with native iOS/Android UI (does NOT rely on Unity GUI).
- Resolution independent (looks great on highres/retina displays).
- Does NOT modify scenes and does NOT add new assets.
- Removes completely from the release build with a single mouse click or from command line (absolutely NO traces left).

### Features
- Instant error notification (never miss an unhandled exception again):  
<img src="https://cloud.githubusercontent.com/assets/786644/12805825/799b00e8-cab4-11e5-97ac-c90c50f0a9d2.PNG" width=320/>
- Quick logger output access with a multi touch gesture:  
![guid-bfa239b6-b55a-4f88-82ea-744f01cd9d77-web](https://cloud.githubusercontent.com/assets/786644/9218257/bc8e64c6-4085-11e5-96f7-f07080f310b0.png)
- Crystal clear font and a nice mobile-friendly interface:  
<img src="https://cloud.githubusercontent.com/assets/786644/12805854/c5c15d50-cab4-11e5-8b8c-acae74dd8c3a.PNG" width=320/>
- Filter by text and log type:  
<img src="https://cloud.githubusercontent.com/assets/786644/12805875/fcfd3686-cab4-11e5-851f-ab82e309b8e7.PNG" width=320/>
<img src="https://cloud.githubusercontent.com/assets/786644/12805880/16a506cc-cab5-11e5-8315-75af62cc164f.PNG" width=320/>
- Collapse similar elements:  
<img src="https://cloud.githubusercontent.com/assets/786644/12805895/4bd77956-cab5-11e5-8cf6-88ba9856e930.PNG" width=320/>
- Tap log entry to view the stack trace:  
<img src="https://cloud.githubusercontent.com/assets/786644/12805978/9385167c-cab6-11e5-8bfc-ae9da0996358.PNG" width=320/>
- Scroll lock, copy-to-clipboard and e-mail options.
- Automatic updates!
 
## Installation
- **Automatic**:  
  Unity Editor Menu:  Window ▶ Lunar Mobile Console ▶ Install...
  
- **Manual**:  
  Drag'n'Drop `LunarConsol.prefab` (Assets/LunarConsole/Scripts/LunarConsole.prefab) into your current scene hierarchy and save your changes. You only need to do it once for your startup scene.

## Usage
You can open the console with a multi touch gesture or using the API call from a script (see [API Calls](#api-calls) for details).

### Configuration
- Select `LunarConsole` game object in the `Hierarchy` window.  
  <img src="https://cloud.githubusercontent.com/assets/786644/14589980/f4e4a06e-04a3-11e6-8c36-51dfb1637a1e.png"/>
- Find `Lunar Console` script settings in the `Inspector` window.  
  <img src="https://cloud.githubusercontent.com/assets/786644/14590014/f9c0d692-04a4-11e6-82b7-ae176dcedab3.png"/>  
  - Set the capacity (the maximum number of lines the console can hold). It's advised to keep this amount low (the more you have - the more memory it will take).
  - Set the trim amount (how many lines will be removed from the beginning of the log when console overflows).
  - Choose a gesture from the drop down list or select `None` to completely disable multi touch gestures (you would still be able to open the console from your scripts).

### Stack Trace Frames
Touch the log entry to view its stack trace.

*Important*: make sure to set the "Development Build" flag in your publishing settings otherwise you will only be able to see exceptions traces.

<img src="https://cloud.githubusercontent.com/assets/786644/14590134/707110ce-04a8-11e6-98cd-ac0acc9cd135.png"/>  
<img src="https://cloud.githubusercontent.com/assets/786644/14590133/7070bd04-04a8-11e6-8187-4b6f10389206.png"/>  

For more info see:
http://docs.unity3d.com/Manual/PublishingBuilds.html

### Enabling/Disabling Plugin

If Lunar Mobile Console is
- _enabled_ - the plugin files would appear in your iOS/Android build: you can access the console with a multi touch gesture or from a script.
- _disabled_ - the plugin files would **NOT** appear in your iOS/Android build: you can't access the console (multi touch gestures or API calls would be ignored).

To
- disable:  
  Window ▶ Lunar Mobile Console ▶ Disable
- enable:  
  Window ▶ Lunar Mobile Console ▶ Enable

For more detailed information check Wiki page: [Enabling and Disabling Lunar Mobile Console](https://github.com/SpaceMadness/lunar-unity-console/wiki/Enabling-and-Disabling-Lunar-Mobile-Console).

### Build System Support
You can enable/disable the plugin from the command line (and make it a part of your build process)

- To disable:  
  ```<UNITY_BIN_PATH>  -quit -batchmode  -executeMethod LunarConsolePluginInternal.Installer.DisablePlugin```
- To enable:  
  ```<UNITY_BIN_PATH>  -quit -batchmode  -executeMethod LunarConsolePluginInternal.Installer.EnablePlugin```

```<UNITY_BIN_PATH>``` locations:
- Mac OS X: ```/Applications/Unity/Unity.app/Contents/MacOS/Unity```
- Windows: ```c:\Program Files\Unity\Editor\Unity.exe```
- Linux: _TBD_
 
## Troubleshooting
Please, visit the [Troubleshooting](https://github.com/SpaceMadness/lunar-unity-console/wiki/Troubleshooting) Wiki page or post your question on the official forum [thread](http://forum.unity3d.com/threads/lunar-mobile-console-high-performance-unity-ios-android-logger-built-with-native-platform-ui.347650/).

## Miscellaneous
### Check for Updates
Window ▶ Lunar Mobile Console ▶ Check for updates...

### Bug Reports
Window ▶ Lunar Mobile Console ▶ Report bug...
 
### Contacts
For anything else: lunar.plugin@gmail.com

### Social Media
- Twitter: [@LunarPlugin](https://twitter.com/LunarPlugin)
- Facebook: [www.facebook.com/LunarPlugin](https://www.facebook.com/LunarPlugin)
- Unity Forums Thread: [[Lunar Mobile Console] High-performance Unity iOS/Android logger built with native platform UI](http://forum.unity3d.com/threads/lunar-mobile-console-high-performance-unity-ios-android-logger-built-with-native-platform-ui.347650/)

# Thanks for using Lunar Mobile Console!

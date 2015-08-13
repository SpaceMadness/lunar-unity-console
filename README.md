# Lunar Unity Mobile Console

Asset store link: TBA

Requires Unity 5.0 or later.

## Project Goals

Build a high-performance Unity iOS/Android logger using the native platform UI.  

## Platform Support
- **iOS**: beta version
- **Android**: TBA
  
## Key Benefits
- Heavily optimized native C/Objective-C code with a low memory footprint.
- Works well with a huge log amount (up to 100000 entries).
- Built with a native platform UI (does NOT rely on Unity GUI).
- Resolution independent (looks great on highres/retina displays).
- Does NOT modify scenes or add assets.
- Removes completely from the release build with a single mouse click (absolutely NO traces left).

## Features
- Instant error notification (never miss an unhandled exception again):  
<img src="https://cloud.githubusercontent.com/assets/786644/9218202/21948cb6-4085-11e5-9173-8bf8ecc0a3f2.PNG" width=320/>
- Quick logger output access with a multi touch gesture:  
![guid-bfa239b6-b55a-4f88-82ea-744f01cd9d77-web](https://cloud.githubusercontent.com/assets/786644/9218257/bc8e64c6-4085-11e5-96f7-f07080f310b0.png)
- Crystal clear font and a nice mobile-friendly interface:  
<img src="https://cloud.githubusercontent.com/assets/786644/9218239/8233282a-4085-11e5-9304-45698b89dde4.PNG" width=320/>
- Filter by text and log type:  
<img src="https://cloud.githubusercontent.com/assets/786644/9218396/01f6dee8-4087-11e5-8ac0-09c795c657b6.PNG" width=320/>
<img src="https://cloud.githubusercontent.com/assets/786644/9218405/166283e6-4087-11e5-9f39-35d34bfdf6c1.PNG" width=320/>
- Scroll lock and copy-to-clipboard options
- Automatic updates!
 
## Installation
- **Automatic**:  
  Unity Editor Menu:  Window ▶ Lunar Mobile Console ▶ Install...
  
- **Manual**:  
  Drag'n'Drop `LunarConsol.prefab` (Assets/LunarConsole/Scripts/LunarConsole.prefab) into your current scene's hierarchy and save your changes. You only need to do it once for your startup scene.

## Enable/Disable plugin for debug/release
- To disable:  
  Window ▶ Lunar Mobile Console ▶ Disable
- To re-enable:  
  Window ▶ Lunar Mobile Console ▶ Enable
  
  When disabled, the plugin source files and resources would NOT appear in the generated native platform project.
 
## Check for Updates
Window ▶ Lunar Mobile Console ▶ Check for updates...
 
## Contacts

For any questions: lunar.plugin@gmail.com

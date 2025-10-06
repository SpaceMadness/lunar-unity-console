# Lunar Console Changelog

## v.1.9.1 - 10/05/2025

**Fixes:**

* Fixed handling **multi-touch** input on Unity 6.

## v.1.9.0 - 02/09/2025

**Fixes:**

* Fixed a crash on Android in Unity 6.
* Fixed a bug that prevented sending logs via email.

## v.1.8.5 - 10/22/2021

**Fixes:**

* Forced decimal points to **en-US** locale.

## v.1.8.4 - 7/16/2021

**Fixes:**

* **Android:** Fixed ProGuard obfuscation.

## v.1.8.3 - 5/18/2021

**Changes:**

* Export unfiltered log.

## v.1.8.2 - 5/16/2021

**Fixes:**

* Fixed rich text color tag hex value issues.
* Fixed rich text nested tag issues.

## v.1.8.1 - 11/12/2020

**Fixes:**

* Fixed iOS watchdog crash.

## v.1.8.0 - 08/07/2020

**Changes:**

* Enum variables support.

## v.1.7.0 - 07/29/2020

**Changes:**

* Rich Text support.
* Ability to toggle the plugin from a **BuildPostProcessor**.

## v.1.6.6 - 06/10/2020

**Fixes:**

* **Android**: Fixed crash on older Android devices.

## v.1.6.4 - 12/15/2019

**Fixes:**

* Fixed Unity 2020.x incompatibility.

## v.1.6.3 - 11/3/2019

**Fixes:**

* Fixed Unity 2019.3 incompatibility.

## v.1.6.2 - 4/5/2019

**Fixes:**

* Fixed exceptions while resolving variables.
* Fixed issue with properly disabling editor analytics.

**Changes:**

* Added ability to disable updater.

## v.1.6.1 - 4/4/2019

**Fixes:**

* Fixed iOS crash while using Log Overlay view with a custom filter.
* Fixed ProGuard minification.

## v.1.6.0 - 3/31/2019

**Changes:**

* Added log overlay customization.
* Added exception warning display modes.

**Fixes:**

* Fixed iPhone X notch safe area.

## v.1.5.7 - 1/25/2019

**Fixes:**

* Fixed resolving variables in Unity at runtime.

## v.1.5.6 - 1/19/2019

**Fixes:**

* Fixed resolving variables in Unity 2018.3.x.

## v.1.5.5 - 11/30/2018

**Fixes:**

* **Android:** Fixed black settings screen when building on Unity 2018.x.

## v.1.5.4 - 4/26/2018

**Fixes:**

* Fixed registering variables in Unity 2017.4.1f1.

## v.1.5.3 - 12/27/2017

**Fixes:**

* Fixed app blocking after minimize/restore.

## v.1.5.2 - 9/14/2017

**Fixes:**

* Fixed `NullReferenceException` while cleaning legacy files.

## v.1.5.1 - 8/28/2017

**Fixes:**

* Fixed issue where console variables were not properly loaded and duplicated.

## v.1.5.0 - 8/20/2017

**New:**

* Console actions can now accept an optional parameter.

**Changes:**

* `LunarConsoleActions` is now deprecated.

## v.1.4.9 - 8/19/2017

**Fixes:**

* Fixed issue where console variables were not properly serialized after reset in editor mode.

## v.1.4.8 - 8/18/2017

**Fixes:**

* Fixed iOS build portability issues.

## v.1.4.7 - 8/1/2017

**New:**

* Variable flags.

**Fixes:**

* Fixed 'onConsoleClosed' callbacks not being called when dismissing the console programmatically.

## v.1.4.6 - 7/11/2017

**Fixes:**

* **Android:** Fixed `java.lang.NullPointerException`: Attempt to invoke virtual method 'void spacemadness.com.lunarconsole.console.WarningView.destroy()' on a null object reference.

## v.1.4.5 - 6/26/2017

**Fixes:**

* **iOS:** Fixed `NSRangeException` in the log overlay controller.
* Fixed editor actions bug.

## v.1.4.4 - 6/19/2017

**Fixes:**

* **iOS:** Suppressed 'Stale touch detected!' warning.

## v.1.4.3 - 5/16/2017

**New:**

* Default **e-mail** configuration.

## v.1.4.2 - 5/15/2017

**Changes:**

* Added `LUNAR_CONSOLE_ANALYTICS_DISABLED` preprocessor define to disable analytics.

## v.1.4.1 - 5/2/2017

**Fixes:**

* **Android:** Fixed build issue on Windows Editor.

## v.1.4.0 - 4/27/2017

**New:**

* Added **'Actions & Variables'** window.

## v.1.3.0 - 4/12/2017

**Changes:**

* **Android**: Replaced legacy plugin installation (`Assets\Plugins\Android\LunarConsole`) with a single `aar` file.
* **iOS**: Removed texture compression for plugin images to decrease build and platform switching time.

## v.1.2.2 - 4/4/2017

**Fixes:**

* Fixed **auto-updater**.
* **Android**: Fixed wrong layout with large log message.

## v.1.2.1 - 4/3/2017

**Fixes:**

* **iOS**: Fixed wrong layout with large log message.

## v.1.2.0 - 3/31/2017

**Fixes:**

* **Android**: Fixed Unity 5.6 incompatibility bugs.

## v.1.1.1 - 3/27/2017

**Fixes:**

* **iOS**: Fixed gesture recognizer after editing a Cvar.

## v.1.1.0 - 3/12/2017

**New:**

* Added Range Variables.
* Added ability to enable/disable the console programmatically.

**Fixes:**

* Fixed user-defined variables UI bug.
* Fixed button icons colors and compression.
* **Android**: Fixed `Gradle (New)` build system.
* **iOS**: Fixed popup controllers bug.

## v.1.0.0 - 2/15/2017

**New:**

* Added FREE version.

**Fixes:**

* **Android**: Fixed filtering **case-sensitive** actions and variables.

## v.0.9.0 - 1/30/2017

**New:**

* Added Variables support. For more information see: https://goo.gl/in0obv

**Fixes:**

* Fixed **UI** layout.

## v.0.8.0 - 1/21/2017

**New:**

* Added Actions support. For more information see: https://goo.gl/in0obv

**Fixes:**

* **iOS**: Fixed Unity Cloud Build error '`[xcode] xcodebuild: error: Unable to read project Unity-iPhone.xcodeproj.`'
* **Android**: Fixed '`JNI ERROR (app bug): local reference table overflow (max=512)`'

**Changes:**
* Renamed Unity Cloud Build 'Pre-Export Method Name' from `LunarConsolePluginInternal.Installer.*` to `LunarConsoleEditorInternal.Installer.*`

## v.0.7.0 - 12/7/2016

**New:**

* Added support for console **move/resize**.

**Fixes:**

* **iOS**: Xcode 7 compatibility and **UI** fixes.
* **Android**: Android Nougat compatibility fixes.

**Improvements:**

* Added **move/resize** feature.
* **iOS**: Improved memory usage.

## v.0.6.1 - 9/19/2016

**Fixes:**

* Fixed multithreaded logging.
* Fixed Vuforia plugin compatibility issues.

## v.0.6.0 - 8/29/2016

**Improvements:**

* Added a transparent log overlay view.
* Added plugin settings.

## v.0.5.0 - 8/16/2016

**Fixes:**

* Fixed Unity 5.4 compatibility bugs.
* **iOS**: Fixed multiple invocation of console callbacks.

**Improvements:**

* Added an option to remove rich text tags from the output.
* Added stack trace for **e-mail** / clipboard log text.

## v.0.4.2b - 7/20/2016

**Fixes:**

* **iOS**: Fixed potential linking issue while building for **IL2CPP** script backend.

## v.0.4.1b - 6/15/2016

**Fixes:**

* **iOS**: Fixed warning text for missing stack trace frames.

## v.0.4.0b - 6/15/2016

**Fixes:**

* **iOS**: Fixed sending log **e-mails**.

## v.0.3.0b - 4/16/2016

**Fixes:**

* Fixed scroll lock bug.

**Improvements:**

* Added `LunarConsole.onConsoleOpened` and `LunarConsole.onConsoleClosed` callbacks.

**Changes:**

* Auto scrolling is turned **on** every time console is opened (would be configurable in a future release).
* Renamed `LunarConsole` to `LunarConsolePlugin`.
* Renamed `LunarConsoleInternal` to `LunarConsolePluginInternal`.

## v.0.2.0b - 2/14/2016

**Improvements:**

* **Android**: Added 'collapse similar entries' option.

## v.0.1.0b - 2/3/2016

**Improvements:**

* **iOS**: Added 'collapse similar entries' option.

## v.0.0.10b - 1/24/2016

**Improvements:**

* Added `LunarConsole.Clear()` method to clear the console output.

## v.0.0.9b - 1/23/2016

**Improvements:**

* Added an option to disable **multi-touch** gesture recognition.

**Fixes:**

* **Android**: Fixed a conflicting between **2-finger** swipe down and zoom (pinch) gesture recognition.
* **Editor**: Fixed 'Report bug...' URL.

## v.0.0.8b - 11/30/2015

**Improvements:**

* **Android**: Added stack trace info for every log entry.
* **iOS**: Improved stack trace info for every log entry.

**Fixes:**

* **Android**: Fixed touches passing through some **area** of the console into the scene.

## v.0.0.7b - 11/23/2015

**Fixes:**

* **Android**: Fixed clicks through exception warning.

## v.0.0.6b - 11/17/2015

**Fixes:**

* **Android**: Resolved conflicts with **third-party** Android plugins (GPGS, Facebook, etc).
* **iOS**: Fixed tab bar buttons layout.

**Improvements:**

* Dramatically reduced plugin size (from 1.6M to 250k)
* Optimized **UI** resources.

## v.0.0.5b - 11/11/2015

**Fixes:**

* **iOS**: Fixed poor scrolling **performance**.
* **iOS**: Dismiss keyboard when pressing **'Search'** button for log filtering.
* **iOS**: Fixed missing **'ShowConsole'** and **'HideConsole'** API calls.

**Improvements:**

* Better overflow management.

## v.0.0.4b - 10/13/2015

**New:**

* Added scroll-to-top gesture (tap status bar to see first log entry).

**Fixes:**

* **iOS**: Fixed **'scroll lock'** button state.

## v.0.0.3b - 10/6/15

**New:**

* Android platform support.
* Enable/Disable plugin from your build system.

**Improvements:**

* **Android**: Stop scrolling on touch-and-drag.
* Destroy LunarConsole game object when running on unsupported platforms (everything except iOS and Android).
* Added **'ShowConsole'** and **'HideConsole'** methods.

**Fixes:**

* Fixed change log messages for **auto-updater**.

## v.0.0.2b - 9/2/2015

**Improvements:**

* Preserve console **UI** state between sessions.

**Fixes:**

* Fixed filtering after console **UI** is closed.

## v.0.0.1b - 8/25/2015

* Initial release.
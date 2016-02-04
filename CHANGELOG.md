# Lunar Console Changelog

## v.0.1.0b - 2/3/2016

**Improvements:**

* **iOS**: collapse similar entries option 

## v.0.0.10b - 1/24/2016

**Improvements:**

* Added LunarConsole.Clear() method to clear the console output.

## v.0.0.9b - 1/23/2016

**Improvements:**

* Added an option to disable multi touch gesture recognition.

**Fixes:**

* **Android**: fixed a conflicting between 2-finger swipe down and zoom (pinch) gesture recognition.
* **Editor**: fixed 'Report bug...' url

## v.0.0.8b - 11/30/2015

**Improvements:**

* **Android**: added stack trace info for every log entry.
* **iOS**: improved stack trace info for every log entry.

**Fixes:**

* **Android**: fixed touches passing through some are of the console into the scene.

## v.0.0.7b - 11/23/2015

**Fixes:**

* **Android**: fixed clicks through exception warning.

## v.0.0.6b - 11/17/2015

**Fixes:**

* **Android**: resolved conflicts with 3rd party Android plugins (GPGS, Facebook, etc).
* **iOS**: fixed tab bar buttons layout.

**Improvements:**

* Dramatically reduced plugin size (from 1.6M to 250k)
* Optimized UI resources.

## v.0.0.5b - 11/11/2015

**Fixes:**

* **iOS**: fixed poor scrolling perfomance.
* **iOS**: dismiss keyboard when pressing 'Search' button for log filtering.
* **iOS**: fixed missing 'ShowConsole' and 'HideConsole' API calls.

**Improvements:**

* Better overflow management.

## v.0.0.4b - 10/13/2015

**New:**

* Added scroll-to-top gesture (tap status bar to see first log entry).

**Fixes:**

* **iOS**: fixed "scroll lock" button state.

## v.0.0.3b - 10/6/15

**New:**

* Android platform support (FUCK YES!!!).
* Enable/Disable plugin from your build system.

**Improvements:**

* **Android**: stop scrolling on touch-and-drag.
* Destroy LunarConsole game object when running on unsupported platforms (everything except iOS and Android).
* Added 'ShowConsole' and 'HideConsole' methods.

**Fixes:**

* Fixed change log messages for auto updater.

## v.0.0.2b - 9/2/2015

**Improvements:**

* Preserve console UI state between sessions.

**Fixes:**  

* Fixed filtering after console UI is closed.

## v.0.0.1b - 8/25/2015

* Initial release.

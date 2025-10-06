# Unity symlinks
## MacOS
- TBD
## Linux
- TBD
## Windows
- `mklink /D "C:\Program Files\Unity-Publish" "C:\Program Files\Unity\Hub\Editor\2019.4.0f1"`
- `mklink /D "C:\Program Files\Unity-Export" "C:\Program Files\Unity\Hub\Editor\6000.2.6f2"`

# JDK
## MacOS
TBD
## Linux
TBD
## Windows
- `winget install Microsoft.OpenJDK.17`
- `setx JAVA_HOME "C:\Program Files\Microsoft\jdk-17.0.16.8-hotspot" -m`

# Android SDK
## MacOS
- TBD
## Linux
- TBD
## Windows
- `setx ANDROID_HOME "%USERPROFILE%\AppData\Local\Android\Sdk" -m`
- `setx PATH "%PATH%;%ANDROID_HOME%\platform-tools" -m`

# Builder
Python 3.13.5
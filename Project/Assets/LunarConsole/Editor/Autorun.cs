//
//  Autorun.cs
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2017 Alex Lementuev, SpaceMadness.
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

using UnityEngine;
using UnityEditor;

using LunarConsolePlugin;
using LunarConsolePluginInternal;

using System;
using System.IO;
using System.Collections;

namespace LunarConsoleEditorInternal
{
    [InitializeOnLoad]
    static class Autorun
    {
        private static readonly string[] kLegacyAssets =
        {
            "Assets/Plugins/Android/LunarConsole",

            "Assets/LunarConsole/Editor/Android/AndroidManifest.xml",
            "Assets/LunarConsole/Editor/Android/libs",
            "Assets/LunarConsole/Editor/Android/project.properties",
            "Assets/LunarConsole/Editor/Android/res",

            "Assets/Plugins/LunarConsole/Editor/Android/AndroidManifest.xml",
            "Assets/Plugins/LunarConsole/Editor/Android/libs",
            "Assets/Plugins/LunarConsole/Editor/Android/project.properties",
            "Assets/Plugins/LunarConsole/Editor/Android/res",
        };

        static Autorun()
        {
            AndroidPlugin.SetEnabled(LunarConsoleConfig.consoleEnabled);
            CleanLegacyFiles(); // automatically fix old installations

            Updater.TryCheckForUpdates();
            LunarConsoleEditorAnalytics.TrackPluginVersionUpdate();
        }

        static void CleanLegacyFiles()
        {
            foreach (var assetPath in kLegacyAssets)
            {
                try
                {
                    if (AssetDatabase.DeleteAsset(assetPath))
                    {
                        Debug.LogWarning("Deleted legacy asset: " + assetPath);
                    }
                }
                catch (NullReferenceException e) // see: https://forum.unity.com/threads/lunar-mobile-console-high-performance-unity-ios-android-logger-built-with-native-platform-ui.347650/page-5#post-3215675
                {
                    Log.d("Exception while deleting asset '{0}': {1}", assetPath, e);
                }
                catch (Exception e)
                {
                    Debug.LogWarningFormat("Exception while deleting asset '{0}': {1}", assetPath, e);
                }
            }
        }
    }
}

//
//  MenuItems.cs
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2016 Alex Lementuev, SpaceMadness.
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
using UnityEditorInternal;

namespace LunarConsoleInternal
{
    static class MenuItems
    {
        [MenuItem("Window/Lunar Mobile Console/Install...")]
        static void Install()
        {
            bool silent = !InternalEditorUtility.isHumanControllingUs;
            Installer.Install(silent);
        }

        [MenuItem("Window/Lunar Mobile Console/")]
        static void Separator()
        {
        }

        [MenuItem("Window/Lunar Mobile Console/Check for updates...")]
        static void CheckForUpdates()
        {
            Updater.CheckForUpdates(false);
        }

        [MenuItem("Window/Lunar Mobile Console/Report bug...")]
        static void RequestFeature()
        {
            Application.OpenURL("https://github.com/SpaceMadness/lunar-unity-console/issues/new");
        }

        #if LUNAR_DEVELOPMENT
        [MenuItem("Window/Lunar Mobile Console/Reset")]
        static void Reset()
        {
            Updater.Reset();
        }
        #endif

        #if LUNAR_DEVELOPMENT && UNITY_ANDROID
        [MenuItem("Window/Lunar Mobile Console/Force update plugin")]
        static void ForceUpdatePlugin()
        {
            AndroidPlugin.ForceUpdateFiles();
        }
        #endif
    }
}

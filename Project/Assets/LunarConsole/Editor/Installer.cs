//
//  Installer.cs
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

﻿using UnityEngine;
using UnityEditor;

using System.Collections;
using System.IO;

using LunarConsolePlugin;

namespace LunarConsolePluginInternal
{
    using Editor = LunarConsolePlugin.Editor;

    static class Installer
    {
        public static void Install(bool silent = true)
        {
            string prefabPath = EditorConstants.PrefabPath;
            string messageTitle = Constants.PluginDisplayName;

            string objectName = Path.GetFileNameWithoutExtension(prefabPath);

            if (!silent && !Utils.ShowDialog(messageTitle, "This will create " + objectName + " (" + prefabPath + ") game object in your scene.\n\nYou only need to do it once for the very first scene of your game\n\nContinue?"))
            {
                return;
            }

            LunarConsole[] existing = GameObject.FindObjectsOfType<LunarConsole>();
            if (existing != null)
            {
                foreach (LunarConsole c in existing)
                {
                    GameObject.DestroyImmediate(c.gameObject);
                }
            }

            GameObject lunarConsolePrefab = AssetDatabase.LoadAssetAtPath(prefabPath, typeof(GameObject)) as GameObject;
            if (lunarConsolePrefab == null)
            {
                if (!silent)
                {
                    Utils.ShowDialog(messageTitle, "Can't instantiate " + prefabPath + ": asset not found");
                }
                return;
            }

            GameObject lunarConsole = PrefabUtility.InstantiatePrefab(lunarConsolePrefab) as GameObject;
            lunarConsole.name = objectName;

            if (!silent)
            {
                Utils.ShowDialog(messageTitle, objectName + " game object created!\n\nDon't forget to save your scene changes!", "OK");
            }
        }

        static void EnablePlugin()
        {
            SetLunarConsoleEnabled(true);
        }

        static void DisablePlugin()
        {
            SetLunarConsoleEnabled(false);
        }

        static void SetLunarConsoleEnabled(bool enabled)
        {
            AndroidPlugin.UpdateFiles(enabled); // we need to update plugin files first
            Editor.SetLunarConsoleEnabled(enabled); // then modify preprocessor's define
        }
    }
}

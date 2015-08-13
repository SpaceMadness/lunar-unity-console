//
//  Installer.cs
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2015 Alex Lementuev, SpaceMadness.
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

namespace LunarConsoleInternal
{
    using LunarConsole = LunarConsole.LunarConsole;

    static class Installer
    {
        static readonly string kMessageBoxTitle = "Lunar Mobile Console";
        static readonly string kPrefabAssetPath = "Assets/LunarConsole/Scripts/LunarConsole.prefab";

        public static void Install(bool silent = true)
        {
            string objectName = Path.GetFileNameWithoutExtension(kPrefabAssetPath);

            if (!silent && !Utils.ShowDialog(kMessageBoxTitle, "This will create " + objectName + " (" + kPrefabAssetPath + ") game object in your scene.\n\nYou only need to do it once for the very first scene of your game\n\nContinue?"))
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

            GameObject lunarConsolePrefab = AssetDatabase.LoadAssetAtPath(kPrefabAssetPath, typeof(GameObject)) as GameObject;
            if (lunarConsolePrefab == null)
            {
                if (!silent)
                {
                    Utils.ShowDialog(kMessageBoxTitle, "Can't instantiate " + kPrefabAssetPath + ": asset not found");
                }
                return;
            }

            GameObject lunarConsole = PrefabUtility.InstantiatePrefab(lunarConsolePrefab) as GameObject;
            lunarConsole.name = objectName;

            if (!silent)
            {
                Utils.ShowDialog(kMessageBoxTitle, objectName + " game object created!\n\nDon't forget to save your scene changes!", "OK");
            }
        }
    }
}

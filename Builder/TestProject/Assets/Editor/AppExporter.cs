//
//  AppExporter.cs
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

using System.IO;
using System.Collections;

namespace LunarConsolePluginInternal
{
    public class AppExporter
    {
        private static readonly string BuildsDir = "Build";

        internal static void BuildAll()
        {
            PerformAndroidBuild();
            PerformiOSBuild();
        }

        internal static void PerformiOSBuild()
        {
            string outDir = BuildsDir + "/iOS";
            Cleanup(outDir);
            
            #if UNITY_4_6
            BuildTarget target = BuildTarget.iPhone;
            #else
            BuildTarget target = BuildTarget.iOS;
            #endif
            
            EditorUserBuildSettings.SwitchActiveBuildTarget(target);
            BuildPipeline.BuildPlayer(GetScenePaths(), outDir, target, BuildOptions.None);
        }

        internal static void PerformAndroidBuild()
        {
            string outDir = BuildsDir + "/Android";
            Cleanup(outDir);
            
            string productName = PlayerSettings.productName;
            
            EditorUserBuildSettings.SwitchActiveBuildTarget(BuildTarget.Android);
            BuildPipeline.BuildPlayer(GetScenePaths(), outDir + "/" + productName + ".apk", BuildTarget.Android, BuildOptions.None);
        }

        private static string[] GetScenePaths()
        {
            string[] scenes = new string[EditorBuildSettings.scenes.Length];
            
            for(int i = 0; i < scenes.Length; i++)
            {
                scenes[i] = EditorBuildSettings.scenes[i].path;
            }
            
            return scenes;
        }

        private static void Cleanup(string dir)
        {
            if (Directory.Exists(dir))
            {
                Directory.Delete(dir, true);
            }
            Directory.CreateDirectory(dir);
        }
    }
}

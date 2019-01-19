//
//  AppExporter.cs
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2019 Alex Lementuev, SpaceMadness.
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

namespace LunarConsoleEditorInternal
{
    public class AppExporter
    {
        private static readonly string buildsDir = "Build";
        private static readonly BuildOptions buildOptions = BuildOptions.Development | BuildOptions.AllowDebugging;

        [MenuItem("Window/Lunar Mobile Console/Build/All")]
        internal static void PerformAllBuilds()
        {
            PerformIOSBuild();
            PerformAndroidBuild();
        }

        [MenuItem("Window/Lunar Mobile Console/Build/iOS")]
        internal static void PerformIOSBuild()
        {
            string outDir = buildsDir + "/iOS";
            Cleanup(outDir);
            
            #if UNITY_4_6
            BuildTarget target = BuildTarget.iPhone;
            #else
            BuildTarget target = BuildTarget.iOS;
            #endif

            #pragma warning disable 0618
            EditorUserBuildSettings.SwitchActiveBuildTarget(target);
            #pragma warning restore 0618
            EditorUserBuildSettings.allowDebugging = true;
            BuildPipeline.BuildPlayer(GetScenePaths(), outDir, target, buildOptions);
        }

        [MenuItem("Window/Lunar Mobile Console/Build/Android")]
        internal static void PerformAndroidBuild()
        {
            string outDir = buildsDir + "/Android";
            Cleanup(outDir);
            
            string productName = PlayerSettings.productName;

            #pragma warning disable 0618
            EditorUserBuildSettings.SwitchActiveBuildTarget(BuildTarget.Android);
            #pragma warning restore 0618
            EditorUserBuildSettings.allowDebugging = true;
            BuildPipeline.BuildPlayer(GetScenePaths(), outDir + "/" + productName + ".apk", BuildTarget.Android, buildOptions);
        }

        internal static void SwitchToIOSBuildTarget()
        {
            #pragma warning disable 0618
            EditorUserBuildSettings.SwitchActiveBuildTarget(BuildTarget.iOS);
            #pragma warning restore 0618
            EditorUserBuildSettings.allowDebugging = true;
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

//
//  BuildPostProcessor.cs
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

﻿using UnityEngine;
using UnityEditor;
using UnityEditor.Callbacks;
using System;

#if UNITY_IOS || UNITY_IPHONE
using UnityEditor.iOS.Xcode;
#endif

using System.Collections;
using System.IO;

using LunarConsolePluginInternal;

namespace LunarConsoleEditorInternal
{
    static class BuildPostProcessor
    {
        #if UNITY_IOS || UNITY_IPHONE
        [PostProcessBuild(1000)]
        static void OnPostprocessBuild(BuildTarget target, string buildPath)
        {
            if (LunarConsoleConfig.consoleEnabled)
            {
                if (target == BuildTarget.iOS)
                {
                    OnPostprocessIOS(buildPath);
                }
            }
        }

        static void OnPostprocessIOS(string buildPath)
        {
            // Workaround for:
            // FileNotFoundException: Could not load file or assembly 'UnityEditor.iOS.Extensions.Xcode, Version=1.0.0.0, Culture=neutral, PublicKeyToken=null' or one of its dependencies.
            // For more information see: http://answers.unity3d.com/questions/1016975/filenotfoundexception-when-using-xcode-api.html
            var projectMod = new XcodeProjMod(buildPath);
            projectMod.UpdateProject();
        }
        #endif //UNITY_IOS || UNITY_IPHONE
    }

    #if UNITY_IOS || UNITY_IPHONE
    class XcodeProjMod
    {
        private readonly string m_buildPath;
        private readonly string m_projectPath;


        public XcodeProjMod(string buildPath)
        {
            m_buildPath = buildPath;
            m_projectPath = PBXProject.GetPBXProjectPath(buildPath);
        }

        public void UpdateProject()
        {
            var project = new PBXProject();
            project.ReadFromFile(m_projectPath);

            string[] files = Directory.GetFiles(EditorConstants.EditorPathIOS, "*.projmods", System.IO.SearchOption.AllDirectories);
            foreach (string file in files)
            {
                ApplyMod(project, file);
            }

            project.WriteToFile(m_projectPath);
        }

        void ApplyMod(PBXProject project, string modFile)
        {
            var json = File.ReadAllText(modFile);
            var mod = JsonUtility.FromJson<ProjMod>(json);
            var sourceDir = Directory.GetParent(modFile).FullName;
            var targetGroup = "Libraries/" + mod.group;
            var targetGuid = project.TargetGuidByName(PBXProject.GetUnityTargetName());
            var dirProject = Directory.GetParent(PBXProject.GetPBXProjectPath(m_buildPath)).FullName;
            foreach (var file in mod.files)
            {
                var filename = Path.GetFileName(file);
                var fileGuid = project.AddFile(FileUtils.FixPath(FileUtils.MakeRelativePath(dirProject, sourceDir + "/" + file)), targetGroup + "/" + filename, PBXSourceTree.Source);
                if (filename.EndsWith(".h"))
                {
                    continue;
                }

                project.AddFileToBuild(targetGuid, fileGuid);
            }
            foreach (var framework in mod.frameworks)
            {
                project.AddFrameworkToProject(targetGuid, framework, false);
            }
        }
    }

    #pragma warning disable 0649
    #pragma warning disable 0414

    [System.Serializable]
    class ProjMod
    {
        public string group;
        public string[] frameworks;
        public string[] files;
    }

    #pragma warning restore 0649
    #pragma warning restore 0414

    #endif // UNITY_IOS || UNITY_IPHONE
}

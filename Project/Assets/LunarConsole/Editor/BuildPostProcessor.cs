using UnityEngine;
using UnityEditor;
using UnityEditor.Callbacks;
using UnityEditor.XCodeEditor.LunarConsole;

using System.Collections;
using System.IO;

using LunarConsole;

namespace LunarConsoleInternal
{
    static class BuildPostProcessor
    {
        [PostProcessBuild]
        static void OnPostprocessBuild(BuildTarget target, string pathToBuiltProject)
        {
            if (LunarConsoleSettings.consoleEnabled)
            {
                if (target.ToString() == "iOS" || target.ToString() == "iPhone")
                {
                    OnPostprocessIOS(pathToBuiltProject);
                }
            }
        }

        static void OnPostprocessIOS(string pathToBuiltProject)
        {
            XCProject project = new XCProject(pathToBuiltProject);

            string pluginDir = Path.Combine(Application.dataPath, "LunarConsole/Editor/iOS");
            string[] files = Directory.GetFiles(pluginDir, "*.projmods", System.IO.SearchOption.AllDirectories);
            foreach (string file in files)
            {
                project.ApplyMod(file);
            }

            project.Save();
        }

        [MenuItem("File/Run Post Process")]
        static void PostProcessBuild()
        {
            OnPostprocessIOS("/Users/weee/dev/projects/unity/lunar-console/Project/Build/iOS/Unity-iPhone.xcodeproj");
        }
    }
}

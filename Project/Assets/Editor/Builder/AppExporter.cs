using UnityEngine;
using UnityEditor;

using System.IO;
using System.Collections;

namespace LunarConsoleInternal
{
    public class AppExporter
    {
        private static readonly string BuildsDir = "Build";

        [MenuItem("Window/Lunar Mobile Console/Build/iOS")]
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

        [MenuItem("Window/Lunar Mobile Console/Build/Android")]
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

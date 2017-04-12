#if UNITY_EDITOR

using System.Collections;
using System.Collections.Generic;
using System.IO;

using UnityEngine;
using UnityEditor;

using System;

namespace LunarConsolePluginInternal
{
    public static class FileUtils
    {
        private static string projectDir;
        private static string assetsDir;

        static FileUtils()
        {
            projectDir = new DirectoryInfo(Application.dataPath).Parent.FullName; 
            assetsDir = new DirectoryInfo(Application.dataPath).FullName;
        }

        public static string GetAssetPath(params string[] pathComponents)
        {
            string path = pathComponents[0];
            for (int i = 1; i < pathComponents.Length; ++i)
            {
                path = Path.Combine(path, pathComponents[i]);
            }

            return FixPath(MakeRelativePath(assetsDir, path));
        }

        public static bool AssetPathExists(string assetPath)
        {
            var fullPath = GetFullAssetPath(assetPath);
            return File.Exists(fullPath) || Directory.Exists(fullPath);
        }

        public static string GetFullAssetPath(string path)
        {
            return Path.Combine(projectDir, path);
        }

        public static string FixPath(string path)
        {
            return path.Replace('\\', '/');
        }

        #pragma warning disable 0612
        #pragma warning disable 0618

        public static string MakeRelativePath(string parentPath, string filePath)
        {
            return Uri.UnescapeDataString(new Uri(parentPath, false).MakeRelative(new Uri(filePath)));
        }

        #pragma warning restore 0612
        #pragma warning restore 0618
    }
}

#endif // UNITY_EDITOR

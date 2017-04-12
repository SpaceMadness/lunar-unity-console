using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEditor;

using LunarConsolePluginInternal;

namespace LunarConsoleEditorInternal
{
    static class AndroidPlugin
    {
        public static void SetEnabled(bool enabled)
        {
            var androidPathAAR = EditorConstants.EditorPathAndroidAAR;
            if (androidPathAAR == null || !FileUtils.AssetPathExists(androidPathAAR))
            {
                Debug.LogErrorFormat("Can't {0} Android plugin: missing required file '{1}'. Re-install {2} to fix the issue.", 
                    enabled ? "enable" : "disable", 
                    androidPathAAR, 
                    Constants.PluginDisplayName);
                return;
            }

            var importer = (PluginImporter) PluginImporter.GetAtPath(androidPathAAR);
            if (importer == null)
            {
                Debug.LogErrorFormat("Can't {0} Android plugin: unable to create importer for '{1}'. Re-install {2} to fix the issue.", 
                    enabled ? "enable" : "disable", 
                    androidPathAAR, 
                    Constants.PluginDisplayName);
                return;
            }

            if (importer.GetCompatibleWithPlatform(BuildTarget.Android) != enabled)
            {
                importer.SetCompatibleWithPlatform(BuildTarget.Android, enabled);
                importer.SaveAndReimport();
            }
        }
    }
}

using UnityEngine;
using System.Collections;

namespace LunarConsoleInternal
{
    static class EditorConstants
    {
        public static readonly string PrefabPath = "Assets/" + Constants.PluginName + "/Scripts/" + Constants.PluginName + ".prefab";
        public static readonly string EditorPath = "Assets/" + Constants.PluginName + "/Editor";
        public static readonly string EditorPathIOS = EditorPath + "/iOS";
        public static readonly string EditorPathAndroid = EditorPath + "/Android";

        public static readonly string PluginAndroidPath = "Assets/Plugins/Android/" + Constants.PluginName;
    }
}

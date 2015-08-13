using UnityEngine;
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

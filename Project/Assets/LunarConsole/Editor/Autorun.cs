using UnityEngine;
using UnityEditor;

using System.Collections;

namespace LunarConsoleInternal
{
    [InitializeOnLoad]
    static class Autorun
    {
        static Autorun()
        {
            Updater.TryCheckForUpdates();
        }
    }
}

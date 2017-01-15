using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace LunarConsolePluginInternal
{
    static class Log
    {
        static readonly string TAG = "[" + Constants.PluginDisplayName + "]";

        public static void e(string format, params object[] args)
        {
            e(StringUtils.TryFormat(format, args));
        }

        public static void e(string message)
        {
            Debug.LogError(TAG + ": " + message);
        }

        public static void w(string format, params object[] args)
        {
            w(StringUtils.TryFormat(format, args));
        }

        public static void w(string message)
        {
            Debug.LogWarning(TAG + ": " + message);
        }
    }
}
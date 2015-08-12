using UnityEngine;

using System;
using System.Collections;

namespace LunarConsoleInternal
{
    static class Log
    {
        [System.Diagnostics.Conditional("LUNAR_DEVELOPMENT")]
        public static void d(string format, params object[] args)
        {
            Debug.Log(TryFormat(format, args));
        }

        [System.Diagnostics.Conditional("LUNAR_DEVELOPMENT")]
        public static void e(string format, params object[] args)
        {
            Debug.LogError(TryFormat(format, args));
        }

        [System.Diagnostics.Conditional("LUNAR_DEVELOPMENT")]
        public static void e(Exception e, string format, params object[] args)
        {
            Log.e(format, args);
            Debug.LogException(e);
        }

        private static string TryFormat(string format, object[] args)
        {
            try
            {
                return string.Format(format, args);
            }
            catch (Exception)
            {
                return format;
            }
        }
    }
}

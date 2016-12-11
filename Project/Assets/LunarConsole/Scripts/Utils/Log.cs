using UnityEngine;

using System;
using System.Collections.Generic;

namespace LunarConsolePluginInternal
{
    static class Log
    {
        public static void d(string message, params object[] args)
        {
            d((object) StringUtils.TryFormat(message, args));
        }

        public static void d(object message)
        {
            try
            {
                Debug.Log(message);
            }
            catch (Exception)
            {
            }
        }

        public static void w(string message, params object[] args)
        {
            w((object) StringUtils.TryFormat(message, args));
        }

        public static void w(object message)
        {
            try
            {
                Debug.LogWarning(message);
            }
            catch (Exception)
            {
            }
        }

        public static void e(string message, params object[] args)
        {
            e((object) StringUtils.TryFormat(message, args));
        }

        public static void e(object message)
        {
            try
            {
                Debug.LogError(message);
            }
            catch (Exception)
            {
            }
        }

        public static void e(Exception exception, string message, params object[] args)
        {
            e(exception, (object) StringUtils.TryFormat(message, args));
        }

        public static void e(Exception exception, object message)
        {
            try
            {
                Debug.LogError(message + ":" + exception.Message);
            }
            catch (Exception)
            {
            }
        }
    }
}

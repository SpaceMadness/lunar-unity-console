using System;
using System.Collections;
using System.Collections.Generic;

using UnityEngine;
using UnityEditor;

using LunarConsolePluginInternal;

namespace LunarConsoleEditorInternal
{
    static class LunarConsoleEditorAnalytics
    {
        private static readonly string kPrefsLastKnownVersion = Constants.EditorPrefsKeyBase + ".LastKnownVersion";

        /// <summary>
        /// Notifies the server about plugin update.
        /// </summary>
        public static void TrackPluginVersionUpdate()
        {
            if (LunarConsoleConfig.consoleEnabled && LunarConsoleConfig.consoleSupported)
            {
                var lastKnownVersion = EditorPrefs.GetString(kPrefsLastKnownVersion);
                if (lastKnownVersion != Constants.Version)
                {
                    EditorPrefs.SetString(kPrefsLastKnownVersion, Constants.Version);
                    TrackEvent("Version", "updated_version");
                }
            }
        }

        public static void TrackEvent(string category, string action, int value = LunarConsoleAnalytics.kUndefinedValue)
        {
            if (LunarConsoleConfig.consoleEnabled && LunarConsoleConfig.consoleSupported)
            {
                var payloadStr = LunarConsoleAnalytics.CreatePayload(category, action, value);
                Log.d("Event track payload: " + payloadStr);

                LunarConsoleHttpClient downloader = new LunarConsoleHttpClient(LunarConsoleAnalytics.TrackingURL);
                downloader.UploadData(payloadStr, delegate(string result, Exception error)
                {
                    if (error != null)
                    {
                        Log.e("Event track failed: " + error);
                    }
                    else
                    {
                        Log.d("Event track result: " + result);
                    }
                });
            }
        }
    }
}

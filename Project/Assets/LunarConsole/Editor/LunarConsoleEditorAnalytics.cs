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
            var lastKnownVersion = EditorPrefs.GetString(kPrefsLastKnownVersion);
            if (lastKnownVersion != Constants.Version)
            {
                EditorPrefs.SetString(kPrefsLastKnownVersion, Constants.Version);
                TrackEvent("new_version");
            }
        }

        public static void TrackEvent(string name, IDictionary<string, object> payload = null)
        {
            var payloadStr = LunarConsoleAnalytics.CreatePayload(name, payload);
            Log.d("Event track payload: " + payloadStr);

            LunarConsoleHttpDownloader downloader = new LunarConsoleHttpDownloader(LunarConsoleAnalytics.TrackingURL);
            downloader.UploadData(payloadStr, "POST", delegate(string result, Exception error) {
                if (error != null) {
                    Log.e("Event track failed: " + error);
                } else {
                    Log.d("Event track result: " + result);
                }
            });
        }
    }
}

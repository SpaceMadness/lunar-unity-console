using System;
using System.Collections;
using System.Collections.Generic;
using System.Net;

using UnityEngine;
using UnityEditor;

namespace LunarConsoleEditorInternal
{
    delegate void LunarConsoleHttpDownloaderCallback(string result, Exception error);

    class LunarConsoleHttpDownloader
    {
        private Uri m_uri;
        private WebClient m_client;

        public LunarConsoleHttpDownloader(string uri)
            : this(new Uri(uri))
        {
        }

        public LunarConsoleHttpDownloader(Uri uri)
        {
            if (uri == null)
            {
                throw new ArgumentNullException("Uri is null");
            }

            m_uri = uri;
            m_client = new WebClient();
        }

        public void UploadData(string data, string method, LunarConsoleHttpDownloaderCallback callback)
        {
            if (callback == null)
            {
                throw new ArgumentNullException("Callback is null");
            }

            if (m_client == null)
            {
                throw new InvalidOperationException("Already downloading something");
            }

            m_client.UploadDataCompleted += delegate(object sender, UploadDataCompletedEventArgs e)
            {
                Utils.DispatchOnMainThread(delegate()
                {
                    if (this.IsShowingProgress)
                    {
                        EditorUtility.ClearProgressBar();
                    }

                    if (!e.Cancelled)
                    {
                        callback(e.Result != null ? System.Text.Encoding.UTF8.GetString(e.Result) : null, e.Error);
                    }
                });
            };

            if (this.IsShowingProgress && EditorUtility.DisplayCancelableProgressBar("Lunar Mobile Console", "Connecting...", 1.0f))
            {
                Cancel();
            }

            m_client.UploadDataAsync(m_uri, method, System.Text.Encoding.UTF8.GetBytes(data));
        }

        public void DownloadString(LunarConsoleHttpDownloaderCallback callback)
        {
            if (callback == null)
            {
                throw new ArgumentNullException("Callback is null");
            }

            if (m_client == null)
            {
                throw new InvalidOperationException("Already downloading something");
            }

            m_client.DownloadStringCompleted += delegate(object sender, DownloadStringCompletedEventArgs e)
            {
                Utils.DispatchOnMainThread(delegate()
                {
                    if (this.IsShowingProgress)
                    {
                        EditorUtility.ClearProgressBar();
                    }

                    if (!e.Cancelled)
                    {
                        callback(e.Result, e.Error);
                    }
                });
            };

            if (this.IsShowingProgress && EditorUtility.DisplayCancelableProgressBar("Lunar Mobile Console", "Connecting...", 1.0f))
            {
                Cancel();
            }

            m_client.DownloadStringAsync(m_uri);
        }

        public void Cancel()
        {
            m_client.CancelAsync();
        }

        private static string ToHumanReadableLength(long bytes, bool addPrefix = true)
        {
            if (bytes < 1024)
            {
                return addPrefix ? string.Format("{0} bytes", bytes) : bytes.ToString();
            }

            if (bytes < 1024 * 1024)
            {
                float kbytes = bytes / 1024.0f;
                return addPrefix ? string.Format("{0} kb", kbytes.ToString("F1")) : kbytes.ToString("F1");
            }

            float mbytes = bytes / 1024.0f / 1024.0f;
            return addPrefix ? string.Format("{0} Mb", mbytes.ToString("F1")) : mbytes.ToString("F1");
        }

        public bool IsShowingProgress { get; set; }
    }
}
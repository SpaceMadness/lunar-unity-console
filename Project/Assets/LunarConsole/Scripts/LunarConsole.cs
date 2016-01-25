//
//  LunarConsole.cs
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2016 Alex Lementuev, SpaceMadness.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

#define LUNAR_CONSOLE_ENABLED

using UnityEngine;

#if UNITY_EDITOR
using UnityEditor;
using System.IO;
using System.Runtime.CompilerServices;
#endif

using System;
using System.Collections;
using System.Runtime.InteropServices;

using LunarConsoleInternal;

#if UNITY_EDITOR
[assembly: InternalsVisibleTo("Test")]
#endif

namespace LunarConsole
{
    public enum Gesture
    {
        None,
        SwipeDown
    }

    public class LunarConsole : MonoBehaviour
    {
        [Range(128, 65536)]
        [Tooltip("Logs will be trimmed to the capacity")]
        public int capacity = 4096;

        [Range(128, 65536)]
        [Tooltip("How many logs will be trimmed when console overflows")]
        public int trim = 512;

        [Tooltip("Gesture type to open the console")]
        public Gesture gesture = Gesture.SwipeDown;

        #if LUNAR_CONSOLE_ENABLED

        private static LunarConsole instance;
        private IPlatform platform;

        void Awake()
        {
            InitInstance();
        }

        void OnEnable()
        {
            InitInstance();
        }

        void InitInstance()
        {
            if (instance == null)
            {
                if (InitPlatform(capacity, trim))
                {
                    instance = this;
                    DontDestroyOnLoad(gameObject);
                }
                else
                {
                    Destroy(gameObject);
                }
            }
            else if (instance != this)
            {
                Destroy(gameObject);
            }
        }

        void ShowConsole()
        {
            if (platform != null)
            {
                platform.ShowConsole();
            }
        }

        void HideConsole()
        {
            if (platform != null)
            {
                platform.HideConsole();
            }
        }

        void ClearConsole()
        {
            if (platform != null)
            {
                platform.ClearConsole();
            }
        }

        bool InitPlatform(int capacity, int trim)
        {
            try
            {
                if (platform == null)
                {
                    trim = Math.Min(trim, capacity); // can't trim more that we have

                    platform = CreatePlatform(capacity, trim);
                    if (platform != null)
                    {
                        Application.logMessageReceived += delegate(string message, string stackTrace, LogType type) {
                            platform.OnLogMessageReceived(message, stackTrace, type);
                        };

                        return true;
                    }
                }
            }
            catch (Exception e)
            {
                Debug.LogError("Can't init " + Constants.PluginName + ": " + e.Message);
            }

            return false;
        }

        IPlatform CreatePlatform(int capacity, int trim)
        {
            #if UNITY_IOS || UNITY_IPHONE
            if (Application.platform == RuntimePlatform.IPhonePlayer)
            {
                return new PlatformIOS(Constants.Version, capacity, trim, GetGestureName(gesture));
            }
            #elif UNITY_ANDROID
            if (Application.platform == RuntimePlatform.Android)
            {
                return new PlatformAndroid(Constants.Version, capacity, trim, GetGestureName(gesture));
            }
            #endif

            return null;
        }

        static string GetGestureName(Gesture gesture)
        {
            return gesture.ToString();
        }

        interface IPlatform
        {
            void OnLogMessageReceived(string message, string stackTrace, LogType type);
            bool ShowConsole();
            bool HideConsole();
            void ClearConsole();
        }

        #if UNITY_IOS || UNITY_IPHONE

        class PlatformIOS : IPlatform
        {
            [DllImport("__Internal")]
            private static extern void __lunar_console_initialize(string version, int capacity, int trim, string gesture);
            
            [DllImport("__Internal")]
            private static extern void __lunar_console_log_message(string message, string stackTrace, int type);

            [DllImport("__Internal")]
            private static extern void __lunar_console_show();

            [DllImport("__Internal")]
            private static extern void __lunar_console_hide();

            [DllImport("__Internal")]
            private static extern void __lunar_console_clear();
            
            public PlatformIOS(string version, int capacity, int trim, string gesture)
            {
                __lunar_console_initialize(version, capacity, trim, gesture);
            }
            
            public void OnLogMessageReceived(string message, string stackTrace, LogType type)
            {
                __lunar_console_log_message(message, stackTrace, (int)type);
            }

            public bool ShowConsole()
            {
                __lunar_console_show();
                return true;
            }

            public bool HideConsole()
            {
                __lunar_console_hide();
                return true;
            }

            public void ClearConsole()
            {
                __lunar_console_clear();
            }
        }

        #elif UNITY_ANDROID

        class PlatformAndroid : IPlatform
        {
            private readonly object logLock = new object();

            private readonly jvalue[] args0 = new jvalue[0];
            private readonly jvalue[] args3 = new jvalue[3];

            private static readonly string PluginClassName = "spacemadness.com.lunarconsole.console.ConsolePlugin";

            private readonly AndroidJavaClass pluginClass;

            private readonly IntPtr pluginClassRaw;
            private readonly IntPtr methodLogMessage;
            private readonly IntPtr methodShowConsole;
            private readonly IntPtr methodHideConsole;
            private readonly IntPtr methodClearConsole;

            public PlatformAndroid(string version, int capacity, int trim, string gesture)
            {
                pluginClass = new AndroidJavaClass(PluginClassName);
                pluginClassRaw = pluginClass.GetRawClass();

                IntPtr methodInit = GetStaticMethod(pluginClassRaw, "init", "(Ljava.lang.String;IILjava.lang.String;)V");
                CallStaticVoidMethod(methodInit, new jvalue[] { jval(version), jval(capacity), jval(trim), jval(gesture) });

                methodLogMessage = GetStaticMethod(pluginClassRaw, "logMessage", "(Ljava.lang.String;Ljava.lang.String;I)V");
                methodShowConsole = GetStaticMethod(pluginClassRaw, "show", "()V");
                methodHideConsole = GetStaticMethod(pluginClassRaw, "hide", "()V");
                methodClearConsole = GetStaticMethod(pluginClassRaw, "clear", "()V");
            }

            ~PlatformAndroid()
            {
                pluginClass.Dispose();
            }

            #region IPlatform implementation
            
            public void OnLogMessageReceived(string message, string stackTrace, LogType type)
            {
                lock (logLock)
                {
                    args3[0] = jval(message);
                    args3[1] = jval(stackTrace);
                    args3[2] = jval((int)type);

                    CallStaticVoidMethod(methodLogMessage, args3);
                }
            }

            public bool ShowConsole()
            {
                try
                {
                    CallStaticVoidMethod(methodShowConsole, args0);
                    return true;
                }
                catch (Exception)
                {
                    return false;
                }
            }

            public bool HideConsole()
            {
                try
                {
                    CallStaticVoidMethod(methodHideConsole, args0);
                    return true;
                }
                catch (Exception)
                {
                    return false;
                }
            }

            public void ClearConsole()
            {
                try
                {
                    CallStaticVoidMethod(methodClearConsole, args0);
                }
                catch (Exception)
                {
                }
            }

            #endregion

            #region Helpers

            private static IntPtr GetStaticMethod(IntPtr classRaw, string name, string signature)
            {
                return AndroidJNIHelper.GetMethodID(classRaw, name, signature, true);
            }

            private void CallStaticVoidMethod(IntPtr method, jvalue[] args)
            {
                AndroidJNI.CallStaticVoidMethod(pluginClassRaw, method, args);
            }

            private bool CallStaticBoolMethod(IntPtr method, jvalue[] args)
            {
                return AndroidJNI.CallStaticBooleanMethod(pluginClassRaw, method, args);
            }

            private jvalue jval(string value)
            {
                jvalue val = new jvalue();
                val.l = AndroidJNI.NewStringUTF(value);
                return val;
            }

            private jvalue jval(int value)
            {
                jvalue val = new jvalue();
                val.i = value;
                return val;
            }

            #endregion
        }

        #endif // UNITY_ANDROID
        
        #endif // LUNAR_CONSOLE_ENABLED

        /// <summary>
        /// Shows Lunar console on top of everything. Does nothing if platform is not supported or if plugin is not initizlied.
        /// </summary>
        public static void Show()
        {
            #if LUNAR_CONSOLE_ENABLED
            if (instance != null)
            {
                instance.ShowConsole();
            }
            else
            {
                Debug.LogError("Can't show " + Constants.PluginName + ": instance is not initialized. Make sure you've installed it correctly");
            }
            #endif
        }

        /// <summary>
        /// Hides Lunar console. Does nothing if platform is not supported or if plugin is not initizlied.
        /// </summary>
        public static void Hide()
        {
            #if LUNAR_CONSOLE_ENABLED
            if (instance != null)
            {
                instance.HideConsole();
            }
            else
            {
                Debug.LogError("Can't hide " + Constants.PluginName + ": instance is not initialized. Make sure you've installed it correctly");
            }
            #endif
        }

        /// <summary>
        /// Clears Lunar console. Does nothing if platform is not supported or if plugin is not initizlied.
        /// </summary>
        public static void Clear()
        {
            #if LUNAR_CONSOLE_ENABLED
            if (instance != null)
            {
                instance.ClearConsole();
            }
            else
            {
                Debug.LogError("Can't clear " + Constants.PluginName + ": instance is not initialized. Make sure you've installed it correctly");
            }
            #endif
        }
    }

    public static class LunarConsoleSettings
    {
        #if LUNAR_CONSOLE_ENABLED
        public static readonly bool consoleEnabled = true;
        #else
        public static readonly bool consoleEnabled = false;
        #endif // LUNAR_CONSOLE_ENABLED
    }

    #if UNITY_EDITOR

    public static class Editor
    {
        #if LUNAR_CONSOLE_ENABLED
        [UnityEditor.MenuItem("Window/Lunar Mobile Console/Disable")]
        static void Disable()
        {
            SetLunarConsoleEnabled(false);
        }
        #else
        [UnityEditor.MenuItem("Window/Lunar Mobile Console/Enable")]
        static void Enable()
        {
            SetLunarConsoleEnabled(true);
        }
        #endif // LUNAR_CONSOLE_ENABLED

        public static void SetLunarConsoleEnabled(bool enabled)
        {
            string pluginFile = ResolvePluginFile();
            if (pluginFile == null)
            {
                PrintError(enabled, "can't resolve plugin file");
                return;
            }

            string sourceCode = File.ReadAllText(pluginFile);

            string oldToken = "#define " + (enabled ? "LUNAR_CONSOLE_DISABLED" : "LUNAR_CONSOLE_ENABLED");
            string newToken = "#define " + (enabled ? "LUNAR_CONSOLE_ENABLED" : "LUNAR_CONSOLE_DISABLED");

            string newSourceCode = sourceCode.Replace(oldToken, newToken);
            if (newSourceCode == sourceCode)
            {
                PrintError(enabled, "can't find '" + oldToken + "' token");
                return;
            }

            File.WriteAllText(pluginFile, newSourceCode);

            // TODO: write a better implementation
            string assetsPath = Directory.GetParent(Application.dataPath).FullName;
            string relativePath = pluginFile.Substring(assetsPath.Length + 1);
            AssetDatabase.ImportAsset(relativePath);
        }

        static string ResolvePluginFile()
        {
            try
            {
                string currentFile = new System.Diagnostics.StackTrace(true).GetFrame(0).GetFileName();
                if (currentFile != null && File.Exists(currentFile))
                {
                    return currentFile;
                }
            }
            catch (Exception)
            {
            }

            return File.Exists(Constants.PluginScriptPath) ? Constants.PluginScriptPath : null;
        }

        static void PrintError(bool flag, string message)
        {
            Debug.LogError("Can't " + (flag ? "enable" : "disable") + " Lunar Console: " + message);
        }
    }

    #endif // UNITY_EDITOR
}

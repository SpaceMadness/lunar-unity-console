#define LUNAR_CONSOLE_ENABLED

using UnityEngine;

#if UNITY_EDITOR
using UnityEditor;
using System.IO;
#endif

using System;
using System.Collections;
using System.Runtime.InteropServices;

namespace LunarConsole
{
    class LunarConsole : MonoBehaviour
    {
        [Range(1, 65536)]
        public int capacity = 4096;

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
                instance = this;
                DontDestroyOnLoad(gameObject);

                InitPlatform(capacity);
            }
            else if (instance != this)
            {
                Destroy(gameObject);
            }
        }

        void InitPlatform(int capacity)
        {
            if (platform == null)
            {
                platform = CreatePlatform(capacity);
                if (platform != null)
                {
                    Application.logMessageReceivedThreaded += delegate(string message, string stackTrace, LogType type) {
                        platform.OnLogMessageReceived(message, stackTrace, type);
                    };
                }
            }
        }

        IPlatform CreatePlatform(int capacity)
        {
            #if UNITY_IOS || UNITY_IPHONE
            if (Application.platform == RuntimePlatform.IPhonePlayer)
            {
                return new PlatformIOS(capacity);
            }
            #endif

            return null;
        }

        interface IPlatform
        {
            void OnLogMessageReceived(string message, string stackTrace, LogType type);
        }

        #if UNITY_IOS || UNITY_IPHONE

        class PlatformIOS : IPlatform
        {
            [DllImport("__Internal")]
            private static extern void __lunar_console_initialize(int capacity);
            
            [DllImport("__Internal")]
            private static extern void __lunar_console_log_message(string message, string stackTrace, int type);
            
            public PlatformIOS(int capacity)
            {
                __lunar_console_initialize(capacity);
            }
            
            public void OnLogMessageReceived(string message, string stackTrace, LogType type)
            {
                __lunar_console_log_message(message, stackTrace, (int)type);
            }
        }

        #endif // UNITY_IOS || UNITY_IPHONE
        
        #endif // LUNAR_CONSOLE_ENABLED
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
    static class LunarEditorMenu
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

        static void SetLunarConsoleEnabled(bool enabled)
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

            string pluginFile = Path.Combine(Application.dataPath, "LunarConsole/Scripts/LunarConsole.cs");
            return File.Exists(pluginFile) ? pluginFile : null;
        }

        static void PrintError(bool flag, string message)
        {
            Debug.LogError("Can't " + (flag ? "enable" : "disable") + " Lunar Console: " + message);
        }
    }
    #endif // UNITY_EDITOR
}

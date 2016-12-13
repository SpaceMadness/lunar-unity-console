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

#if UNITY_IOS || UNITY_IPHONE || UNITY_ANDROID
#define LUNAR_CONSOLE_PLATFORM_SUPPORTED
#endif

using UnityEngine;

#if UNITY_EDITOR
using UnityEditor;
using System.IO;
using System.Runtime.CompilerServices;
#endif

using System;
using System.Collections;
using System.Collections.Generic;
using System.Reflection;
using System.Runtime.InteropServices;
using System.Threading;

using LunarConsolePlugin;
using LunarConsolePluginInternal;

#if UNITY_EDITOR
[assembly: InternalsVisibleTo("Test")]
#endif

namespace LunarConsolePlugin
{
    public enum Gesture
    {
        None,
        SwipeDown
    }

    delegate void LunarConsoleNativeMessageCallback(string message);
    delegate void LunarConsoleNativeMessageHandler(IDictionary<string, string> data);

    public sealed class LunarConsole : MonoBehaviour
    {
        #pragma warning disable 0649
        #pragma warning disable 0414

        [Range(128, 65536)]
        [Tooltip("Logs will be trimmed to the capacity")]
        [SerializeField]
        int m_capacity = 4096;

        [Range(128, 65536)]
        [Tooltip("How many logs will be trimmed when console overflows")]
        [SerializeField]
        int m_trim = 512;

        [Tooltip("Gesture type to open the console")]
        [SerializeField]
        Gesture m_gesture = Gesture.SwipeDown;

        [Tooltip("If checked - removes <color>, <b> and <i> rich text tags from the output (may cause performance overhead)")]
        [SerializeField]
        bool m_removeRichTextTags;

        static LunarConsole s_instance;

        #pragma warning restore 0649
        #pragma warning restore 0414

        #if LUNAR_CONSOLE_ENABLED

        IPlatform m_platform;
        CRegistry m_registry;

        IDictionary<string, LunarConsoleNativeMessageHandler> m_nativeHandlerLookup;

        Queue<MessageInfo> m_queuedMessages;

        #region Life cycle

        void Awake()
        {
            InitInstance();
        }

        void OnEnable()
        {
            InitInstance();
        }

        void Update()
        {
            DispatchMessages();
        }

        void InitInstance()
        {
            if (s_instance == null)
            {
                if (InitPlatform(m_capacity, m_trim))
                {
                    s_instance = this;
                    DontDestroyOnLoad(gameObject);
                }
                else
                {
                    Destroy(gameObject);
                }
            }
            else if (s_instance != this)
            {
                Destroy(gameObject);
            }
        }

        #endregion

        #region Platforms

        bool InitPlatform(int capacity, int trim)
        {
            try
            {
                if (m_platform == null)
                {
                    trim = Math.Min(trim, capacity); // can't trim more that we have

                    m_platform = CreatePlatform(capacity, trim);
                    if (m_platform != null)
                    {
                        m_registry = new CRegistry();
                        m_registry.registryDelegate = m_platform;

                        m_queuedMessages = new Queue<MessageInfo>();
                        int mainThreadId = Thread.CurrentThread.ManagedThreadId;

                        Application.logMessageReceivedThreaded += delegate(string message, string stackTrace, LogType type)
                        {
                            message = m_removeRichTextTags ? StringUtils.RemoveRichTextTags(message) : message;
                            if (Thread.CurrentThread.ManagedThreadId == mainThreadId)
                            {
                                m_platform.OnLogMessageReceived(message, stackTrace, type);
                            }
                            else
                            {
                                QueueMessage(message, stackTrace, type);
                            }
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
                LunarConsoleNativeMessageCallback callback = NativeMessageCallback;
                return new PlatformIOS(gameObject.name, callback.Method.Name, Constants.Version, capacity, trim, GetGestureName(m_gesture));
            }
            #elif UNITY_ANDROID
            if (Application.platform == RuntimePlatform.Android)
            {
                LunarConsoleNativeMessageCallback callback = NativeMessageCallback;
                return new PlatformAndroid(gameObject.name, callback.Method.Name, Constants.Version, capacity, trim, GetGestureName(m_gesture));
            }
            #endif

            return null;
        }

        static string GetGestureName(Gesture gesture)
        {
            return gesture.ToString();
        }

        interface IPlatform : ICRegistryDelegate
        {
            void OnLogMessageReceived(string message, string stackTrace, LogType type);
            bool ShowConsole();
            bool HideConsole();
            void ClearConsole();
        }

        #region Threading

        void QueueMessage(string message, string stackTrace, LogType type)
        {
            lock (m_queuedMessages)
            {
                m_queuedMessages.Enqueue(new MessageInfo(message, stackTrace, type));
            }
        }

        void DispatchMessages()
        {
            if (m_queuedMessages != null)
            {
                lock (m_queuedMessages)
                {
                    while (m_queuedMessages.Count > 0)
                    {
                        var info = m_queuedMessages.Dequeue();
                        m_platform.OnLogMessageReceived(info.message, info.stackTrace, info.type);
                    }
                }
            }
        }

        #endregion

        #if UNITY_IOS || UNITY_IPHONE

        class PlatformIOS : IPlatform
        {
            [DllImport("__Internal")]
            private static extern void __lunar_console_initialize(string targetName, string methodName, string version, int capacity, int trim, string gesture);
            
            [DllImport("__Internal")]
            private static extern void __lunar_console_log_message(string message, string stackTrace, int type);

            [DllImport("__Internal")]
            private static extern void __lunar_console_show();

            [DllImport("__Internal")]
            private static extern void __lunar_console_hide();

            [DllImport("__Internal")]
            private static extern void __lunar_console_clear();

            [DllImport("__Internal")]
            private static extern void __lunar_console_action_register(int actionId, string name);

            [DllImport("__Internal")]
            private static extern void __lunar_console_action_unregister(int actionId);

            [DllImport("__Internal")]
            private static extern void __lunar_console_cvar_register(int variableId, string name, string type, string value, string defaultValue);

            [DllImport("__Internal")]
            private static extern void __lunar_console_cvar_update(int variableId, string value);

            /// <summary>
            /// Initializes a new instance of the iOS platform class.
            /// </summary>
            /// <param name="targetName">The name of the game object which will receive native callbacks</param>
            /// <param name="methodName">The method of the game object which will be called from the native code</param>
            /// <param name="version">Plugin version</param>
            /// <param name="capacity">Console capacity (elements over this amount will be trimmed)</param>
            /// <param name="trim">Console trim amount (how many elements will be trimmed on the overflow)</param>
            /// <param name="gesture">Gesture name to activate the console</param>
            public PlatformIOS(string targetName, string methodName, string version, int capacity, int trim, string gesture)
            {
                __lunar_console_initialize(targetName, methodName, version, capacity, trim, gesture);
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

            public void OnActionRegistered(CRegistry registry, CAction action)
            {
                __lunar_console_action_register(action.Id, action.Name);
            }

            public void OnActionUnregistered(CRegistry registry, CAction action)
            {
                __lunar_console_action_unregister(action.Id);
            }

            public void OnVariableRegistered(CRegistry registry, CVar cvar)
            {
                __lunar_console_cvar_register(cvar.Id, cvar.Name, cvar.Type.ToString(), cvar.Value, cvar.DefaultValue);
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

            /// <summary>
            /// Initializes a new instance of the Android platform class.
            /// </summary>
            /// <param name="targetName">The name of the game object which will receive native callbacks</param>
            /// <param name="methodName">The method of the game object which will be called from the native code</param>
            /// <param name="version">Plugin version</param>
            /// <param name="capacity">Console capacity (elements over this amount will be trimmed)</param>
            /// <param name="trim">Console trim amount (how many elements will be trimmed on the overflow)</param>
            /// <param name="gesture">Gesture name to activate the console</param>
            public PlatformAndroid(string targetName, string methodName, string version, int capacity, int trim, string gesture)
            {
                pluginClass = new AndroidJavaClass(PluginClassName);
                pluginClassRaw = pluginClass.GetRawClass();

                IntPtr methodInit = GetStaticMethod(pluginClassRaw, "init", "(Ljava.lang.String;Ljava.lang.String;Ljava.lang.String;IILjava.lang.String;)V");
                CallStaticVoidMethod(methodInit, new jvalue[] {
                    jval(targetName),
                    jval(methodName),
                    jval(version),
                    jval(capacity),
                    jval(trim),
                    jval(gesture)
                });

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

        #endregion

        #region Native callback

        void NativeMessageCallback(string param)
        {
            IDictionary<string, string> data = StringUtils.DeserializeString(param);
            string name = data["name"];
            if (string.IsNullOrEmpty(name))
            {
                Debug.LogError("Can't handle native callback: 'name' is undefined");
                return;
            }

            LunarConsoleNativeMessageHandler handler;
            if (!nativeHandlerLookup.TryGetValue(name, out handler))
            {
                Debug.LogError("Can't handle native callback: handler not found '" + name + "'");
                return;
            }

            try
            {
                handler(data);
            }
            catch (Exception e)
            {
                Debug.LogError("Exception while handling native callback (" + name + "): " + e.Message);
            }
        }

        IDictionary<string, LunarConsoleNativeMessageHandler> nativeHandlerLookup
        {
            get
            {
                if (m_nativeHandlerLookup == null)
                {
                    m_nativeHandlerLookup = new Dictionary<string, LunarConsoleNativeMessageHandler>();
                    m_nativeHandlerLookup["console_open"] = ConsoleOpenHandler;
                    m_nativeHandlerLookup["console_close"] = ConsoleCloseHandler;
                    m_nativeHandlerLookup["console_action"] = ConsoleActionHandler;
                }

                return m_nativeHandlerLookup;
            }
        }

        void ConsoleOpenHandler(IDictionary<string, string> data)
        {
            if (onConsoleOpened != null)
            {
                onConsoleOpened();
            }
        }

        void ConsoleCloseHandler(IDictionary<string, string> data)
        {
            if (onConsoleClosed != null)
            {
                onConsoleClosed();
            }
        }

        void ConsoleActionHandler(IDictionary<string, string> data)
        {
            if (m_registry == null)
            {
                Debug.LogError("Can't run action: make sure plugin is properly initialized");
                return;
            }

            string actionIdStr;
            if (!data.TryGetValue("id", out actionIdStr))
            {
                Debug.LogError("Can't run action: data is not properly formatted");
                return;
            }

            int actionId;
            if (!int.TryParse(actionIdStr, out actionId))
            {
                Debug.LogError("Can't run action: invalid ID " + actionIdStr);
                return;
            }

            var action = m_registry.FindAction(actionId);
            if (action == null)
            {
                Debug.LogError("Can't run action: ID not found " + actionIdStr);
                return;
            }

            try
            {
                action.Execute();
            }
            catch (Exception e)
            {
                Debug.LogError("Can't run action: " + e.Message);
            }
        }

        #endregion

        #endif // LUNAR_CONSOLE_ENABLED

        #region Public API

        /// <summary>
        /// Shows Lunar console on top of everything. Does nothing if platform is not supported or if plugin is not initizlied.
        /// </summary>
        public static void Show()
        {
#if LUNAR_CONSOLE_PLATFORM_SUPPORTED
        #if LUNAR_CONSOLE_ENABLED
            if (s_instance != null)
            {
                s_instance.ShowConsole();
            }
            else
            {
                Debug.LogError("Can't show " + Constants.PluginName + ": instance is not initialized. Make sure you've installed it correctly");
            }
        #else
            Debug.LogWarning("Can't show " + Constants.PluginName + ": plugin is disabled");
        #endif
#else
            Debug.LogWarning("Can't show " + Constants.PluginName + ": current platform is not supported");
#endif
        }

        /// <summary>
        /// Hides Lunar console. Does nothing if platform is not supported or if plugin is not initizlied.
        /// </summary>
        public static void Hide()
        {
#if LUNAR_CONSOLE_PLATFORM_SUPPORTED
        #if LUNAR_CONSOLE_ENABLED
            if (s_instance != null)
            {
                s_instance.HideConsole();
            }
            else
            {
                Debug.LogError("Can't hide " + Constants.PluginName + ": instance is not initialized. Make sure you've installed it correctly");
            }
        #else
            Debug.LogWarning("Can't hide " + Constants.PluginName + ": plugin is disabled");
        #endif
#else
            Debug.LogWarning("Can't hide " + Constants.PluginName + ": current platform is not supported");
#endif
        }

        /// <summary>
        /// Clears Lunar console. Does nothing if platform is not supported or if plugin is not initizlied.
        /// </summary>
        public static void Clear()
        {
#if LUNAR_CONSOLE_PLATFORM_SUPPORTED
        #if LUNAR_CONSOLE_ENABLED
            if (s_instance != null)
            {
                s_instance.ClearConsole();
            }
            else
            {
                Debug.LogError("Can't clear " + Constants.PluginName + ": instance is not initialized. Make sure you've installed it correctly");
            }
        #else
            Debug.LogWarning("Can't clear " + Constants.PluginName + ": plugin is disabled");
        #endif
#else
            Debug.LogWarning("Can't clear " + Constants.PluginName + ": current platform is not supported");
#endif
        }

        public static void RegisterAction(string name, Action action)
        {
#if LUNAR_CONSOLE_PLATFORM_SUPPORTED
        #if LUNAR_CONSOLE_ENABLED
            if (s_instance != null)
            {
                s_instance.RegisterConsoleAction(name, action);
            }
            else
            {
                Debug.LogError("Can't register action: instance is not initialized. Make sure you've installed it correctly");
            }
        #else
            Debug.LogWarning("Can't register action: plugin is disabled");
        #endif
#else
            Debug.LogWarning("Can't register action: current platform is not supported");
#endif // LUNAR_CONSOLE_PLATFORM_SUPPORTED
        }

        public static void UnregisterAction(Action action)
        {
#if LUNAR_CONSOLE_PLATFORM_SUPPORTED
        #if LUNAR_CONSOLE_ENABLED
            if (s_instance != null)
            {
                s_instance.UnregisterConsoleAction(action);
            }
            else
            {
                Debug.LogError("Can't unregister action: instance is not initialized. Make sure you've installed it correctly");
            }
        #else
            Debug.LogWarning("Can't unregister action: plugin is disabled");
        #endif
#else
            Debug.LogWarning("Can't unregister action: current platform is not supported");
#endif // LUNAR_CONSOLE_PLATFORM_SUPPORTED
        }

        public static void UnregisterAction(string name)
        {
#if LUNAR_CONSOLE_PLATFORM_SUPPORTED
        #if LUNAR_CONSOLE_ENABLED
            if (s_instance != null)
            {
                s_instance.UnregisterConsoleAction(name);
            }
            else
            {
                Debug.LogError("Can't unregister action: instance is not initialized. Make sure you've installed it correctly");
            }
        #else
            Debug.LogWarning("Can't unregister action: plugin is disabled");
        #endif
#else
            Debug.LogWarning("Can't unregister action: current platform is not supported");
#endif // LUNAR_CONSOLE_PLATFORM_SUPPORTED
        }

        public static void UnregisterAllActions(object target)
        {
#if LUNAR_CONSOLE_PLATFORM_SUPPORTED
        #if LUNAR_CONSOLE_ENABLED
            if (s_instance != null)
            {
                s_instance.UnregisterAllConsoleActions(target);
            }
            else
            {
                Debug.LogError("Can't unregister actions: instance is not initialized. Make sure you've installed it correctly");
            }
        #else
            Debug.LogWarning("Can't unregister actions: plugin is disabled");
        #endif
#else
            Debug.LogWarning("Can't unregister actions: current platform is not supported");
#endif // LUNAR_CONSOLE_PLATFORM_SUPPORTED
        }

        /// <summary>
        /// Console opened callback
        /// </summary>
        public static Action onConsoleOpened { get; set; }

        /// <summary>
        /// Console closed callback
        /// </summary>
        public static Action onConsoleClosed { get; set; }

        #if LUNAR_CONSOLE_ENABLED

        void ShowConsole()
        {
            if (m_platform != null)
            {
                m_platform.ShowConsole();
            }
        }

        void HideConsole()
        {
            if (m_platform != null)
            {
                m_platform.HideConsole();
            }
        }

        void ClearConsole()
        {
            if (m_platform != null)
            {
                m_platform.ClearConsole();
            }
        }

        void RegisterConsoleAction(string name, Action actionDelegate)
        {
            if (m_registry != null)
            {
                m_registry.RegisterAction(name, actionDelegate);
            }
        }

        void UnregisterConsoleAction(Action actionDelegate)
        {
            if (m_registry != null)
            {
                m_registry.Unregister(actionDelegate);
            }
        }

        void UnregisterConsoleAction(string name)
        {
            if (m_registry != null)
            {
                m_registry.Unregister(name);
            }
        }

        void UnregisterAllConsoleActions(object target)
        {
            if (m_registry != null)
            {
                m_registry.UnregisterAll(target);
            }
        }

        #endif // LUNAR_CONSOLE_ENABLED

        #endregion
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

    #if LUNAR_CONSOLE_ENABLED

    struct MessageInfo
    {
        public readonly string message;
        public readonly string stackTrace;
        public readonly LogType type;

        public MessageInfo(string message, string stackTrace, LogType type)
        {
            this.message = message;
            this.stackTrace = stackTrace;
            this.type = type;
        }
    }

    #endif // LUNAR_CONSOLE_ENABLED
}
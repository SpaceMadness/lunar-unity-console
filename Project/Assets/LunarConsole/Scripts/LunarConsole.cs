//
//  LunarConsole.cs
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2017 Alex Lementuev, SpaceMadness.
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
#define LUNAR_CONSOLE_FULL

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
using System.Text;

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

        void OnDestroy()
        {
            DestroyPlatform();
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
                        registry.registryDelegate = m_platform;

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

                        // resolve variables
                        CVarResolver.ResolveVariables();

                        return true;
                    }
                }
            }
            catch (Exception e)
            {
                Log.e(e, "Can't init platform");
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

        void DestroyPlatform()
        {
            if (registry.registryDelegate == m_platform)
            {
                registry.registryDelegate = null;
            }
            s_instance = null;
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
            private readonly object m_logLock = new object();

            private readonly jvalue[] m_args0 = new jvalue[0];
            private readonly jvalue[] m_args1 = new jvalue[1];
            private readonly jvalue[] m_args2 = new jvalue[2];
            private readonly jvalue[] m_args3 = new jvalue[3];
            private readonly jvalue[] m_args5 = new jvalue[5];

            private static readonly string kPluginClassName = "spacemadness.com.lunarconsole.console.ConsolePlugin";

            private readonly AndroidJavaClass m_pluginClass;

            private readonly IntPtr m_pluginClassRaw;
            private readonly IntPtr m_methodLogMessage;
            private readonly IntPtr m_methodShowConsole;
            private readonly IntPtr m_methodHideConsole;
            private readonly IntPtr m_methodClearConsole;
            private readonly IntPtr m_methodRegisterAction;
            private readonly IntPtr m_methodUnregisterAction;
            private readonly IntPtr m_methodRegisterVariable;
            private readonly IntPtr m_methodUpdateVariable;

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
                m_pluginClass = new AndroidJavaClass(kPluginClassName);
                m_pluginClassRaw = m_pluginClass.GetRawClass();

                IntPtr methodInit = GetStaticMethod(m_pluginClassRaw, "init", "(Ljava.lang.String;Ljava.lang.String;Ljava.lang.String;IILjava.lang.String;)V");
                var methodInitParams = new jvalue[] {
                    jval(targetName),
                    jval(methodName),
                    jval(version),
                    jval(capacity),
                    jval(trim),
                    jval(gesture)
                };
                CallStaticVoidMethod(methodInit, methodInitParams);

                AndroidJNI.DeleteLocalRef(methodInitParams[0].l);
                AndroidJNI.DeleteLocalRef(methodInitParams[1].l);
                AndroidJNI.DeleteLocalRef(methodInitParams[2].l);
                AndroidJNI.DeleteLocalRef(methodInitParams[5].l);

                m_methodLogMessage = GetStaticMethod(m_pluginClassRaw, "logMessage", "(Ljava.lang.String;Ljava.lang.String;I)V");
                m_methodShowConsole = GetStaticMethod(m_pluginClassRaw, "show", "()V");
                m_methodHideConsole = GetStaticMethod(m_pluginClassRaw, "hide", "()V");
                m_methodClearConsole = GetStaticMethod(m_pluginClassRaw, "clear", "()V");
                m_methodRegisterAction = GetStaticMethod(m_pluginClassRaw, "registerAction", "(ILjava.lang.String;)V");
                m_methodUnregisterAction = GetStaticMethod(m_pluginClassRaw, "unregisterAction", "(I)V");
                m_methodRegisterVariable = GetStaticMethod(m_pluginClassRaw, "registerVariable", "(ILjava.lang.String;Ljava.lang.String;Ljava.lang.String;Ljava.lang.String;)V");
                m_methodUpdateVariable = GetStaticMethod(m_pluginClassRaw, "updateVariable", "(ILjava.lang.String;)V");
            }

            ~PlatformAndroid()
            {
                m_pluginClass.Dispose();
            }

            #region IPlatform implementation
            
            public void OnLogMessageReceived(string message, string stackTrace, LogType type)
            {
                lock (m_logLock)
                {
                    m_args3[0] = jval(message);
                    m_args3[1] = jval(stackTrace);
                    m_args3[2] = jval((int)type);

                    CallStaticVoidMethod(m_methodLogMessage, m_args3);

                    AndroidJNI.DeleteLocalRef(m_args3[0].l);
                    AndroidJNI.DeleteLocalRef(m_args3[1].l);
                }
            }

            public bool ShowConsole()
            {
                try
                {
                    CallStaticVoidMethod(m_methodShowConsole, m_args0);
                    return true;
                }
                catch (Exception e)
                {
                    Debug.LogError("Exception while calling 'LunarConsole.ShowConsole': " + e.Message);
                    return false;
                }
            }

            public bool HideConsole()
            {
                try
                {
                    CallStaticVoidMethod(m_methodHideConsole, m_args0);
                    return true;
                }
                catch (Exception e)
                {
                    Debug.LogError("Exception while calling 'LunarConsole.HideConsole': " + e.Message);
                    return false;
                }
            }

            public void ClearConsole()
            {
                try
                {
                    CallStaticVoidMethod(m_methodClearConsole, m_args0);
                }
                catch (Exception e)
                {
                    Debug.LogError("Exception while calling 'LunarConsole.ClearConsole': " + e.Message);
                }
            }

            public void OnActionRegistered(CRegistry registry, CAction action)
            {
                try
                {
                    m_args2[0] = jval(action.Id);
                    m_args2[1] = jval(action.Name);
                    CallStaticVoidMethod(m_methodRegisterAction, m_args2);
                    AndroidJNI.DeleteLocalRef(m_args2[1].l);
                }
                catch (Exception e)
                {
                    Debug.LogError("Exception while calling 'LunarConsole.OnActionRegistered': " + e.Message);
                }
            }

            public void OnActionUnregistered(CRegistry registry, CAction action)
            {
                try
                {
                    m_args1[0] = jval(action.Id);
                    CallStaticVoidMethod(m_methodUnregisterAction, m_args1);
                }
                catch (Exception e)
                {
                    Debug.LogError("Exception while calling 'LunarConsole.OnActionUnregistered': " + e.Message);
                }
            }

            public void OnVariableRegistered(CRegistry registry, CVar cvar)
            {
                try
                {
                    m_args5[0] = jval(cvar.Id);
                    m_args5[1] = jval(cvar.Name);
                    m_args5[2] = jval(cvar.Type.ToString());
                    m_args5[3] = jval(cvar.Value);
                    m_args5[4] = jval(cvar.DefaultValue);
                    CallStaticVoidMethod(m_methodRegisterVariable, m_args5);
                    AndroidJNI.DeleteLocalRef(m_args5[1].l);
                    AndroidJNI.DeleteLocalRef(m_args5[2].l);
                    AndroidJNI.DeleteLocalRef(m_args5[3].l);
                    AndroidJNI.DeleteLocalRef(m_args5[4].l);
                }
                catch (Exception e)
                {
                    Debug.LogError("Exception while calling 'LunarConsole.OnVariableRegistered': " + e.Message);
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
                AndroidJNI.CallStaticVoidMethod(m_pluginClassRaw, method, args);
            }

            private bool CallStaticBoolMethod(IntPtr method, jvalue[] args)
            {
                return AndroidJNI.CallStaticBooleanMethod(m_pluginClassRaw, method, args);
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
                Log.w("Can't handle native callback: 'name' is undefined");
                return;
            }

            LunarConsoleNativeMessageHandler handler;
            if (!nativeHandlerLookup.TryGetValue(name, out handler))
            {
                Log.w("Can't handle native callback: handler not found '" + name + "'");
                return;
            }

            try
            {
                handler(data);
            }
            catch (Exception e)
            {
                Log.e(e, "Exception while handling native callback '{0}'", name);
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
                    m_nativeHandlerLookup["console_variable_set"] = ConsoleVariableSetHandler;
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
            string actionIdStr;
            if (!data.TryGetValue("id", out actionIdStr))
            {
                Log.w("Can't run action: data is not properly formatted");
                return;
            }

            int actionId;
            if (!int.TryParse(actionIdStr, out actionId))
            {
                Log.w("Can't run action: invalid ID " + actionIdStr);
                return;
            }

            var action = registry.FindAction(actionId);
            if (action == null)
            {
                Log.w("Can't run action: ID not found " + actionIdStr);
                return;
            }

            try
            {
                action.Execute();
            }
            catch (Exception e)
            {
                Log.e(e, "Can't run action {0}", action.Name);
            }
        }

        void ConsoleVariableSetHandler(IDictionary<string, string> data)
        {
            string variableIdStr;
            if (!data.TryGetValue("id", out variableIdStr))
            {
                Log.w("Can't set variable: missing 'id' property");
                return;
            }

            string value;
            if (!data.TryGetValue("value", out value))
            {
                Log.w("Can't set variable: missing 'value' property");
                return;
            }

            int variableId;
            if (!int.TryParse(variableIdStr, out variableId))
            {
                Log.w("Can't set variable: invalid ID " + variableIdStr);
                return;
            }

            var variable = registry.FindVariable(variableId);
            if (variable == null)
            {
                Log.w("Can't set variable: ID not found " + variableIdStr);
                return;
            }

            try
            {
                switch(variable.Type)
                {
                    case CVarType.Boolean:
                    {
                        int intValue;
                        if (int.TryParse(value, out intValue) && (intValue == 0 || intValue == 1))
                        {
                            variable.BoolValue = intValue == 1;
                        }
                        else
                        {
                            Log.e("Invalid boolean value: '{0}'", value);
                        }
                        break;
                    }
                    case CVarType.Integer:
                    {
                        int intValue;
                        if (int.TryParse(value, out intValue))
                        {
                            variable.IntValue = intValue;
                        }
                        else
                        {
                            Log.e("Invalid integer value: '{0}'", value);
                        }
                        break;
                    }
                    case CVarType.Float:
                    {
                        float floatValue;
                        if (float.TryParse(value, out floatValue))
                        {
                            variable.FloatValue = floatValue;
                        }
                        else
                        {
                            Log.e("Invalid float value: '{0}'", value);
                        }
                        break;
                    }
                    case CVarType.String:
                    {
                        variable.Value = value;
                        break;
                    }
                    default:
                    {
                        Log.e("Unexpected variable type: {0}", variable.Type);
                        break;
                    }
                }
            }
            catch (Exception e)
            {
                Log.e(e, "Exception while trying to set variable '{0}'", variable.Name);
            }
        }

        #endregion

        private CRegistry registry
        {
            get { return CRegistry.instance; }
        }

        #endif // LUNAR_CONSOLE_ENABLED

        #region Public API

        /// <summary>
        /// Shows Lunar console on top of everything. Does nothing if current platform is not supported or if the plugin is not initizlied.
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
                Log.w("Can't show console: instance is not initialized. Make sure you've installed it correctly");
            }
            #else
            Log.w("Can't show console: plugin is disabled");
            #endif
            #else
            Log.w("Can't show console: current platform is not supported");
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
                Log.w("Can't hide console: instance is not initialized. Make sure you've installed it correctly");
            }
            #else
            Log.w("Can't hide console: plugin is disabled");
            #endif
            #else
            Log.w("Can't hide console: current platform is not supported");
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
                Log.w("Can't clear console: instance is not initialized. Make sure you've installed it correctly");
            }
            #else
            Log.w("Can't clear console: plugin is disabled");
            #endif
            #else
            Log.w("Can't clear console: current platform is not supported");
            #endif
        }

        #if LUNAR_CONSOLE_FULL
        /// <summary>
        /// Registers action delegate
        /// </summary>
        /// <param name="name">Display name</param>
        /// <param name="action">Callback delegate</param>
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
                Log.w("Can't register action: instance is not initialized. Make sure you've installed it correctly");
            }
            #else
            Log.w("Can't register action: plugin is disabled");
            #endif
            #endif
        }
        #endif // LUNAR_CONSOLE_FULL

        #if LUNAR_CONSOLE_FULL
        /// <summary>
        /// Unregister action by delegate
        /// </summary>
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
                Log.w("Can't unregister action: instance is not initialized. Make sure you've installed it correctly");
            }
            #else
            Log.w("Can't unregister action: plugin is disabled");
            #endif
            #endif
        }
        #endif // LUNAR_CONSOLE_FULL

        #if LUNAR_CONSOLE_FULL
        /// <summary>
        /// Unregister action by name
        /// </summary>
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
                Log.w("Can't unregister action: instance is not initialized. Make sure you've installed it correctly");
            }
            #else
            Log.w("Can't unregister action: plugin is disabled");
            #endif
            #endif
        }
        #endif // LUNAR_CONSOLE_FULL

        #if LUNAR_CONSOLE_FULL
        /// <summary>
        /// Unregister all actions from specific target
        /// </summary>
        public static void UnregisterAllActions(object target)
        {
            #if LUNAR_CONSOLE_PLATFORM_SUPPORTED
            #if LUNAR_CONSOLE_ENABLED
            if (LunarConsoleSettings.actionsEnabled)
            {
                if (s_instance != null)
                {
                    s_instance.UnregisterAllConsoleActions(target);
                }
                else
                {
                    Log.w("Can't unregister actions: instance is not initialized. Make sure you've installed it correctly");
                }
            }
            #endif // LUNAR_CONSOLE_ENABLED
            #endif // LUNAR_CONSOLE_PLATFORM_SUPPORTED
        }
        #endif // LUNAR_CONSOLE_FULL

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
            registry.RegisterAction(name, actionDelegate);
        }

        void UnregisterConsoleAction(Action actionDelegate)
        {
            registry.Unregister(actionDelegate);
        }

        void UnregisterConsoleAction(string name)
        {
            registry.Unregister(name);
        }

        void UnregisterAllConsoleActions(object target)
        {
            registry.UnregisterAll(target);
        }

        #endif // LUNAR_CONSOLE_ENABLED

        #endregion
    }

    public static class LunarConsoleSettings
    {
        public static readonly bool consoleEnabled;
        public static readonly bool consoleSupported;

        static LunarConsoleSettings()
        {
            #if LUNAR_CONSOLE_ENABLED
            consoleEnabled = true;
            #else
            consoleEnabled = false;
            #endif

            #if LUNAR_CONSOLE_PLATFORM_SUPPORTED
            consoleSupported = true;
            #else
            consoleSupported = false;
            #endif
        }

        public static bool actionsEnabled
        {
            get
            {
                if (consoleSupported && consoleEnabled)
                {
                    #if UNITY_IOS || UNITY_IPHONE
                    return Application.platform == RuntimePlatform.IPhonePlayer;
                    #elif UNITY_ANDROID
                    return Application.platform == RuntimePlatform.Android;
                    #endif
                }

                return false;
            }
        }
    }

    #if UNITY_EDITOR

    public static class LunarConsolePluginEditorHelper
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

#if LUNAR_CONSOLE_ENABLED

namespace LunarConsolePluginInternal
{
    /// <summary>
    /// Class for collecting anonymous usage statistics
    /// </summary>
    static class LunarConsoleAnalytics
    {
        private static readonly string serverURL = "http://www.google-analytics.com";
        private static readonly string defaultPayload;

        static LunarConsoleAnalytics()
        {
            // tracking id
            #if LUNAR_CONSOLE_FULL
            var trackingId = "UA-91768505-1";
            #else
            var trackingId = "UA-91747018-1";
            #endif

            defaultPayload = String.Format("v=1&tid={0}&cid={1}&dos={2}", 
                trackingId, 
                WWW.EscapeURL(SystemInfo.deviceUniqueIdentifier), 
                WWW.EscapeURL(SystemInfo.operatingSystem)
            );
        }

        public static IEnumerator trackEvent(String name, IDictionary<string, object> payload = null)
        {
            var payloadStr = createPayloadStr(name, payload);
            var request = new WWW(serverURL, System.Text.Encoding.UTF8.GetBytes(payloadStr));
            Log.dev("Event track payload: " + payloadStr);

            yield return null;

            Log.dev("Event track result: " + request.text);
        }

        static string createPayloadStr(string name, IDictionary<string, object> payload)
        {
            var buffer = new StringBuilder();
            buffer.AppendFormat("{0}&t={1}", defaultPayload, WWW.EscapeURL(name));
            if (payload != null && payload.Count > 0)
            {
                foreach (var entry in payload)
                {
                    if (entry.Value == null)
                    {
                        continue;
                    }

                    buffer.AppendFormat("&{0}={1}", WWW.EscapeURL(entry.Key), WWW.EscapeURL(entry.Value.ToString()));
                }
            }
            return buffer.ToString();
        }
    }
}

#endif // LUNAR_CONSOLE_ENABLED
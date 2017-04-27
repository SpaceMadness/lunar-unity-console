using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEditor;

using LunarConsolePlugin;
using LunarConsolePluginInternal;

namespace LunarConsoleEditorInternal
{
    class ActionsAndVariablesWindow : EditorWindow
    {
        static readonly string PrefsKeyFilterText = "com.spacemadness.LunarMobileConsole.Editor.FilterText";

        string m_filterText;
        Vector2 m_scrollPosition;

        GUIStyle m_filterTextStyle;
        GUIStyle m_filterButtonStyle;
        GUIStyle m_headerLabelStyle;

        public ActionsAndVariablesWindow()
        {
            this.titleContent = new GUIContent("Actions & Vars");
        }

        void OnEnable()
        {
            m_filterText = EditorPrefs.GetString(PrefsKeyFilterText, "");
        }
        
        void OnGUI()
        {
            if (Application.isPlaying)
            {
                var instance = LunarConsole.instance;
                if (instance != null && instance.registry != null)
                {
                    OnRegistryGUI(instance.registry);
                }
                else
                {
                    GUILayout.Label("Instance is not initialized");
                }
            }
            else
            {
                GUILayout.Label("Only available in Play Mode");
            }
        }

        void OnRegistryGUI(CRegistry registry)
        {
            GUILayout.BeginVertical();
            {
                GUILayout.BeginHorizontal();
                {
                    m_filterText = GUILayout.TextField(m_filterText, filterTextStyle);
                    if (GUILayout.Button(GUIContent.none, filterButtonStyle))
                    {
                        m_filterText = "";
                    }
                }
                GUILayout.EndHorizontal();

                m_scrollPosition = GUILayout.BeginScrollView(m_scrollPosition);
                {
                    var filterText = m_filterText.ToLower();

                    GUILayout.Label("Actions", headerLabelStyle);
                    foreach (var action in registry.actions)
                    {
                        if (m_filterText.Length == 0 || action.Name.ToLower().Contains(filterText))
                        {
                            OnActionGUI(action);
                        }
                    }

                    GUILayout.Label("Variables", headerLabelStyle);
                    foreach (var cvar in registry.cvars)
                    {
                        if (m_filterText.Length == 0 || cvar.Name.ToLower().Contains(filterText))
                        {
                            OnVariableGUI(cvar);
                        }
                    }
                }
                GUILayout.EndScrollView();
            }
            GUILayout.EndVertical();
        }

        void OnActionGUI(CAction action)
        {
            if (GUILayout.Button(action.Name))
            {
                action.Execute();
            }
        }

        void OnVariableGUI(CVar cvar)
        {
            switch (cvar.Type)
            {
                case CVarType.Boolean:
                    cvar.BoolValue = EditorGUILayout.Toggle(cvar.Name, cvar.BoolValue);
                    break;
                case CVarType.Float:
                    cvar.FloatValue = EditorGUILayout.FloatField(cvar.Name, cvar.FloatValue);
                    break;
                case CVarType.Integer:
                    cvar.IntValue = EditorGUILayout.IntField(cvar.Name, cvar.IntValue);
                    break;
                case CVarType.String:
                    cvar.Value = EditorGUILayout.TextField(cvar.Name, cvar.Value);
                    break;
                default:
                    EditorGUILayout.LabelField(cvar.Name, cvar.Value);
                    break;
            }
        }

        public static void ShowWindow()
        {
            EditorWindow.GetWindow<ActionsAndVariablesWindow>();
        }

        private GUIStyle filterTextStyle
        {
            get
            {
                if (m_filterTextStyle == null)
                {
                    m_filterTextStyle = new GUIStyle("SearchTextField");
                }
                return m_filterTextStyle;
            }
        }

        private GUIStyle filterButtonStyle
        {
            get
            {
                if (m_filterButtonStyle == null)
                {
                    m_filterButtonStyle = new GUIStyle("SearchCancelButton");
                }
                return m_filterButtonStyle;
            }
        }

        private GUIStyle headerLabelStyle
        {
            get
            {
                if (m_headerLabelStyle == null)
                {
                    m_headerLabelStyle = new GUIStyle("HeaderLabel");
                }
                return m_headerLabelStyle;
            }
        }
    }
}

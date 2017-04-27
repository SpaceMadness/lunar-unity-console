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
                m_filterText = GUILayout.TextField(m_filterText);
                m_scrollPosition = GUILayout.BeginScrollView(m_scrollPosition);
                {
                    GUILayout.Label("Actions");
                    foreach (var action in registry.actions)
                    {
                        OnActionGUI(action);
                    }

                    GUILayout.Label("Variables");
                    foreach (var cvar in registry.cvars)
                    {
                        OnVariableGUI(cvar);
                    }
                }
                GUILayout.EndScrollView();
            }
            GUILayout.EndVertical();
        }

        void OnActionGUI(CAction action)
        {
            GUILayout.Label(action.Name);
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
    }
}

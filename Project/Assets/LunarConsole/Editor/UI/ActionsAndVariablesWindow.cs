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
                foreach (var action in registry.actions)
                {
                    if (GUILayout.Button(action.Name))
                    {
                    }
                }
                GUILayout.EndScrollView();
            }
            GUILayout.EndVertical();
        }

        public static void ShowWindow()
        {
            EditorWindow.GetWindow<ActionsAndVariablesWindow>();
        }
    }
}

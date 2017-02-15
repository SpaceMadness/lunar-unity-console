using System.Collections;
using System.Collections.Generic;

using UnityEngine;
using UnityEditor;

using LunarConsolePlugin;
using LunarConsolePluginInternal;

namespace LunarConsoleEditorInternal
{
    [CustomEditor(typeof(LunarConsole))]
    class LunarConsoleEditor : Editor
    {
        private GUIStyle m_buttonStyle;

        public override void OnInspectorGUI()
        {
            base.OnInspectorGUI();

            if (LunarConsoleConfig.freeVersion)
            {
                if (m_buttonStyle == null)
                {
                    m_buttonStyle = new GUIStyle("LargeButton");
                }

                if (GUILayout.Button("Get PRO version", m_buttonStyle))
                {
                    Application.OpenURL("https://goo.gl/jvJzr7");
                }
            }
        }
    }
}
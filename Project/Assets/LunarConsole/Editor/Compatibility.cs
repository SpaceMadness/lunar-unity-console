using System;
using System.Collections;
using System.Collections.Generic;
using UnityEditor;
using UnityEngine;

namespace LunarConsoleEditorInternal
{
#if UNITY_5_4_OR_NEWER
    class DisabledScopeCompat : IDisposable
    {
        private readonly EditorGUI.DisabledScope m_target;

        public DisabledScopeCompat(bool disabled)
        {
            m_target = new EditorGUI.DisabledScope(disabled);
        }

        public void Dispose()
        {
            m_target.Dispose();
        }
    }
#else
    class DisabledScopeCompat : IDisposable
    {
        public DisabledScopeCompat(bool disabled)
        {
            EditorGUI.BeginDisabledGroup(disabled);
        }

        public void Dispose()
        {
            EditorGUI.EndDisabledGroup();
        }
    }
#endif
}

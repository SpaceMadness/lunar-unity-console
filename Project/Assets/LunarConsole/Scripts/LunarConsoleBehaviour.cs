using System;
using LunarConsolePluginInternal;
using UnityEngine;

namespace LunarConsolePlugin
{
    public abstract class LunarConsoleBehaviour : MonoBehaviour
    {
        [SerializeField]
        private TargetPlatform m_platform = TargetPlatform.All;
        
        private IDisposable m_disposable;

        protected virtual void Awake()
        {
            if (!LunarConsole.isConsoleEnabled || !m_platform.IsSupported())
            {
                Destroy(this);
            }
        }

        protected virtual void OnEnable()
        {
            m_disposable = LunarConsole.Register(this, false);
        }

        private void OnDisable()
        {
            if (m_disposable != null)
            {
                m_disposable.Dispose();
                m_disposable = null;
            }
        }
    }
}
using System;
using System.Collections.Generic;
using LunarConsolePlugin;
using UnityEngine;

namespace LunarConsolePluginInternal
{
    public class LunarRegistryComponent : MonoBehaviour
    {
        private readonly List<IDisposable> m_disposables = new List<IDisposable>();

        private void Awake()
        {
            if (!LunarConsole.isConsoleEnabled)
            {
                Destroy(this);
            }
        }

        private void OnEnable()
        {
            m_disposables.Clear();

            var components = ListComponents();
            for (var index = 0; index < components.Length; index++)
            {
                var disposable = LunarConsole.Register(components[index]);
                if (disposable is NullDisposable)
                {
                    continue;
                }

                m_disposables.Add(disposable);
            }
        }

        private void OnDisable()
        {
            foreach (IDisposable disposable in m_disposables)
            {
                disposable.Dispose();
            }

            m_disposables.Clear();
        }

        private MonoBehaviour[] ListComponents()
        {
            return gameObject.GetComponents<MonoBehaviour>();
        }
    }
}
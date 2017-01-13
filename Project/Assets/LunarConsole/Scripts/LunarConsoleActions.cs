using System;
using System.Collections;
using System.Collections.Generic;
using System.Reflection;

using UnityEngine;
using UnityEngine.Events;

using Object = UnityEngine.Object;

namespace LunarConsolePlugin
{
    [Serializable]
    public class LunarConsoleAction
    {
        static readonly object[] kEmptyArgs = {};

        [SerializeField]
        string m_name;

        [SerializeField]
        GameObject m_target;

        [SerializeField]
        string m_componentTypeName;

        [SerializeField]
        string m_componentMethodName;

        Type m_componentType;
        MethodInfo m_componentMethod;

        public void Register()
        {
            if (m_name != null && m_componentMethodName != null)
            {
                LunarConsole.RegisterAction(m_name, Invoke);
            }
        }

        public void Unregister()
        {
            LunarConsole.UnregisterAction(Invoke);
        }

        void Invoke()
        {
            if (m_target == null)
            {
                Debug.LogErrorFormat("Can't invoke action '{0}': target is not set", m_name);
                return;
            }

            if (m_componentTypeName == null)
            {
                Debug.LogErrorFormat("Can't invoke action '{0}': method is not set", m_name);
                return;
            }

            if (m_componentMethodName == null)
            {
                Debug.LogErrorFormat("Can't invoke action '{0}': method is not set", m_name);
                return;
            }

            if (m_componentMethod == null || m_componentType == null)
            {
                ResolveInvocation();
            }

            var component = m_target.GetComponent(m_componentType);
            m_componentMethod.Invoke(component, kEmptyArgs);
        }

        void ResolveInvocation()
        {
            try
            {
                m_componentType = Type.GetType(m_componentTypeName);
                m_componentMethod = m_componentType.GetMethod(m_componentMethodName, BindingFlags.Instance|BindingFlags.Public|BindingFlags.NonPublic);
            }
            catch (Exception e)
            {
                Debug.LogErrorFormat(e.Message); // TODO: better error log
            }
        }
    }

    public class LunarConsoleActions : MonoBehaviour
    {
        [SerializeField]
        [HideInInspector]
        List<LunarConsoleAction> m_actions;

        void Start()
        {
            if (LunarConsoleSettings.consoleEnabled)
            {
                foreach (var action in m_actions)
                {
                    action.Register();
                }
            }
            else
            {
                Destroy(this);
            }
        }

        void OnDestroy()
        {
            if (LunarConsoleSettings.consoleEnabled)
            {
                foreach (var action in m_actions)
                {
                    action.Unregister();
                }
            }
        }

        public void AddAction(LunarConsoleAction action)
        {
            m_actions.Add(action);
        }

        public List<LunarConsoleAction> actions
        {
            get { return m_actions; }
        }
    }
}
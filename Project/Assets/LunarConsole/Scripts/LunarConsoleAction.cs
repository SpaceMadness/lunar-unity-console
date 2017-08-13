//
//  LunarConsoleActions.cs
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

﻿using System;
using System.Collections;
using System.Collections.Generic;
using System.Reflection;

using UnityEngine;
using UnityEngine.Events;

using Object = UnityEngine.Object;

using LunarConsolePlugin;

namespace LunarConsolePluginInternal
{
    public enum PersistentListenerMode
    {
        Void,
        Bool,
        Int,
        Float,
        String,
        Object
    }

    [Serializable]
    public class LunarConsoleActionCall
    {
        [SerializeField]
        Object m_target;

        [SerializeField]
        string m_methodName;

        [SerializeField]
        PersistentListenerMode m_mode;

        [SerializeField]
        Object m_objectArgument;

        [SerializeField]
        string m_objectArgumentAssemblyTypeName;

        [SerializeField]
        int m_intArgument;

        [SerializeField]
        float m_floatArgument;

        [SerializeField]
        string m_stringArgument;

        [SerializeField]
        bool m_boolArgument;

        public void Invoke()
        {
        }
    }

    public class LunarConsoleAction : MonoBehaviour
    {
        [SerializeField]
        string m_title;

        [SerializeField]
        [HideInInspector]
        List<LunarConsoleActionCall> m_calls;

        void Awake()
        {
            if (!actionsEnabled)
            {
                Destroy(this);
            }
        }

        void Start()
        {
            if (actionsEnabled)
            {
                RegisterAction();
            }
            else
            {
                Destroy(this);
            }
        }
        
        void OnDestroy()
        {
            if (actionsEnabled)
            {
                UnregisterAction();
            }
        }
        
        public List<LunarConsoleActionCall> calls
        {
            get { return m_calls; }
        }

        private void RegisterAction()
        {
            LunarConsole.RegisterAction(m_title, InvokeAction);
        }

        private void UnregisterAction()
        {
            LunarConsole.UnregisterAction(InvokeAction);
        }

        private void InvokeAction()
        {
            if (m_calls != null && m_calls.Count > 0)
            {
                foreach (LunarConsoleActionCall call in m_calls)
                {
                    call.Invoke();
                }
            }
            else
            {
                Debug.LogWarningFormat("Action '{0}' has 0 calls", m_title);
            }
        }

        bool actionsEnabled
        {
            get { return LunarConsoleConfig.actionsEnabled; }
        }
    }
}
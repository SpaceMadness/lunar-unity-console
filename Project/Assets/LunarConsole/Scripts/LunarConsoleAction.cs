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
    public enum LunarPersistentListenerMode
    {
        Void,
        Bool,
        Int,
        Float,
        String,
        Object
    }

    [Serializable]
    internal class LunarArgumentCache : ISerializationCallbackReceiver
    {
        [SerializeField]
        private UnityEngine.Object m_objectArgument;

        [SerializeField]
        private string m_objectArgumentAssemblyTypeName;

        [SerializeField]
        private int m_intArgument;

        [SerializeField]
        private float m_floatArgument;

        [SerializeField]
        private string m_stringArgument;

        [SerializeField]
        private bool m_boolArgument;

        public UnityEngine.Object unityObjectArgument
        {
            get
            {
                return this.m_objectArgument;
            }
            set
            {
                this.m_objectArgument = value;
                this.m_objectArgumentAssemblyTypeName = ((!(value != null)) ? string.Empty : value.GetType().AssemblyQualifiedName);
            }
        }

        public string unityObjectArgumentAssemblyTypeName
        {
            get
            {
                return this.m_objectArgumentAssemblyTypeName;
            }
        }

        public int intArgument
        {
            get
            {
                return this.m_intArgument;
            }
            set
            {
                this.m_intArgument = value;
            }
        }

        public float floatArgument
        {
            get
            {
                return this.m_floatArgument;
            }
            set
            {
                this.m_floatArgument = value;
            }
        }

        public string stringArgument
        {
            get
            {
                return this.m_stringArgument;
            }
            set
            {
                this.m_stringArgument = value;
            }
        }

        public bool boolArgument
        {
            get
            {
                return this.m_boolArgument;
            }
            set
            {
                this.m_boolArgument = value;
            }
        }

        private void TidyAssemblyTypeName()
        {
            if (!string.IsNullOrEmpty(this.m_objectArgumentAssemblyTypeName))
            {
                int num = 2147483647;
                int num2 = this.m_objectArgumentAssemblyTypeName.IndexOf(", Version=");
                if (num2 != -1)
                {
                    num = Math.Min(num2, num);
                }
                num2 = this.m_objectArgumentAssemblyTypeName.IndexOf(", Culture=");
                if (num2 != -1)
                {
                    num = Math.Min(num2, num);
                }
                num2 = this.m_objectArgumentAssemblyTypeName.IndexOf(", PublicKeyToken=");
                if (num2 != -1)
                {
                    num = Math.Min(num2, num);
                }
                if (num != 2147483647)
                {
                    this.m_objectArgumentAssemblyTypeName = this.m_objectArgumentAssemblyTypeName.Substring(0, num);
                }
            }
        }

        public void OnBeforeSerialize()
        {
            this.TidyAssemblyTypeName();
        }

        public void OnAfterDeserialize()
        {
            this.TidyAssemblyTypeName();
        }
    }

    [Serializable]
    public class LunarConsoleActionCall
    {
        [SerializeField]
        Object m_target;

        [SerializeField]
        string m_methodName;

        [SerializeField]
        LunarPersistentListenerMode m_mode;

        [SerializeField]
        LunarArgumentCache m_arguments;

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
//
//  CRegistry.cs
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2015-2020 Alex Lementuev, SpaceMadness.
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


using System;
using System.Collections.Generic;
using System.Reflection;
using LunarConsolePlugin;

namespace LunarConsolePluginInternal
{
    public interface ICRegistryDelegate
    {
        void OnActionRegistered(CRegistry registry, CAction action);
        void OnActionUnregistered(CRegistry registry, CAction action);
        void OnVariableRegistered(CRegistry registry, CVar cvar);
        void OnVariableUpdated(CRegistry registry, CVar cvar);
    }

    public class CRegistry
    {
        private readonly CActionList m_actions = new CActionList();
        private readonly CVarList m_vars = new CVarList();
        private ICRegistryDelegate m_delegate;
        private int m_nextEntryId;
        
        /// <summary>
        /// Registers all actions defined in the target class.
        /// <returns>Disposable object which will remove all registered items.</returns> 
        /// </summary>
        public IDisposable Register(object target)
        {
            List<ConsoleEntry> entries = new List<ConsoleEntry>();
            
            var type = target.GetType();
            var methods = ClassUtils.ListMethods(type, MethodFilter);
            for (var index = 0; index < methods.Count; index++)
            {
                var method = methods[index];
                var attribute = method.GetCustomAttribute<ConsoleActionAttribute>();
                if (attribute == null)
                {
                    continue;
                }

                var returnType = method.ReturnType;
                if (returnType != typeof(void))
                {
                    Log.w("Action method with non-void return types are not supported yet: {0}", method);
                    continue;
                }

                var parameterCount = method.GetParameters().Length;
                if (parameterCount > 0)
                {
                    Log.w("Action method parameters are not supported yet: {0}", method);
                    continue;
                }
                
                var actionName = string.IsNullOrWhiteSpace(attribute.Name)
                    ? StringUtils.ToDisplayName(method.Name)
                    : attribute.Name;

                var delegateType = typeof(Action);
                Delegate callback = method.IsStatic ? method.CreateDelegate(delegateType) : method.CreateDelegate(delegateType, target);
                var action = RegisterAction(actionName, callback);
                entries.Add(action);
            }
            
            return new EntriesDisposer(this, entries);
        }

        private void Unregister(ConsoleEntry entry)
        {
            if (entry is CAction)
            {
                var action = entry as CAction;
                UnregisterAction(action.Id);
            }
            else
            {
                throw new NotImplementedException("Can't unregister entry: " + entry);
            }
        }
        
        #region Action registry

        public CAction RegisterAction(string name, Delegate actionDelegate)
        {
            if (name == null)
            {
                throw new ArgumentNullException("name");
            }

            if (name.Length == 0)
            {
                throw new ArgumentException("Action's name is empty");
            }
            
            if (actionDelegate == null)
            {
                throw new ArgumentNullException("actionDelegate");
            }

            var existingAction = m_actions.Find(name);
            if (existingAction != null)
            {
                Log.w("Duplicate actions:\n{0}: {1}\n{2}: {3}", existingAction.Name, existingAction.ActionDelegate,
                    name, actionDelegate);
            }
            
            var action = new CAction(m_nextEntryId++, name, actionDelegate);
            m_actions.Add(action);

            if (m_delegate != null)
            {
                m_delegate.OnActionRegistered(this, action);
            }

            return action;
        }

        public bool UnregisterAction(string name)
        {
            return UnregisterAction(delegate(CAction action)
            {
                return action.Name == name;
            });
        }

        public bool UnregisterAction(int id)
        {
            return UnregisterAction(delegate(CAction action)
            {
                return action.Id == id;
            });
        }

        public bool UnregisterAction(Delegate del)
        {
            return UnregisterAction(delegate(CAction action)
            {
                return action.ActionDelegate == del;
            });
        }

        public bool UnregisterAll(object target)
        {
            return target != null && UnregisterAction(delegate(CAction action)
            {
                return action.ActionDelegate.Target == target;
            });
        }

        private bool UnregisterAction(Predicate<CAction> filter)
        {
            if (filter == null)
            {
                throw new ArgumentNullException("filter");
            }

            IList<CAction> actionsToRemove = new List<CAction>();
            foreach (var action in m_actions)
            {
                if (filter(action))
                {
                    actionsToRemove.Add(action);
                }
            }

            foreach (var action in actionsToRemove)
            {
                RemoveAction(action);
            }

            return actionsToRemove.Count > 0;
        }

        private bool RemoveAction(CAction action)
        {
            if (m_actions.Remove(action.Id))
            {
                if (m_delegate != null)
                {
                    m_delegate.OnActionUnregistered(this, action);
                }

                return true;
            }

            return false;
        }

        public CAction FindAction(int id)
        {
            return m_actions.Find(id);
        }

        #endregion

        #region Variables

        public void Register(CVar cvar)
        {
            m_vars.Add(cvar);

            if (m_delegate != null)
            {
                m_delegate.OnVariableRegistered(this, cvar);
            }
        }

        public CVar FindVariable(int variableId)
        {
            return m_vars.Find(variableId);
        }

        public CVar FindVariable(string variableName)
        {
            return m_vars.Find(variableName);
        }

        #endregion

        #region Helpers

        private static bool MethodFilter(MethodInfo method)
        {
            return true; // list all methods
        }

        #endregion

        #region Destroyable

        public void Destroy()
        {
            m_actions.Clear();
            m_vars.Clear();
            m_delegate = null;
        }

        #endregion

        #region Properties

        public ICRegistryDelegate registryDelegate
        {
            get { return m_delegate; }
            set { m_delegate = value; }
        }

        public CActionList actions
        {
            get { return m_actions; }
        }

        public CVarList cvars
        {
            get { return m_vars; }
        }

        #endregion

        private sealed class EntriesDisposer : IDisposable
        {
            private readonly CRegistry m_registry;
            private readonly List<ConsoleEntry> m_entries;

            public EntriesDisposer(CRegistry registry, List<ConsoleEntry> entries)
            {
                m_entries = entries;
                m_registry = registry;
            }

            public void Dispose()
            {
                foreach (ConsoleEntry entry in m_entries)
                {
                    m_registry.Unregister(entry);
                }
                m_entries.Clear();
            }
        }
    }
}

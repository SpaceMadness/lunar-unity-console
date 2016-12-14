using System;
using System.Collections.Generic;
using System.Reflection;

using LunarConsolePlugin;

namespace LunarConsolePluginInternal
{
    delegate bool CActionFilter(CAction action);

    public interface ICRegistryDelegate
    {
        void OnActionRegistered(CRegistry registry, CAction action);
        void OnActionUnregistered(CRegistry registry, CAction action);
        void OnVariableRegistered(CRegistry registry, CVar cvar);
    }

    public class CRegistry
    {
        readonly CActionList m_actions = new CActionList();
        readonly CVarList m_vars = new CVarList();

        ICRegistryDelegate m_delegate;

        #region Commands registry

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

            CAction action = m_actions.Find(name);
            if (action != null)
            {
                Log.w("Overriding action: {0}", name);
                action.ActionDelegate = actionDelegate;
            }
            else
            {
                action = new CAction(name, actionDelegate);
                m_actions.Add(action);

                if (m_delegate != null)
                {
                    m_delegate.OnActionRegistered(this, action);
                }
            }

            return action;
        }

        public bool Unregister(string name)
        {
            return Unregister(delegate(CAction action)
            {
                return action.Name == name;
            });
        }

        public bool Unregister(int id)
        {
            return Unregister(delegate(CAction action)
            {
                return action.Id == id;
            });
        }

        public bool Unregister(Delegate del)
        {
            return Unregister(delegate(CAction action)
            {
                return action.ActionDelegate == del;
            });
        }

        public bool UnregisterAll(object target)
        {
            return target != null && Unregister(delegate(CAction action)
            {
                return action.ActionDelegate.Target == target;
            });
        }

        bool Unregister(CActionFilter filter)
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

        bool RemoveAction(CAction action)
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
    }
}

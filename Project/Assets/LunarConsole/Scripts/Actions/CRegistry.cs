using System;
using System.Collections.Generic;
using System.Reflection;

using LunarConsolePlugin;

namespace LunarConsolePluginInternal
{
    using CVarList = SortedList<CVar>;

    delegate bool CActionFilter(CAction action);

    public interface ICActionRegistryDelegate
    {
        void OnActionAdded(CRegistry registry, CAction action);
        void OnActionRemoved(CRegistry registry, CAction action);
        void OnVariableAdded(CRegistry registry, CVar cvar);
    }

    public class CRegistry
    {
        readonly CActionList m_actions = new CActionList();
        readonly CVarList m_vars = new CVarList();

        ICActionRegistryDelegate m_delegate;

        #region Commands registry

        internal CAction RegisterAction(string name, Delegate actionDelegate)
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
                action.actionDelegate = actionDelegate;
            }
            else
            {
                action = new CAction(name, actionDelegate);
                m_actions.Add(action);

                if (m_delegate != null)
                {
                    m_delegate.OnActionAdded(this, action);
                }
            }

            return action;
        }

        public bool Unregister(string name)
        {
            return Unregister(delegate(CAction action)
            {
                return action.name == name;
            });
        }

        public bool Unregister(int id)
        {
            return Unregister(delegate(CAction action)
            {
                return action.id == id;
            });
        }

        public bool Unregister(Delegate del)
        {
            return Unregister(delegate(CAction action)
            {
                return action.actionDelegate == del;
            });
        }

        public bool UnregisterAll(object target)
        {
            return target != null && Unregister(delegate(CAction action)
            {
                return action.actionDelegate.Target == target;
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
            if (m_actions.Remove(action.id))
            {
                if (m_delegate != null)
                {
                    m_delegate.OnActionRemoved(this, action);
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
                m_delegate.OnVariableAdded(this, cvar);
            }
        }

        #endregion

        #region Properties

        public ICActionRegistryDelegate registryDelegate
        {
            get { return m_delegate; }
            set { m_delegate = value; }
        }

        public CVarList cvars
        {
            get { return m_vars; }
        }

        #endregion
    }
}

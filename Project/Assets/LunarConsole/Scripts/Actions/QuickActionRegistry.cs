using System;
using System.Collections.Generic;
using System.Reflection;

using LunarConsolePlugin;

namespace LunarConsolePluginInternal
{
    using QuickActionLookup = Dictionary<int, CAction>;
    using QuickActionGroupList = MyList<QuickActionGroup>;
    using QuickActionGroupLookup = Dictionary<string, QuickActionGroup>;
    using CVarList = MyList<CVar>;

    delegate bool QuickActionFilter<T>(T cmd) where T : CAction;

    public interface IQuickActionRegistryDelegate
    {
        void OnActionAdded(QuickActionRegistry registry, CAction action);
        void OnActionRemoved(QuickActionRegistry registry, CAction action);
        void OnVariableAdded(QuickActionRegistry registry, CVar cvar);
    }

    public class QuickActionRegistry
    {
        readonly QuickActionLookup m_actionLookup = new QuickActionLookup();
        IQuickActionRegistryDelegate m_delegate;

        readonly QuickActionGroupList m_actionGroups = new QuickActionGroupList();
        readonly QuickActionGroupLookup m_actionGroupLookup = new QuickActionGroupLookup();
        readonly CVarList m_cvars = new CVarList();

        #region Commands registry

        internal CAction RegisterAction(string name, Delegate actionDelegate)
        {
            return RegisterAction("", name, actionDelegate);
        }

        internal CAction RegisterAction(string group, string name, Delegate actionDelegate)
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

            QuickActionGroup actionGroup = ResolveActionGroup(StringUtils.NonNullOrEmpty(group));
            CAction action = actionGroup.FindAction(name);
            if (action != null)
            {
                Log.w("Overriding action: {0}", name);
                action.actionDelegate = actionDelegate;
            }
            else
            {
                action = new CAction(name, actionDelegate);
                actionGroup.AddAction(action);

                if (m_delegate != null)
                {
                    m_delegate.OnActionAdded(this, action);
                }
            }

            m_actionLookup[action.id] = action;

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

        bool Unregister(QuickActionFilter<CAction> filter)
        {
            if (filter == null)
            {
                throw new ArgumentNullException("filter");
            }

            IList<CAction> actionsToRemove = new List<CAction>();
            foreach (var group in m_actionGroups)
            {
                foreach (var action in group.actions)
                {
                    if (filter(action))
                    {
                        actionsToRemove.Add(action);
                    }
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
            if (m_actionLookup.Remove(action.id))
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
            CAction cmd;
            if (m_actionLookup.TryGetValue(id, out cmd))
            {
                return cmd;
            }

            return null;
        }

        #endregion

        #region Action groups

        public QuickActionGroup FindActionGroup(string name)
        {
            QuickActionGroup group;
            return name != null && m_actionGroupLookup.TryGetValue(name, out group) ? group : null;
        }

        QuickActionGroup ResolveActionGroup(string name)
        {
            if (name == null)
            {
                throw new ArgumentNullException("name");
            }

            QuickActionGroup group;
            if (!m_actionGroupLookup.TryGetValue(name, out group))
            {
                group = new QuickActionGroup(name);
                m_actionGroups.Add(group);
                m_actionGroupLookup[name] = group;
            }
            return group;
        }

        #endregion

        #region Variables

        public void Register(CVar cvar)
        {
            m_cvars.Add(cvar);

            if (m_delegate != null)
            {
                m_delegate.OnVariableAdded(this, cvar);
            }
        }

        #endregion

        #region Properties

        public IQuickActionRegistryDelegate registryDelegate
        {
            get { return m_delegate; }
            set { m_delegate = value; }
        }

        public QuickActionGroupList actionGroups
        {
            get { return m_actionGroups; }
        }

        public CVarList cvars
        {
            get { return m_cvars; }
        }

        #endregion
    }
}

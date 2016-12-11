using System;
using System.Collections.Generic;

namespace LunarConsolePluginInternal
{
    using QuickActionList = MyList<QuickAction>;
    using QuickActionLookup = Dictionary<string, QuickAction>;

    public class QuickActionGroup : IComparable<QuickActionGroup>
    {
        readonly string m_name;
        readonly QuickActionList m_actions;
        readonly QuickActionLookup m_actionLookup;

        public QuickActionGroup(string name)
        {
            m_name = name;
            m_actions = new QuickActionList();
            m_actionLookup = new QuickActionLookup();
        }

        internal void AddAction(QuickAction action)
        {
            if (action.group != null)
            {
                throw new ArgumentException("Action already belongs to a group");
            }

            m_actions.Add(action);
            m_actionLookup[action.name] = action;
            action.group = this;
        }

        internal bool RemoveAction(QuickAction action)
        {
            if (action.group != this)
            {
                throw new ArgumentException("Action doesn't belong to a group");
            }

            if (m_actionLookup.Remove(action.name))
            {
                m_actions.Remove(action);
                action.group = null;
                return true;
            }

            return false;
        }

        public QuickAction FindAction(string name)
        {
            QuickAction action;
            return m_actionLookup.TryGetValue(name, out action) ? action : null;
        }

        #region IComparable implementation

        public int CompareTo(QuickActionGroup other)
        {
            return m_name.CompareTo(other.name);
        }

        #endregion

        #region Properties

        public string name
        {
            get { return m_name; }
        }

        public QuickActionList actions
        {
            get { return m_actions; }
        }

        #endregion
    }
}


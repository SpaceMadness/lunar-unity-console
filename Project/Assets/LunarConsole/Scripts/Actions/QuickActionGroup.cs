using System;
using System.Collections.Generic;

namespace LunarConsolePluginInternal
{
    using QuickActionList = MyList<CAction>;
    using QuickActionLookup = Dictionary<string, CAction>;

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

        internal void AddAction(CAction action)
        {
            m_actions.Add(action);
            m_actionLookup[action.name] = action;
        }

        internal bool RemoveAction(CAction action)
        {
            if (m_actionLookup.Remove(action.name))
            {
                m_actions.Remove(action);
                return true;
            }

            return false;
        }

        public CAction FindAction(string name)
        {
            CAction action;
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


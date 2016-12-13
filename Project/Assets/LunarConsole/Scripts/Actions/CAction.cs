using System;
using System.Collections.Generic;
using System.Reflection;
using System.Text;

using UnityEngine;

namespace LunarConsolePluginInternal
{
    public class CAction : IComparable<CAction>
    {
        static readonly string[] kEmptyArgs = new String[0];
        static int s_nextActionId;

        readonly int m_id;
        readonly string m_name;
        Delegate m_actionDelegate;

        public CAction(string name, Delegate actionDelegate)
        {
            if (name == null)
            {
                throw new ArgumentNullException("name");
            }

            if (name.Length == 0)
            {
                throw new ArgumentException("Action name is empty");
            }

            if (actionDelegate == null)
            {
                throw new ArgumentNullException("Action delegate is null");
            }

            m_id = s_nextActionId++;
            m_name = name;
            m_actionDelegate = actionDelegate;
        }

        internal bool Execute()
        {
            try
            {
                return ReflectionUtils.Invoke(ActionDelegate, kEmptyArgs); // TODO: remove it
            }
            catch (ReflectionException e)
            {
                Log.e(e.Message);
            }
            catch (TargetInvocationException e)
            {
                Log.e(e.InnerException, "Error while executing command");
            }
            catch (Exception e)
            {
                Log.e(e, "Error while executing command");
            }

            return false;
        }

        #region Helpers

        internal bool StartsWith(string prefix)
        {
            return StringUtils.StartsWithIgnoreCase(Name, prefix);
        }

        #endregion

        #region IComparable

        public int CompareTo(CAction other)
        {
            return Name.CompareTo(other.Name);
        }

        #endregion

        #region String representation

        public override string ToString()
        {
            return string.Format("{0} ({1})", Name, ActionDelegate);
        }

        #endregion

        #region Properties

        public int Id
        {
            get { return m_id; }
        }

        public string Name
        {
            get { return m_name; }
        }

        public Delegate ActionDelegate
        {
            get { return m_actionDelegate; }
            set
            {
                if (value == null)
                {
                    throw new ArgumentNullException("actionDelegate");
                }
                m_actionDelegate = value;
            }
        }

        #endregion
    }

    public class CActionList : IEnumerable<CAction>
    {
        private readonly SortedList<CAction> m_actions;
        private readonly Dictionary<int, CAction> m_actionLookupById;
        private readonly Dictionary<string, CAction> m_actionLookupByName;

        public CActionList()
        {
            m_actions = new SortedList<CAction>();
            m_actionLookupById = new Dictionary<int, CAction>();
            m_actionLookupByName = new Dictionary<string, CAction>();
        }

        public void Add(CAction action)
        {
            m_actions.Add(action);
            m_actionLookupById.Add(action.Id, action);
            m_actionLookupByName.Add(action.Name, action);
        }

        public bool Remove(int id)
        {
            CAction action;
            if (m_actionLookupById.TryGetValue(id, out action))
            {
                m_actionLookupById.Remove(id);
                m_actionLookupByName.Remove(action.Name);
                m_actions.Remove(action);

                return true;
            }

            return false;
        }

        public CAction Find(string name)
        {
            CAction action;
            return m_actionLookupByName.TryGetValue(name, out action) ? action : null;
        }

        public CAction Find(int id)
        {
            CAction action;
            return m_actionLookupById.TryGetValue(id, out action) ? action : null;
        }

        #region IEnumerable implementation

        public IEnumerator<CAction> GetEnumerator()
        {
            return m_actions.GetEnumerator();
        }

        #endregion

        #region IEnumerable implementation

        System.Collections.IEnumerator System.Collections.IEnumerable.GetEnumerator()
        {
            return m_actions.GetEnumerator();
        }

        #endregion
    }
}

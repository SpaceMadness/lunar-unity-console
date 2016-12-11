using System;
using System.Collections.Generic;
using System.Reflection;
using System.Text;

using UnityEngine;

namespace LunarConsolePluginInternal
{
    public class QuickAction : IComparable<QuickAction>
    {
        static readonly string[] kEmptyArgs = new String[0];
        static int s_nextActionId;

        readonly int m_id;
        readonly string m_name;
        QuickActionGroup m_group;
        Delegate m_actionDelegate;

        public QuickAction(string name, Delegate actionDelegate)
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
                return ReflectionUtils.Invoke(actionDelegate, kEmptyArgs); // TODO: remove it
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

        internal bool RemoveFromGroup()
        {
            if (m_group != null)
            {
                m_group.RemoveAction(this);
                return true;
            }

            return false;
        }

        #region Helpers

        internal bool StartsWith(string prefix)
        {
            return StringUtils.StartsWithIgnoreCase(name, prefix);
        }

        #endregion

        #region IComparable

        public int CompareTo(QuickAction other)
        {
            return name.CompareTo(other.name);
        }

        #endregion

        #region String representation

        public override string ToString()
        {
            return string.Format("{0} ({1})", name, actionDelegate);
        }

        #endregion

        #region Properties

        public int id
        {
            get { return m_id; }
        }

        public string name
        {
            get { return m_name; }
        }

        public QuickActionGroup group
        {
            get { return m_group; }
            internal set { m_group = value; }
        }

        public string groupName
        {
            get { return m_group != null ? m_group.name : null; }
        }

        public Delegate actionDelegate
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
}

using System;
using System.Collections;
using System.Collections.Generic;

using UnityEngine;
using LunarConsolePluginInternal;

namespace LunarConsolePlugin
{
    public delegate void CVarChangedDelegate(CVar cvar);

    public enum CVarType
    {
        Boolean,
        Integer,
        Float,
        String
    }

    struct CValue
    {
        public string stringValue;
        public int intValue;
        public float floatValue;

        public bool Equals(ref CValue other)
        {
            return other.intValue == intValue &&
            other.floatValue == floatValue &&
            other.stringValue == stringValue;
        }
    }

    public class CVar : IEquatable<CVar>, IComparable<CVar>
    {
        private readonly string m_name;
        private readonly CVarType m_type;

        private CValue m_value;
        private CValue m_defaultValue;

        private CVarChangedDelegateList m_delegateList;

        public CVar(string name, bool defaultValue)
            : this(name, CVarType.Boolean)
        {
            this.IntValue = defaultValue ? 1 : 0;
            m_defaultValue = m_value;
        }

        public CVar(string name, int defaultValue)
            : this(name, CVarType.Integer)
        {
            this.IntValue = defaultValue;
            m_defaultValue = m_value;
        }

        public CVar(string name, float defaultValue)
            : this(name, CVarType.Float)
        {
            this.FloatValue = defaultValue;
            m_defaultValue = m_value;
        }

        public CVar(string name, string defaultValue)
            : this(name, CVarType.String)
        {
            this.Value = defaultValue;
            m_defaultValue = m_value;
        }

        private CVar(string name, CVarType type)
        {
            if (name == null)
            {
                throw new NullReferenceException("Name is null");
            }

            m_name = name;
            m_type = type;

            Register(this);
        }

        //////////////////////////////////////////////////////////////////////////////

        #region Registry

        private static void Register(CVar cvar)
        {
            LunarConsoleLittleHelper.Register(cvar);
        }

        #endregion

        //////////////////////////////////////////////////////////////////////////////

        #region Delegates

        public void AddDelegate(CVarChangedDelegate del)
        {
            if (del == null)
            {
                throw new ArgumentNullException("del");
            }

            if (m_delegateList == null)
            {
                m_delegateList = new CVarChangedDelegateList(1);
                m_delegateList.Add(del);
            }
            else if (!m_delegateList.Contains(del))
            {
                m_delegateList.Add(del);
            }
        }

        public void RemoveDelegate(CVarChangedDelegate del)
        {
            if (del != null && m_delegateList != null)
            {
                m_delegateList.Remove(del);

                if (m_delegateList.Count == 0)
                {
                    m_delegateList = null;
                }
            }
        }

        public void RemoveDelegates(object target)
        {
            if (target != null && m_delegateList != null)
            {
                for (int i = m_delegateList.Count - 1; i >= 0; --i)
                {
                    if (m_delegateList.Get(i).Target == target)
                    {
                        m_delegateList.RemoveAt(i);
                    }
                }

                if (m_delegateList.Count == 0)
                {
                    m_delegateList = null;
                }
            }
        }

        private void NotifyValueChanged()
        {
            if (m_delegateList != null && m_delegateList.Count > 0)
            {
                m_delegateList.NotifyValueChanged(this);
            }
        }

        #endregion

        //////////////////////////////////////////////////////////////////////////////

        #region IEquatable

        public bool Equals(CVar other)
        {
            return other != null &&
            other.m_name == m_name &&
            other.m_value.Equals(ref m_value) &&
            other.m_defaultValue.Equals(ref m_defaultValue) &&
            other.m_type == m_type;
        }

        #endregion

        //////////////////////////////////////////////////////////////////////////////

        #region IComparable

        public int CompareTo(CVar other)
        {
            return Name.CompareTo(other.Name);
        }

        #endregion

        //////////////////////////////////////////////////////////////////////////////

        #region Properties

        public string Name
        {
            get { return m_name; }
        }

        public CVarType Type
        {
            get { return m_type; }
        }

        public string DefaultValue
        {
            get { return m_defaultValue.stringValue; }
        }

        public bool IsString
        {
            get { return m_type == CVarType.String; }
        }

        public string Value
        {
            get { return m_value.stringValue; }
            set
            {
                bool changed = m_value.stringValue != value;

                m_value.stringValue = value;
                m_value.intValue = 0;
                m_value.floatValue = 0.0f;

                if (changed)
                {
                    NotifyValueChanged();
                }
            }
        }

        public bool IsInt
        {
            get { return m_type == CVarType.Integer || m_type == CVarType.Boolean; }
        }

        public int IntValue
        {
            get { return m_value.intValue; }
            set
            {
                bool changed = m_value.intValue != value;

                m_value.stringValue = StringUtils.ToString(value);
                m_value.intValue = value;
                m_value.floatValue = (float)value;

                if (changed)
                {
                    NotifyValueChanged();
                }
            }
        }

        public bool IsFloat
        {
            get { return m_type == CVarType.Float; }
        }

        public float FloatValue
        {
            get { return m_value.floatValue; }
            set
            {
                float oldValue = m_value.floatValue;

                m_value.stringValue = StringUtils.ToString(value);
                m_value.intValue = (int)value;
                m_value.floatValue = value;

                if (oldValue != value)
                {
                    NotifyValueChanged();
                }
            }
        }

        public bool IsBool
        {
            get { return m_type == CVarType.Boolean; }
        }

        public bool BoolValue
        {
            get { return m_value.intValue != 0; }
            set { this.IntValue = value ? 1 : 0; }
        }

        public bool IsDefault
        {
            get { return m_value.Equals(m_defaultValue); }
            set
            {
                bool changed = this.IsDefault ^ value;
                m_value = m_defaultValue;

                if (changed)
                {
                    NotifyValueChanged();
                }
            }
        }

        #endregion
    }

    class CVarChangedDelegateList : BaseList<CVarChangedDelegate>
    {
        public CVarChangedDelegateList(int capacity)
            : base(NullCVarChangedDelegate, capacity)
        {
        }

        public void NotifyValueChanged(CVar cvar)
        {
            try
            {
                Lock();

                int elementsCount = list.Count;
                for (int i = 0; i < elementsCount; ++i) // do not update added items on that tick
                {
                    try
                    {
                        list[i](cvar);
                    }
                    catch (Exception e)
                    {
                        Log.e(e);
                    }
                }
            }
            finally
            {
                Unlock();
            }
        }

        static void NullCVarChangedDelegate(CVar cvar)
        {
        }
    }
}
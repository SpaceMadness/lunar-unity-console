using System;
using System.Collections;
using System.Collections.Generic;

using UnityEngine;
using LunarConsolePluginInternal;

namespace LunarConsolePlugin
{
    public delegate void CVarChangedDelegate(CVar cvar);

    public enum CFlags
    {   
        /// <summary>
        /// No flags (default value)
        /// </summary>
        None      = 0,

        /// <summary>
        /// The value cannot be modified from the command line
        /// </summary>
        Readonly  = 1 << 1,

        /// <summary>
        /// Only accessible in debug mode
        /// </summary>
        Debug     = 1 << 2,

        /// <summary>
        /// Won't be listed in cvarlist
        /// </summary>
        Hidden    = 1 << 3,

        /// <summary>
        /// System variable (hidden in cvarlist unless "--all (-a)" option is used)
        /// </summary>
        System    = 1 << 4,

        /// <summary>
        /// Don't save into config file
        /// </summary>
        NoArchive = 1 << 5,
    }

    public enum CVarType
    {
        Boolean,
        Integer,
        Float,
        String,
        Color,
        Rect,
        Vector2,
        Vector3,
        Vector4
    }

    struct CValue
    {
        public string stringValue;
        public int intValue;
        public float floatValue;
        public Vector4 vectorValue;

        public bool Equals(ref CValue other)
        {
            return other.intValue == intValue &&
                other.floatValue == floatValue &&
                other.stringValue == stringValue &&
                other.vectorValue == vectorValue;
        }
    }

    public class CVar : IEquatable<CVar>, IComparable<CVar>
    {
        private readonly string m_name;
        private readonly CVarType m_type;

        private CValue m_value;
        private CValue m_defaultValue;

        private CFlags m_flags;

        private CVarChangedDelegateList m_delegateList;

        public CVar(string name, bool defaultValue)
            : this(name, defaultValue, CFlags.None)
        {
        }

        public CVar(string name, bool defaultValue, CFlags flags)
            : this(name, CVarType.Boolean, flags)
        {
            this.IntValue = defaultValue ? 1 : 0;
            m_defaultValue = m_value;
        }

        public CVar(string name, int defaultValue)
            : this(name, defaultValue, CFlags.None)
        {
        }

        public CVar(string name, int defaultValue, CFlags flags)
            : this(name, CVarType.Integer, flags)
        {
            this.IntValue = defaultValue;
            m_defaultValue = m_value;
        }

        public CVar(string name, float defaultValue)
            : this(name, defaultValue, CFlags.None)
        {
        }

        public CVar(string name, float defaultValue, CFlags flags)
            : this(name, CVarType.Float, flags)
        {
            this.FloatValue = defaultValue;
            m_defaultValue = m_value;
        }

        public CVar(string name, string defaultValue)
            : this(name, defaultValue, CFlags.None)
        {
        }

        public CVar(string name, string defaultValue, CFlags flags)
            : this(name, CVarType.String, flags)
        {
            this.Value = defaultValue;
            m_defaultValue = m_value;
        }

        public CVar(string name, Color defaultValue)
            : this(name, defaultValue, CFlags.None)
        {
        }

        public CVar(string name, Color defaultValue, CFlags flags)
            : this(name, CVarType.Color, flags)
        {
            this.ColorValue = defaultValue;;
            m_defaultValue = m_value;
        }

        public CVar(string name, Rect defaultValue)
            : this(name, defaultValue, CFlags.None)
        {
        }

        public CVar(string name, Rect defaultValue, CFlags flags)
            : this(name, CVarType.Rect, flags)
        {
            this.RectValue = defaultValue;
            m_defaultValue = m_value;
        }

        public CVar(string name, Vector2 defaultValue)
            : this(name, defaultValue, CFlags.None)
        {
        }

        public CVar(string name, Vector2 defaultValue, CFlags flags)
            : this(name, CVarType.Vector2, flags)
        {
            this.Vector2Value = defaultValue;
            m_defaultValue = m_value;
        }

        public CVar(string name, Vector3 defaultValue)
            : this(name, defaultValue, CFlags.None)
        {
        }

        public CVar(string name, Vector3 defaultValue, CFlags flags)
            : this(name, CVarType.Vector3, flags)
        {
            this.Vector3Value = defaultValue;
            m_defaultValue = m_value;
        }

        public CVar(string name, Vector4 defaultValue)
            : this(name, defaultValue, CFlags.None)
        {
        }

        public CVar(string name, Vector4 defaultValue, CFlags flags)
            : this(name, CVarType.Vector4, flags)
        {
            this.Vector4Value = defaultValue;
            m_defaultValue = m_value;
        }

        private CVar(string name, CVarType type, CFlags flags)
        {
            if (name == null)
            {
                throw new NullReferenceException("Name is null");
            }

            m_name = name;
            m_type = type;
            m_flags = flags;

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
                other.m_type == m_type &&
                other.m_flags == m_flags;
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
                m_value.vectorValue = new Vector4();

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
                m_value.vectorValue = new Vector4();

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
                m_value.vectorValue = new Vector4();

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

        public bool IsColor
        {
            get { return m_type == CVarType.Color; }
        }

        public Color ColorValue
        {
            get { return new Color(
                m_value.vectorValue.x, 
                m_value.vectorValue.y, 
                m_value.vectorValue.z, 
                m_value.vectorValue.w); 
            }
            set {
                Vector4 vector = new Vector4(value.r, value.g, value.b, value.a);
                bool changed = m_value.vectorValue != vector;

                m_value.stringValue = StringUtils.ToString(ref value);
                m_value.intValue = 0;
                m_value.floatValue = 0.0f;
                m_value.vectorValue = vector;
                m_value.intValue = (int)(ColorUtils.ToRGBA(ref value));

                if (changed)
                {
                    NotifyValueChanged();
                }
            }
        }

        public bool IsRect
        {
            get { return m_type == CVarType.Rect; }
        }

        public Rect RectValue
        {
            get { return new Rect(
                m_value.vectorValue.x, 
                m_value.vectorValue.y, 
                m_value.vectorValue.z, 
                m_value.vectorValue.w); 
            }

            set {
                Vector4 vector = new Vector4(value.x, value.y, value.width, value.height);
                bool changed = m_value.vectorValue != vector;

                m_value.stringValue = StringUtils.ToString(ref value);
                m_value.intValue = 0;
                m_value.floatValue = 0.0f;
                m_value.vectorValue = vector;

                if (changed)
                {
                    NotifyValueChanged();
                }
            }
        }

        public bool IsVector2
        {
            get { return m_type == CVarType.Vector2; }
        }

        public Vector2 Vector2Value
        {
            get { return new Vector2(
                m_value.vectorValue.x, 
                m_value.vectorValue.y); 
            }
            set {
                Vector4 vector = new Vector4(value.x, value.y);
                bool changed = m_value.vectorValue != vector;

                m_value.stringValue = StringUtils.ToString(ref value);
                m_value.intValue = 0;
                m_value.floatValue = 0.0f;
                m_value.vectorValue = vector;

                if (changed)
                {
                    NotifyValueChanged();
                }
            }
        }

        public bool IsVector3
        {
            get { return m_type == CVarType.Vector3; }
        }

        public Vector3 Vector3Value
        {
            get { return new Vector3(
                m_value.vectorValue.x, 
                m_value.vectorValue.y,
                m_value.vectorValue.z); 
            }
            set {
                Vector4 vector = new Vector4(value.x, value.y, value.z);
                bool changed = m_value.vectorValue != vector;

                m_value.stringValue = StringUtils.ToString(ref value);
                m_value.intValue = 0;
                m_value.floatValue = 0.0f;
                m_value.vectorValue = vector;

                if (changed)
                {
                    NotifyValueChanged();
                }
            }
        }

        public bool IsVector4
        {
            get { return m_type == CVarType.Vector4; }
        }

        public Vector4 Vector4Value
        {
            get { return m_value.vectorValue; }
            set {
                bool changed = m_value.vectorValue != value;

                m_value.stringValue = StringUtils.ToString(ref value);
                m_value.intValue = 0;
                m_value.floatValue = 0.0f;
                m_value.vectorValue = value;

                if (changed)
                {
                    NotifyValueChanged();
                }
            }
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

        internal bool IsHidden
        {
            get { return HasFlag(CFlags.Hidden); }
        }

        internal bool IsSystem
        {
            get { return HasFlag(CFlags.System); }
        }

        internal bool IsDebug
        {
            get { return HasFlag(CFlags.Debug); }
        }

        internal bool HasFlag(CFlags flag)
        {
            return (m_flags & flag) != 0;
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
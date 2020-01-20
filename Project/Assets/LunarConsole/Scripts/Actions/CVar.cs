//
//  CVar.cs
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2019 Alex Lementuev, SpaceMadness.
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
using System.CodeDom;
using System.Collections;
using System.Collections.Generic;
using System.Reflection;

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

    public struct CValue
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

    public struct CVarValueRange
    {
        public static readonly CVarValueRange Undefined = new CVarValueRange(float.NaN, float.NaN);

        public readonly float min;
        public readonly float max;

        public CVarValueRange(float min, float max)
        {
            this.min = min;
            this.max = max;
        }

        public bool IsValid
        {
            get { return !float.IsNaN(min) && !float.IsNaN(max); }
        }
    }

    public enum CFlags
    {
        /// <summary>
        /// No flags (default value)
        /// </summary>
        None      = 0,

        /// <summary>
        /// Won't be listed in UI
        /// </summary>
        Hidden    = 1 << 1,

        /// <summary>
        /// Don't save between sessions
        /// </summary>
        NoArchive = 1 << 2
    }

    public interface ICVarProxy
    {
        CValue Value { get; set; }
    }

    public class CVarProxy<T> : ICVarProxy
    {
        private Func<T> GetFunc { get; }
        private Func<T, T> SetFunc { get; }
        private CValue _value;

        public CVarProxy()
        {
            GetFunc = () =>
            {
                if (typeof(T) == typeof(int))
                {
                    return (T) Convert.ChangeType(_value.intValue, typeof(T));
                }

                if (typeof(T) == typeof(float))
                {
                    return (T) Convert.ChangeType(_value.floatValue, typeof(T));
                }

                if (typeof(T) == typeof(string))
                {
                    return (T) Convert.ChangeType(_value.stringValue, typeof(T));
                }

                throw new ArgumentException();
            };

            SetFunc = value => value;
        }

        public CVarProxy(Func<T> getFunc, Func<T, T> setFunc)
        {
            GetFunc = getFunc;
            SetFunc = setFunc;
        }

        public CValue Value
        {
            get
            {
                var value = GetFunc.Invoke();

                if (typeof(T) == typeof(int))
                {
                    _value.intValue = (int) Convert.ChangeType(value, typeof(int));
                }
                else if (typeof(T) == typeof(float))
                {
                    _value.floatValue = (float) Convert.ChangeType(value, typeof(float));
                }
                else if (typeof(T) == typeof(string))
                {
                    _value.stringValue = (string) Convert.ChangeType(value, typeof(string));
                }
                else
                {
                    throw new ArgumentException();
                }

                return _value;
            }
            set
            {
                _value = value;

                if (typeof(T) == typeof(int))
                {
                    SetFunc.Invoke((T) Convert.ChangeType(_value.intValue, typeof(T)));
                    return;
                }

                if (typeof(T) == typeof(float))
                {
                    SetFunc.Invoke((T) Convert.ChangeType(_value.floatValue, typeof(T)));
                    return;
                }

                if (typeof(T) == typeof(string))
                {
                    SetFunc.Invoke((T) Convert.ChangeType(_value.stringValue, typeof(T)));
                    return;
                }

                throw new ArgumentException();
            }
        }
    }

    public class CVar : IEquatable<CVar>, IComparable<CVar>
    {
        private static int s_nextId;

        private readonly int m_id;
        private readonly string m_name;
        private readonly CVarType m_type;
        private readonly CFlags m_flags;

        private CValue m_defaultValue;
        private CVarValueRange m_range = CVarValueRange.Undefined;

        private CVarChangedDelegateList m_delegateList;

        private CVarProxy<int> _intProxy;
        private CVarProxy<float> _floatProxy;
        private CVarProxy<string> _stringProxy;

        private ICVarProxy Proxy
        {
            get
            {
                if (_intProxy != null) return _intProxy;
                if (_floatProxy != null) return _floatProxy;
                if (_stringProxy != null) return _stringProxy;

                throw new ArgumentException();
            }
        }

        public CVar(string name, bool defaultValue, CFlags flags = CFlags.None, CVarProxy<int> proxy = null)
            : this(name, CVarType.Boolean, flags)
        {
            _intProxy = proxy ?? new CVarProxy<int>();
            IntValue = defaultValue ? 1 : 0;
            m_defaultValue = new CValue { intValue = defaultValue ? 1 : 0 };
        }

        public CVar(string name, int defaultValue, CFlags flags = CFlags.None, CVarProxy<int> proxy = null)
            : this(name, CVarType.Integer, flags)
        {
            _intProxy = proxy ?? new CVarProxy<int>();
            IntValue = defaultValue;
            m_defaultValue = new CValue { intValue = defaultValue };
        }

        public CVar(string name, float defaultValue, CFlags flags = CFlags.None, CVarProxy<float> proxy = null)
            : this(name, CVarType.Float, flags)
        {
            _floatProxy = proxy ?? new CVarProxy<float>();
            FloatValue = defaultValue;
            m_defaultValue = new CValue { floatValue = defaultValue };
        }

        public CVar(string name, string defaultValue, CFlags flags = CFlags.None, CVarProxy<string> proxy = null)
            : this(name, CVarType.String, flags)
        {
            _stringProxy = proxy ?? new CVarProxy<string>();
            Value = defaultValue;
            m_defaultValue = new CValue { stringValue = defaultValue };
        }

        private CVar(string name, CVarType type, CFlags flags)
        {
            if (name == null)
            {
                throw new ArgumentNullException("name");
            }

            m_id = ++s_nextId;

            m_name = name;
            m_type = type;
            m_flags = flags;
        }

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
            other.Proxy.Value.Equals(Proxy.Value) &&
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

        public int Id
        {
            get { return m_id; }
        }

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
            get { return Proxy.Value.stringValue; }
            set
            {
                var changed = Proxy.Value.stringValue != value;

                Proxy.Value = new CValue
                {
                    stringValue = value,
                    floatValue = IsInt || IsFloat ? StringUtils.ParseFloat(value, 0.0f) : 0.0f,
                    intValue = IsInt || IsFloat ? (int)FloatValue : 0
                };

                if (changed)
                {
                    NotifyValueChanged();
                }
            }
        }

        public CVarValueRange Range
        {
            get { return m_range; }
            set { m_range = value; }
        }

        public bool HasRange
        {
            get { return m_range.IsValid; }
        }

        public bool IsInt
        {
            get { return m_type == CVarType.Integer || m_type == CVarType.Boolean; }
        }

        public int IntValue
        {
            get => Proxy.Value.intValue;
            set
            {
                var changed = Proxy.Value.intValue != value;

                Proxy.Value = new CValue
                {
                    stringValue = StringUtils.ToString(value),
                    intValue = value,
                    floatValue = value
                };

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
            get { return Proxy.Value.floatValue; }
            set
            {
                var changed = Proxy.Value.floatValue != value;

                Proxy.Value = new CValue
                {
                    stringValue = StringUtils.ToString(value),
                    intValue = (int) value,
                    floatValue = value
                };

                if (changed)
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
            get { return Proxy.Value.intValue != 0; }
            set
            {
                Proxy.Value = new CValue
                {
                    intValue = value ? 1 : 0
                };
            }
        }

        public bool IsDefault
        {
            get { return Proxy.Value.Equals(m_defaultValue); }
            set
            {
                bool changed = IsDefault ^ value;
                Proxy.Value = m_defaultValue;

                if (changed)
                {
                    NotifyValueChanged();
                }
            }
        }

        public bool HasFlag(CFlags flag)
        {
            return (m_flags & flag) != 0;
        }

        public CFlags Flags
        {
            get { return m_flags; }
        }

        #endregion

        #region Operators

        public static implicit operator string(CVar cvar)
        {
            return cvar.Proxy.Value.stringValue;
        }

        public static implicit operator int(CVar cvar)
        {
            return cvar.Proxy.Value.intValue;
        }

        public static implicit operator float(CVar cvar)
        {
            return cvar.Proxy.Value.floatValue;
        }

        public static implicit operator bool(CVar cvar)
        {
            return cvar.Proxy.Value.intValue != 0;
        }

        #endregion
    }

    public class CVarList : IEnumerable<CVar>
    {
        private readonly List<CVar> m_variables;
        private readonly Dictionary<int, CVar> m_lookupById;

        public CVarList()
        {
            m_variables = new List<CVar>();
            m_lookupById = new Dictionary<int, CVar>();
        }

        public void Add(CVar variable)
        {
            m_variables.Add(variable);
            m_lookupById.Add(variable.Id, variable);
        }

        public bool Remove(int id)
        {
            CVar variable;
            if (m_lookupById.TryGetValue(id, out variable))
            {
                m_lookupById.Remove(id);
                m_variables.Remove(variable);

                return true;
            }

            return false;
        }

        public CVar Find(int id)
        {
            CVar variable;
            return m_lookupById.TryGetValue(id, out variable) ? variable : null;
        }

        public CVar Find(string name)
        {
            foreach (var cvar in m_variables)
            {
                if (cvar.Name == name)
                {
                    return cvar;
                }
            }
            return null;
        }

        public void Clear()
        {
            m_variables.Clear();
            m_lookupById.Clear();
        }

        #region IEnumerable implementation

        public IEnumerator<CVar> GetEnumerator()
        {
            return m_variables.GetEnumerator();
        }

        #endregion

        #region IEnumerable implementation

        IEnumerator IEnumerable.GetEnumerator()
        {
            return m_variables.GetEnumerator();
        }

        #endregion

        public int Count
        {
            get { return m_variables.Count; }
        }
    }

    [AttributeUsage (AttributeTargets.Field)]
    public sealed class CVarRangeAttribute : Attribute
    {
        public readonly float min;

        public readonly float max;

        public CVarRangeAttribute(float min, float max)
        {
            this.min = min;
            this.max = max;
        }
    }

    [AttributeUsage(AttributeTargets.Class)]
    public class CVarContainerAttribute : Attribute
    {
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
                        Log.e(e, "Exception while calling value changed delegate for '{0}'", cvar.Name);
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

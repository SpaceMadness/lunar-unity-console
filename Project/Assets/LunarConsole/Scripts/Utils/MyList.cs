using System;
using System.Collections.Generic;

namespace LunarConsolePluginInternal
{
    /// <summary>
    /// Utility list for storing and retreiving items in a sorted order
    /// </summary>
    public class MyList<T> : IEnumerable<T> where T : class, IComparable<T>
    {
        public delegate void EachCallback(T element);
        public delegate bool FilterCallback(T element);

        readonly LinkedList<T> m_list;

        public MyList()
        {
            m_list = new LinkedList<T>();
        }

        #region Operations

        public T Add(T element)
        {
            if (element == null)
            {
                throw new ArgumentNullException("element");
            }

            for (LinkedListNode<T> node = m_list.First; node != null; node = node.Next)
            {
                int compare = element.CompareTo(node.Value);
                if (compare < 0)
                {
                    m_list.AddBefore(node, element);
                    return null;
                }

                if (compare == 0)
                {
                    T oldElement = node.Value;
                    node.Value = element;
                    return oldElement;
                }
            }

            m_list.AddLast(element);
            return null;
        }

        public bool Remove(T element)
        {
            return m_list.Remove(element);
        }

        public void Each(EachCallback callback)
        {
            if (callback == null)
            {
                throw new ArgumentNullException("callback");
            }

            LinkedListNode<T> node = m_list.First;
            while (node != null)
            {
                LinkedListNode<T> next = node.Next;
                callback(node.Value);
                node = next;
            }
        }

        public IList<T> Filter(FilterCallback callback)
        {
            return Filter(new List<T>(), callback);
        }

        public IList<T> Filter(IList<T> outList, FilterCallback callback)
        {
            if (outList == null)
            {
                throw new ArgumentNullException("Out list is null");
            }

            if (callback == null)
            {
                throw new ArgumentNullException("callback");
            }

            foreach (T element in m_list)
            {
                if (callback(element))
                {
                    outList.Add(element);
                }
            }

            return outList;
        }

        public void Clear()
        {
            m_list.Clear();
        }

        public T[] ToArray()
        {
            T[] array = new T[m_list.Count];
            m_list.CopyTo(array, 0);
            return array;
        }

        #endregion

        #region Properties

        public int Count
        {
            get { return m_list.Count; }
        }

        #endregion

        #region IEnumerable implementation

        public IEnumerator<T> GetEnumerator()
        {
            return m_list.GetEnumerator();
        }

        #endregion

        #region IEnumerable implementation

        System.Collections.IEnumerator System.Collections.IEnumerable.GetEnumerator()
        {
            return m_list.GetEnumerator();
        }

        #endregion
    }
}


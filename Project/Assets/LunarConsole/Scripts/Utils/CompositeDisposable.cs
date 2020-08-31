using System;
using System.Collections.Generic;

namespace LunarConsolePlugin
{
    public sealed class CompositeDisposable : IDisposable
    {
        private readonly List<IDisposable> m_children;

        public CompositeDisposable(int capacity = 2)
        {
            m_children = new List<IDisposable>(capacity);
        }

        public CompositeDisposable Add(IDisposable disposable)
        {
            if (disposable == null)
            {
                throw new ArgumentNullException("disposable");
            }
        
            if (!m_children.Contains(disposable))
            {
                m_children.Add(disposable);
            }

            return this;
        }

        public CompositeDisposable Add(params IDisposable[] disposables)
        {
            for (var i = 0; i < disposables.Length; i++)
            {
                Add(disposables[i]);
            }

            return this;
        }

        public bool Remove(IDisposable disposable)
        {
            return m_children.Remove(disposable);
        }
    
        public void Dispose()
        {
            for (var i = 0; i < m_children.Count; i++)
            {
                m_children[i].Dispose();
            }

            m_children.Clear();
        }
    }
}
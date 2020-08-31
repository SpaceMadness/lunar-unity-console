using System;

namespace LunarConsolePluginInternal
{
    internal sealed class ConsoleEntryDisposer : IDisposable
    {
        private readonly CRegistry m_registry;
        private readonly ConsoleEntry m_entry;

        public ConsoleEntryDisposer(CRegistry registry, ConsoleEntry entry)
        {
            if (registry == null)
            {
                throw new ArgumentNullException("registry");
            }

            if (entry == null)
            {
                throw new ArgumentNullException("entry");
            }

            m_registry = registry;
            m_entry = entry;
        }

        public void Dispose()
        {
            m_registry.Unregister(m_entry);
        }
    }
}
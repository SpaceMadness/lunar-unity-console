using System;

namespace LunarConsolePluginInternal
{
    internal sealed class NullDisposable : IDisposable
    {
        public static readonly IDisposable Instance = new NullDisposable();
        
        public void Dispose()
        {
        }
    }
}
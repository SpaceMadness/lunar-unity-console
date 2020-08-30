using System;

namespace LunarConsolePlugin
{
    [AttributeUsage(AttributeTargets.Method, Inherited = true, AllowMultiple = false)]
    public sealed class ConsoleActionAttribute : Attribute
    {
        public string Name { get; set; }
    }
}
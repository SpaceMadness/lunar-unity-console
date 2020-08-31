using System;

namespace LunarConsolePlugin
{
    [AttributeUsage(AttributeTargets.Method, Inherited = true, AllowMultiple = false)]
    public sealed class ConsoleActionAttribute : Attribute
    {
        public ConsoleActionAttribute()
        {
            Platform = TargetPlatform.All;
        }
        
        /// <summary>
        /// Display name of the action (method name will be used if missing).
        /// </summary>
        public string DisplayName { get; set; }

        /// <summary>
        /// Target platform for the action (TargetPlatform.All will be used if missing).
        /// </summary>
        public TargetPlatform Platform { get; set; }

        /// <summary>
        /// Indicates when ever a confirmation dialog will be shown before running the action (useful for destructive actions).
        /// </summary>
        public bool RequiresConfirmation { get; set; }
    }
}
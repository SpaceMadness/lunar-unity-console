using System;

namespace QuickActions
{
    struct QuickActionInfo
    {
        public readonly string name;
        public readonly Delegate actionDelegate;

        public QuickActionInfo(string name, Action actionDelegate)
        {
            this.name = name;
            this.actionDelegate = actionDelegate;
        }
    }
}
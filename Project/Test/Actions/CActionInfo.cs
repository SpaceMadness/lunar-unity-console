using System;

namespace Actions
{
    struct CActionInfo
    {
        public readonly string name;
        public readonly Delegate actionDelegate;

        public CActionInfo(string name, Action actionDelegate)
        {
            this.name = name;
            this.actionDelegate = actionDelegate;
        }
    }
}
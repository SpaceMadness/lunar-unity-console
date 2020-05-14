using System;
using UnityEditor;

namespace LunarConsoleEditorInternal
{
    public static class EditorHelper
    {
        public static void DelayCallOnce(Action action)
        {
            new EditorCallback(action).ScheduleDelayCall();
        }
        
        private sealed class EditorCallback
        {
            private readonly Action action;

            public EditorCallback(Action action)
            {
                this.action = action;
            }

            public void ScheduleDelayCall()
            {
                EditorApplication.delayCall += Invoke;
            }

            private void Invoke()
            {
                try
                {
                    action();
                }
                finally
                {
                    // ReSharper disable once DelegateSubtraction
                    EditorApplication.delayCall -= Invoke;
                }
            }
        }
    }
}
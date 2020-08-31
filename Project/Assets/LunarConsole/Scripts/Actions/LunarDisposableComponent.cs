using System;
using System.Collections.Generic;
using LunarConsolePlugin;
using UnityEngine;

namespace LunarConsolePluginInternal
{
    internal class LunarDisposableComponent : MonoBehaviour
    {
        internal IDisposable disposable;

        private void OnDisable()
        {
            if (disposable != null)
            {
                disposable.Dispose();
            }
        }
    }
}
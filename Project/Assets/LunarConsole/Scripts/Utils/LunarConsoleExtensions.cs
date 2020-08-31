using LunarConsolePlugin;
using UnityEngine;

namespace LunarConsolePluginInternal
{
    internal static class LunarConsoleExtensions
    {
        public static bool IsSupported(this TargetPlatform targetPlatform)
        {
            var platform = Application.platform;
            return platform == RuntimePlatform.Android && (targetPlatform & TargetPlatform.Android) != 0 ||
                   platform == RuntimePlatform.IPhonePlayer && (targetPlatform & TargetPlatform.IOS) != 0 ||
                   Application.isEditor && (targetPlatform & TargetPlatform.Editor) != 0;
        }
    }
}
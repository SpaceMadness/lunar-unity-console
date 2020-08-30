using System;
using System.Collections.Generic;
using System.Reflection;
using LunarConsolePlugin;
using LunarConsolePluginInternal;
using UnityEngine;

public class LunarConsoleLifecycle : MonoBehaviour
{
    /// <summary>
    /// Maintains a list of all actions registered by this game object.
    /// </summary>
    private readonly List<int> m_actionIds = new List<int>();
    
    private void Awake()
    {
        if (!LunarConsole.isConsoleEnabled)
        {
            Destroy(this);
        }
    }

    private void OnEnable()
    {
        var components = ListComponents();
        for (var index = 0; index < components.Length; index++)
        {
            RegisterComponent(components[index]);
        }
    }

    private void OnDisable()
    {
        for (var index = 0; index < m_actionIds.Count; index++)
        {
            LunarConsole.UnregisterAction(m_actionIds[index]);
        }
        m_actionIds.Clear();
    }

    private void RegisterComponent(MonoBehaviour component)
    {
        var type = component.GetType();
        var methods = ClassUtils.ListMethods(type, MethodFilter);
        for (var index = 0; index < methods.Count; index++)
        {
            var method = methods[index];
            var attribute = method.GetCustomAttribute<ConsoleActionAttribute>();
            if (attribute == null)
            {
                continue;
            }

            var parameterCount = method.GetParameters().Length;
            if (parameterCount > 0)
            {
                Log.w("Action method parameters are not supported yet: {0}", method);
                continue;
            }
            
            var actionName = string.IsNullOrWhiteSpace(attribute.Name)
                ? StringUtils.ToDisplayName(method.Name)
                : attribute.Name;

            var action = new CAction(actionName, method.CreateDelegate(typeof(Action), component));
            LunarConsole.RegisterAction(action);
            m_actionIds.Add(action.Id);
        }
    }

    private MonoBehaviour[] ListComponents()
    {
        return gameObject.GetComponents<MonoBehaviour>();
    }

    private static bool MethodFilter(MethodInfo method)
    {
        return true;
    }
}

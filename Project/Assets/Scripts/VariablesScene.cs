using System;
using System.Collections;
using System.Collections.Generic;
using System.Threading;

using UnityEngine;
using UnityEngine.UI;
using LunarConsolePlugin;

public class VariablesScene : MonoBehaviour
{
    CVar[] m_variables = {
        Variables.c_bool,
        Variables.c_float,
        Variables.c_int,
        Variables.c_range,
        Variables.c_string
    };

    void Start()
    {
        foreach (CVar cvar in m_variables)
        {
            cvar.AddDelegate(OnVariableChanged);
        }
    }

    void OnDestroy()
    {
        foreach (CVar cvar in m_variables)
        {
            cvar.RemoveDelegate(OnVariableChanged);
        }
    }

    void OnVariableChanged(CVar cvar)
    {
        Debug.LogFormat("{0} {1}", cvar.Name, cvar.Value);
    }
}

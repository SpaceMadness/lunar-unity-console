//
//  VariablesScene.cs
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2015-2020 Alex Lementuev, SpaceMadness.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//


using System;
using UnityEngine;
using LunarConsolePlugin;
using UnityEngine.UI;

public class VariablesScene : MonoBehaviour
{
    private readonly CVar[] m_variables = {
        Variables.myBool,
        Variables.myFloat,
        Variables.myInt,
        Variables.myRange,
        Variables.myString,
        Variables.myEnum
    };

    [SerializeField]
    private Text[] m_variableLabels;

    private void Start()
    {
        for (var index = 0; index < m_variables.Length; index++)
        {
            var cvar = m_variables[index];
            m_variableLabels[index].text = string.Format("{0}: {1}", cvar.Name, cvar.Value);
        }

        foreach (CVar cvar in m_variables)
        {
            cvar.AddDelegate(OnVariableChanged);
        }
    }

    private void OnDestroy()
    {
        foreach (CVar cvar in m_variables)
        {
            cvar.RemoveDelegate(OnVariableChanged);
        }
    }

    private void OnVariableChanged(CVar cvar)
    {
        int index = Array.IndexOf(m_variables, cvar);
        m_variableLabels[index].text = string.Format("{0}: {1}", cvar.Name, cvar.Value);
    }
}

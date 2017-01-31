using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using LunarConsolePlugin;

public class Scene2UI : MonoBehaviour
{
    [SerializeField]
    InputField m_floatField;

    [SerializeField]
    InputField m_intField;

    [SerializeField]
    InputField m_stringField;

    [SerializeField]
    Toggle m_toggle;

    void Start()
    {
        m_intField.text = Variables.c_int;
        m_floatField.text = Variables.c_float;
        m_stringField.text = Variables.c_string;
        m_toggle.isOn = Variables.c_bool;

        Variables.c_float.AddDelegate(VariableChanged);
        Variables.c_int.AddDelegate(VariableChanged);
        Variables.c_string.AddDelegate(VariableChanged);
        Variables.c_bool.AddDelegate(VariableChanged);
    }

    void OnDestroy()
    {
        Variables.c_float.RemoveDelegate(VariableChanged);
        Variables.c_int.RemoveDelegate(VariableChanged);
        Variables.c_string.RemoveDelegate(VariableChanged);
        Variables.c_bool.RemoveDelegate(VariableChanged);
    }

    void VariableChanged(CVar variable)
    {
        if (variable == Variables.c_bool)
        {
            m_toggle.isOn = variable;
        }
        else if (variable == Variables.c_int)
        {
            m_intField.text = variable;
        }
        else if (variable == Variables.c_float)
        {
            m_floatField.text = variable;
        }
        else if (variable == Variables.c_string)
        {
            m_stringField.text = variable;
        }
    }
}

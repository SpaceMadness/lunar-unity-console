using System.Collections;
using System.Collections.Generic;
using UnityEngine;

using LunarConsolePlugin;

public class TestActions : MonoBehaviour
{
    // Use this for initialization
    void Start()
    {
        LunarConsole.RegisterAction("Set Red Color", delegate() {
            SetColor(Color.red);
        });

        LunarConsole.RegisterAction("Set Green Color", delegate() {
            SetColor(Color.green);
        });

        LunarConsole.RegisterAction("Set Blue Color", delegate() {
            SetColor(Color.blue);
        });
    }

    void OnDestroy()
    {
        LunarConsole.UnregisterAllActions(this);
    }

    void SetColor(Color color)
    {
        Camera.main.backgroundColor = color;
    }
}

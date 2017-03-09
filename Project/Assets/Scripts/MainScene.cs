using System.Collections;
using System.Collections.Generic;

using UnityEngine;

using LunarConsolePlugin;

public class MainScene : MonoBehaviour
{
    public void EnableConsole()
    {
        LunarConsole.SetConsoleEnabled(true);
    }

    public void DisableConsole()
    {
        LunarConsole.SetConsoleEnabled(false);
    }
}

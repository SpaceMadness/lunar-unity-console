using System.Collections;
using System.Collections.Generic;

using UnityEngine;
using LunarConsolePlugin;

public class APIScene : MonoBehaviour
{
    void Start()
    {
        LunarConsole.onConsoleOpened += OnConsoleOpened;
        LunarConsole.onConsoleClosed += OnConsoleClosed;
    }

    void OnDestroy()
    {
        LunarConsole.onConsoleOpened -= OnConsoleOpened;
        LunarConsole.onConsoleClosed -= OnConsoleClosed;
    }

    public void OpenConsole()
    {
        LunarConsole.Show();
    }

    public void CloseConsole()
    {
        LunarConsole.Hide();
    }

    public void ClearConsole()
    {
        LunarConsole.Clear();
    }

    private void OnConsoleOpened()
    {
        Debug.Log("Console open");
    }

    private void OnConsoleClosed()
    {
        Debug.Log("Console close");
    }
}

//
//  APIScene.cs
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2019 Alex Lementuev, SpaceMadness.
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

﻿using System.Collections;
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

    public void EnableConsole()
    {
        LunarConsole.SetConsoleEnabled(true);
    }

    public void DisableConsole()
    {
        LunarConsole.SetConsoleEnabled(false);
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

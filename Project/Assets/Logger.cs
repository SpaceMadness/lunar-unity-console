//
//  Logger.cs
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2016 Alex Lementuev, SpaceMadness.
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

using UnityEngine;

using System;
using System.Collections;
using System.Collections.Generic;

using LunarConsolePlugin;

using Random = System.Random;

public delegate void LogDelegate(string message);

public class Logger : MonoBehaviour
{
    private static readonly string[] MESSAGES = {
        "Engineer: Somebody has planted a bomb. (lit. It appears that an unknown party has planted an explosive.)",
        "Radio Operator: We're getting a video on the main screen (lit. A visual is coming on the main screen.)",
        "CATS: With the cooperation of Federation Forces, all your base are belong to us",
        "CATS: Treasure what little time remains in your lives.",
        "Captain: I ask of you, ZIG [units]...",
        "Captain: ...let there be hope for our future (lit. ...to our future, [restore] hope.)"
    };

    readonly LogDelegate[] logDelegates = {
        Logger1.LogDebug,
        Logger1.LogWarning,
        Logger1.LogError,
        Logger2.LogDebug,
        Logger2.LogWarning,
        Logger2.LogError,
        Logger3.LogDebug,
        Logger3.LogWarning,
        Logger3.LogError
    };

    public float delay = 0.5f;

    void Start()
    {
        Shuffle(logDelegates);
    }

    public void LogMessages()
    {
        StartCoroutine(LogMessages(delay));
    }

    public void LogError()
    {
        throw new Exception("Error!");
    }

    public void ShowConsole()
    {
        LunarConsole.Show();
    }

    public void ClearConsole()
    {
        LunarConsole.Clear();
    }

    IEnumerator LogMessages(float delay)
    {
        int i = 0;
        int j = 0;
        while (true)
        {
            logDelegates[i](MESSAGES[j]);
            i = (i + 1) % logDelegates.Length;
            j = (j + 1) % MESSAGES.Length;

            yield return new WaitForSeconds(delay);
        }
    }

    void Shuffle<T>(IList<T> list)
    {
        Random random = new Random();
        int n = list.Count;
        while (n > 1)
        {
            --n;
            int k = random.Next(n + 1);
            T value = list[k];
            list[k] = list[n];
            list[n] = value;
        }
    }
}

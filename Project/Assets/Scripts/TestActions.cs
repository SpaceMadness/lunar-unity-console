//
//  TestActions.cs
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2017 Alex Lementuev, SpaceMadness.
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

public class TestActions : MonoBehaviour
{
    // Use this for initialization
    void Start()
    {
        LunarConsole.RegisterAction("Set Red Color", delegate() {
            StartCoroutine(SetColor(Color.red));
        });

        LunarConsole.RegisterAction("Set Green Color", delegate() {
            StartCoroutine(SetColor(Color.green));
        });

        LunarConsole.RegisterAction("Set Blue Color", delegate() {
            StartCoroutine(SetColor(Color.blue));
        });
    }

    void OnDestroy()
    {
        LunarConsole.UnregisterAllActions(this);
    }

    IEnumerator SetColor(Color color)
    {
        var currentColor = Camera.main.backgroundColor;
        float duration = 0.5f;
        float elapsed = 0.0f;

        while (elapsed < duration)
        {
            Camera.main.backgroundColor = Color.Lerp(currentColor, color, elapsed / duration);

            elapsed += Time.deltaTime;
            yield return null;
        }

        Camera.main.backgroundColor = color;
    }
}

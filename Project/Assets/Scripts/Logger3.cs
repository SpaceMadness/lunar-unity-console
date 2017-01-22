//
//  Logger3.cs
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

﻿using System;
using UnityEngine;

public static class Logger3
{
    public static void LogDebug(string message)
    {
        Debug.Log(message);
    }
    
    public static void LogWarning(string message)
    {
        Debug.LogWarning(message);
    }
    
    public static void LogError(string message)
    {
        Debug.LogError(message);
    }
    
    public static void ThrowException(string message)
    {
        throw new Exception(message);
    }
}

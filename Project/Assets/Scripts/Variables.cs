//
//  Variables.cs
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

﻿using UnityEngine;
using System.Collections;

using LunarConsolePlugin;

[CVarContainer]
public static class Variables
{
    public static readonly CVar c_int = new CVar("int", 10);
    public static readonly CVar c_bool = new CVar("bool", true);
    public static readonly CVar c_float = new CVar("float", 3.14f);
    public static readonly CVar c_string = new CVar("string", "Test");
    public static readonly CVar c_volatile = new CVar("volatile", 0.0f, CFlags.NoArchive);

    [CVarRange(1.5f, 11.5f)]
    public static readonly CVar c_range = new CVar("range", 2.5f);
}

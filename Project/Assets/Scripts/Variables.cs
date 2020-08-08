//
//  Variables.cs
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


using LunarConsolePlugin;

public enum MyEnum
{
    One,
    Two,
    Three
}

[CVarContainer]
public static class Variables
{
    public static readonly CVar myBool = new CVar("My boolean value", true);
    public static readonly CVar myFloat = new CVar("My float value", 3.14f);
    public static readonly CVar myInt = new CVar("My integer value", 10);
    public static readonly CVar myString = new CVar("My string value", "Test");
    [CVarRange(0.0f, 1.0f)]
    public static readonly CVar myRange = new CVar("My range value", 0.5f);
    public static readonly CEnumVar<MyEnum> myEnum = new CEnumVar<MyEnum>("My enum value", MyEnum.Two);
}

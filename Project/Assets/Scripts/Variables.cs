using UnityEngine;
using System.Collections;

using LunarConsolePlugin;

[CVarContainer]
public static class Variables
{
    public static readonly CVar c_int = new CVar("int", 10);
    public static readonly CVar c_bool = new CVar("bool", true);
    public static readonly CVar c_float = new CVar("float", 3.14f);
    public static readonly CVar c_string = new CVar("string", "Test");
}

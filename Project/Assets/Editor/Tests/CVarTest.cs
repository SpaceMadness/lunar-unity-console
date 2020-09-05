//
//  CVarTest.cs
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


using NUnit.Framework;

using LunarConsolePlugin;

public class CVarTest : TestFixtureBase
{
    [SetUp]
    public void SetUp()
    {
        RunSetUp();
    }

    [Test]
    public void TestIntVar()
    {
        CVar v = new CVar("int", 10);
        Assert.IsTrue(v.IsDefault);
        Assert.AreEqual(10, v.IntValue);
        Assert.AreEqual(10.0f, v.FloatValue);
        Assert.AreEqual("10", v.Value);
        Assert.AreEqual(true, v.BoolValue);
        Assert.AreEqual(10, (int)v);
        Assert.AreEqual(10.0f, (float)v);
        Assert.AreEqual("10", (string)v);
        Assert.AreEqual(true, (bool)v);

        v.IntValue = 20;
        Assert.IsFalse(v.IsDefault);
        Assert.AreEqual(20, v.IntValue);
        Assert.AreEqual(20.0f, v.FloatValue);
        Assert.AreEqual("20", v.Value);
        Assert.AreEqual(true, v.BoolValue);
        Assert.AreEqual(20, (int)v);
        Assert.AreEqual(20.0f, (float)v);
        Assert.AreEqual("20", (string)v);
        Assert.AreEqual(true, (bool)v);

        v.Reset();
        Assert.IsTrue(v.IsDefault);
        Assert.AreEqual(10, v.IntValue);
        Assert.AreEqual(10.0f, v.FloatValue);
        Assert.AreEqual("10", v.Value);
        Assert.AreEqual(true, v.BoolValue);
        Assert.AreEqual(10, (int)v);
        Assert.AreEqual(10.0f, (float)v);
        Assert.AreEqual("10", (string)v);
        Assert.AreEqual(true, (bool)v);
    }

    [Test]
    public void TestFloatVar()
    {
        CVar v = new CVar("float", 10.0f);
        Assert.AreEqual(10, v.IntValue);
        Assert.AreEqual(10.0f, v.FloatValue);
        Assert.AreEqual("10", v.Value);
        Assert.AreEqual(true, v.BoolValue);
        Assert.AreEqual(10, (int)v);
        Assert.AreEqual(10.0f, (float)v);
        Assert.AreEqual("10", (string)v);
        Assert.AreEqual(true, (bool)v);
    }

    [Test]
    public void TestBoolVar()
    {
        CVar v = new CVar("bool", true);
        Assert.AreEqual(1, v.IntValue);
        Assert.AreEqual(1.0f, v.FloatValue);
        Assert.AreEqual("1", v.Value);
        Assert.AreEqual(true, v.BoolValue);
        Assert.AreEqual(1, (int)v);
        Assert.AreEqual(1.0f, (float)v);
        Assert.AreEqual("1", (string)v);
        Assert.AreEqual(true, (bool)v);

        v = new CVar("bool", false);
        Assert.AreEqual(0, v.IntValue);
        Assert.AreEqual(0.0f, v.FloatValue);
        Assert.AreEqual("0", v.Value);
        Assert.AreEqual(false, v.BoolValue);
        Assert.AreEqual(0, (int)v);
        Assert.AreEqual(0.0f, (float)v);
        Assert.AreEqual("0", (string)v);
        Assert.AreEqual(false, (bool)v);
    }
    
    [Test]
    public void TestStringVar()
    {
        CVar v = new CVar("string", "default");
        Assert.AreEqual(0, v.IntValue);
        Assert.AreEqual(0.0f, v.FloatValue);
        Assert.AreEqual("default", v.Value);
        Assert.AreEqual(true, v.BoolValue);
        Assert.AreEqual(0, (int)v);
        Assert.AreEqual(0.0f, (float)v);
        Assert.AreEqual("default", (string)v);
        Assert.AreEqual(true, (bool)v);
    }
    
    [Test]
    public void TestEnumVar()
    {
        CEnumVar<MyEnum> v = new CEnumVar<MyEnum>("enum", MyEnum.Two);
        Assert.AreEqual(0, v.IntValue);
        Assert.AreEqual(0.0f, v.FloatValue);
        Assert.AreEqual("Two", v.Value);
        Assert.AreEqual(true, v.BoolValue);
        Assert.AreEqual(MyEnum.Two, v.EnumValue);
        Assert.AreEqual(0, (int)v);
        Assert.AreEqual(0.0f, (float)v);
        Assert.AreEqual("default", (string)v);
        Assert.AreEqual(true, (bool)v);
        Assert.AreEqual(MyEnum.Two, (MyEnum) v);
    }

    [Test]
    public void TestDelegate()
    {
        CVar boolVar = new CVar("bool", true);
        boolVar.AddDelegate(delegate(CVar cvar)
        {
            AddResult("bool " + (bool)cvar);
        });

        CVar intVar = new CVar("int", 10);
        intVar.AddDelegate(delegate(CVar cvar)
        {
            AddResult("int " + (int)cvar);
        });

        CVar floatVar = new CVar("float", 3.14f);
        floatVar.AddDelegate(delegate(CVar cvar)
        {
            AddResult("float " + (float)cvar);
        });

        CVar stringVar = new CVar("string", "value");
        stringVar.AddDelegate(delegate(CVar cvar)
        {
            AddResult("string " + (string)cvar);
        });

        boolVar.BoolValue = false;
        AssertResult("bool False");

        boolVar.BoolValue = true;
        AssertResult("bool True");

        intVar.IntValue = 20;
        AssertResult("int 20");

        floatVar.FloatValue = 6.28f;
        AssertResult("float 6.28");

        stringVar.Value = "new value";
        AssertResult("string new value");
    }
    
    public enum MyEnum
    {
        One,
        Two,
        Three
    }
}

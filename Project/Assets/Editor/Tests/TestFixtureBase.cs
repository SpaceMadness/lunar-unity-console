//
//  TestFixtureBase.cs
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

﻿using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

using NUnit.Framework;
using LunarConsolePluginInternal;

public class TestFixtureBase
{
    private List<string> result;

    protected virtual void RunSetUp()
    {
        result = new List<string>();
    }

    protected virtual void RunTearDown()
    {
        result = null;
    }

    protected void AssertList<T>(IList<T> actual, params T[] expected) 
        where T : IEquatable<T>
    {
        Assert.AreEqual(expected.Length, actual.Count, "Expected: [{0}]\nActual: [{1}]", Join(", ", expected), Join(", ", actual));
        for (int i = 0; i < expected.Length; ++i)
        {
            Assert.AreEqual(expected[i], actual[i]);
        }
    }

    protected void AssertArray<T>(T[] actual, params T[] expected)
        where T : IEquatable<T>
    {
        Assert.IsNotNull(actual);
        Assert.IsNotNull(expected);

        Assert.AreEqual(actual.Length, expected.Length, "Expected: [{0}]\nActual: [{1}]", Join(", ", expected), Join(", ", actual));
        for (int i = 0; i < expected.Length; ++i)
        {
            Assert.AreEqual(expected[i], actual[i]);
        }
    }

    protected void AssertTypes<T>(IList<T> actual, params Type[] expected)
    {
        Assert.AreEqual(actual.Count, expected.Length, "Expected: [{0}]\nActual: [{1}]", Join(", ", expected), JoinTypes(", ", actual));
        for (int i = 0; i < expected.Length; ++i)
        {
            Assert.AreEqual(actual[i].GetType(), expected[i]);
        }
    }

    protected string Join<T>(string separator, IList<T> list)
    {
        return StringUtils.Join(list, separator);
    }

    private string JoinTypes<T>(string separator, IList<T> list)
    {
        Type[] types = new Type[list.Count];
        for (int i = 0; i < list.Count; ++i)
        {
            types[i] = list[i].GetType();
        }

        return Join(separator, types);
    }

    protected void AssertResult(params string[] expected)
    {
        AssertList(result, expected);
        result.Clear();
    }

    protected void AddResult(string str)
    {
        result.Add(str);
    }

    protected List<String> Result
    {
        get { return result; }
    }
}

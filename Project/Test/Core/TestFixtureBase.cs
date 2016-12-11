using System;
using System.Collections.Generic;

using NUnit.Framework;

using LunarConsolePluginInternal;

public abstract class TestFixtureBase
{
    private List<String> result;

    #region Life cycle

    protected virtual void RunSetUp()
    {
        result = new List<String>();
    }

    protected virtual void RunTearDown()
    {
        result = null;
    }

    #endregion

    #region Helpers

    protected void AssertList<T>(IList<T> actual, params T[] expected) 
        where T : IEquatable<T>
    {
        Assert.AreEqual(expected.Length, actual.Count, StringUtils.TryFormat("Expected: [{0}]\nActual: [{1}]"), Join(", ", expected), Join(", ", actual));
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

        Assert.AreEqual(actual.Length, expected.Length, StringUtils.TryFormat("Expected: [{0}]\nActual: [{1}]"), Join(", ", expected), Join(", ", actual));
        for (int i = 0; i < expected.Length; ++i)
        {
            Assert.AreEqual(expected[i], actual[i]);
        }
    }

    protected void AssertTypes<T>(IList<T> actual, params Type[] expected)
    {
        Assert.AreEqual(actual.Count, expected.Length, StringUtils.TryFormat("Expected: [{0}]\nActual: [{1}]"), Join(", ", expected), JoinTypes(", ", actual));
        for (int i = 0; i < expected.Length; ++i)
        {
            Assert.AreEqual(actual[i].GetType(), expected[i]);
        }
    }

    protected void AssertTitles(IList<QuickAction> command, params string[] expected)
    {
        Assert.AreEqual(command.Count, expected.Length, StringUtils.TryFormat("Expected: [{0}]\nActual: [{1}]"), Join(", ", expected), JoinTypes(", ", command));
        for (int i = 0; i < expected.Length; ++i)
        {
            Assert.AreEqual(command[i].name, expected[i]);
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

    #endregion
}


using System.Collections.Generic;

using UnityEngine;
using UnityEditor;

using NUnit.Framework;

using LunarConsolePluginInternal;

public class StringUtilsTest
{
    #region Serialization

    [Test]
    public void TestStringDeserialization()
    {
        string data = "key3:value with\\nlinebreak\nkey1:value\nkey6:\nkey4:value with \"quotes\"\nkey2:value with whitespace\nkey5:value with: separator";
        IDictionary<string, string> dictionary = StringUtils.DeserializeString(data);
        Assert.AreEqual(6, dictionary.Count);
        Assert.AreEqual("value", dictionary["key1"]);
        Assert.AreEqual("value with whitespace", dictionary["key2"]);
        Assert.AreEqual("value with\nlinebreak", dictionary["key3"]);
        Assert.AreEqual("value with \"quotes\"", dictionary["key4"]);
        Assert.AreEqual("value with: separator", dictionary["key5"]);
        Assert.AreEqual("", dictionary["key6"]);
    }

    #endregion

    #region Display name

    [Test]
    public void TestDisplayName1()
    {
        Assert.IsNull(StringUtils.ToDisplayName(null));
    }

    [Test]
    public void TestDisplayName2()
    {
        Assert.AreEqual("", StringUtils.ToDisplayName(""));
    }

    [Test]
    public void TestDisplayName3()
    {
        Assert.AreEqual("Name", StringUtils.ToDisplayName("name"));
    }

    [Test]
    public void TestDisplayName4()
    {
        Assert.AreEqual("Pretty Display Name", StringUtils.ToDisplayName("prettyDisplayName"));
    }

    [Test]
    public void TestDisplayName5()
    {
        Assert.AreEqual("Display 12", StringUtils.ToDisplayName("display12"));
    }

    #endregion
}

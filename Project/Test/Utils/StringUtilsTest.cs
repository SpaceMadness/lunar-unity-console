using System;
using System.Collections.Generic;

using NUnit.Framework;

using LunarConsolePluginInternal;

namespace Utils
{
    [TestFixture]
    public class StringUtilsTest : TestFixtureBase
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
    }
}


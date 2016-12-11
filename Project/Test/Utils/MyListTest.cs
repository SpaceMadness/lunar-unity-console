using System;
using NUnit.Framework;

using LunarConsolePluginInternal;

namespace Utils
{
    [TestFixture]
    public class MyListTest : TestFixtureBase
    {
        MyList<string> m_list;

        #region Setup

        [SetUp]
        public void SetUp()
        {
            m_list = new MyList<string>();
        }

        #endregion

        #region Testing

        [Test]
        public void TestAddItems()
        {
            m_list.Add("3");
            m_list.Add("2");
            m_list.Add("1");
            m_list.Add("4");

            AssertList("1", "2", "3", "4");
        }

        [Test]
        public void TestDuplicateItems()
        {
            m_list.Add("3");
            m_list.Add("3");
            m_list.Add("2");
            m_list.Add("1");
            m_list.Add("1");
            m_list.Add("4");

            AssertList("1", "2", "3", "4");
        }

        [Test]
        public void TestRemoveItems()
        {
            m_list.Add("3");
            m_list.Add("2");
            m_list.Add("1");
            m_list.Add("4");

            m_list.Remove("2");

            AssertList("1", "3", "4");
        }

        [Test]
        public void TestToArray()
        {
            m_list.Add("1");
            m_list.Add("2");
            m_list.Add("3");

            AssertArray(m_list.ToArray(), "1", "2", "3");
        }

        #endregion

        #region Helpers

        void AssertList(params string[] expected)
        {
            Assert.AreEqual(expected.Length, m_list.Count);

            int index = 0;
            foreach (string actual in m_list)
            {
                Assert.AreEqual(expected[index], actual);
                ++index;
            }
        }

        #endregion
    }
}


//
//  SortedListTest.cs
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
using NUnit.Framework;

using LunarConsolePluginInternal;

namespace Utils
{
    [TestFixture]
    public class SortedListTest : TestFixtureBase
    {
        SortedList<string> m_list;

        #region Setup

        [SetUp]
        public void SetUp()
        {
            m_list = new SortedList<string>();
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


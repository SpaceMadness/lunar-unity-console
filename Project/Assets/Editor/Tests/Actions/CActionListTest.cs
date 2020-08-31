//
//  CActionListTest.cs
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


using System;
using NUnit.Framework;
using LunarConsolePluginInternal;

namespace Actions
{
    [TestFixture]
    public class CActionListTest : TestFixtureBase
    {
        private int m_nextActionId;
        
        #region Setup
        
        [SetUp]
        public void SetUp()
        {
            RunSetUp();
            m_nextActionId = 0;
        }
        
        #endregion

        [Test]
        public void TestAdd()
        {
            Action del1 = Del1;
            Action del2 = Del2;
            Action del3 = Del3;

            CActionList actionList = new CActionList();
            CAction a1 = CreateAction("a1", del1);
            CAction a2 = CreateAction("a2", del2);
            CAction a3 = CreateAction("a3", del3);

            actionList.Add(a1);
            actionList.Add(a2);
            actionList.Add(a3);

            CAction[] actions = { a1, a2, a3 };
            int index = 0;
            foreach (var action in actionList)
            {
                Assert.AreSame(actions[index++], action);
            }
            
            Assert.AreSame(a1, actionList.Find(a1.Id));
            Assert.AreSame(a2, actionList.Find(a2.Id));
            Assert.AreSame(a3, actionList.Find(a3.Id));
        }

        [Test]
        public void TestRemove()
        {
            Action del1 = Del1;
            Action del2 = Del2;
            Action del3 = Del3;
            
            CActionList actionList = new CActionList();
            CAction a1 = CreateAction("a1", del1);
            CAction a2 = CreateAction("a2", del2);
            CAction a3 = CreateAction("a3", del3);
            
            actionList.Add(a1);
            actionList.Add(a2);
            actionList.Add(a3);

            Assert.AreSame(a1, actionList.Find(a1.Id));
            Assert.AreSame(a2, actionList.Find(a2.Id));
            Assert.AreSame(a3, actionList.Find(a3.Id));

            actionList.Remove(a1.Id);
            
            Assert.IsNull(actionList.Find(a1.Id));
            Assert.AreSame(a2, actionList.Find(a2.Id));
            Assert.AreSame(a3, actionList.Find(a3.Id));

            actionList.Remove(a2.Id);
            
            Assert.IsNull(actionList.Find(a1.Id));
            Assert.IsNull(actionList.Find(a2.Id));
            Assert.AreSame(a3, actionList.Find(a3.Id));

            actionList.Remove(a3.Id);
            
            Assert.IsNull(actionList.Find(a1.Id));
            Assert.IsNull(actionList.Find(a2.Id));
            Assert.IsNull(actionList.Find(a3.Id));

            actionList.Remove(a1.Id);
            actionList.Remove(a2.Id);
            actionList.Remove(a3.Id);
            Assert.IsNull(actionList.Find(a1.Id));
            Assert.IsNull(actionList.Find(a2.Id));
            Assert.IsNull(actionList.Find(a3.Id));
        }

        [Test]
        public void TestFind()
        {
            Action del1 = Del1;
            Action del2 = Del2;
            Action del3 = Del3;
            
            CActionList actionList = new CActionList();
            CAction a1 = CreateAction("a1", del1);
            CAction a2 = CreateAction("a2", del2);
            CAction a3 = CreateAction("a3", del3);
            
            actionList.Add(a1);
            actionList.Add(a2);
            actionList.Add(a3);

            Assert.AreSame(a1, actionList.Find(a1.Id));
            Assert.AreSame(a2, actionList.Find(a2.Id));
            Assert.AreSame(a3, actionList.Find(a3.Id));

            Assert.AreSame(a1, actionList.Find(a1.Name));
            Assert.AreSame(a2, actionList.Find(a2.Name));
            Assert.AreSame(a3, actionList.Find(a3.Name));
        }

        #region Helpers

        private void Del1()
        {
        }

        private void Del2()
        {
        }

        private void Del3()
        {
        }
        
        private CAction CreateAction(string name, Action callback)
        {
            return new CAction(m_nextActionId++, name, callback, requiresConfirmation: false);
        }

        #endregion
    }
}
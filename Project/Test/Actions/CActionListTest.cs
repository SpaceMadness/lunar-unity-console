//
//  CActionListTest.cs
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
using System.Threading;
using System.Collections.Generic;

using NUnit.Framework;

using LunarConsolePlugin;
using LunarConsolePluginInternal;

namespace Actions
{
    using Assert = NUnit.Framework.Assert;
    
    [TestFixture]
    public class CActionListTest : TestFixtureBase
    {
        #region Setup
        
        [SetUp]
        public void SetUp()
        {
            RunSetUp();
        }
        
        #endregion

        [Test]
        public void TestAdd()
        {
            Action del1 = Del1;
            Action del2 = Del2;
            Action del3 = Del3;

            CActionList actionList = new CActionList();
            CAction a1 = new CAction("a1", del1);
            CAction a2 = new CAction("a2", del2);
            CAction a3 = new CAction("a3", del3);

            actionList.Add(a3);
            actionList.Add(a1);
            actionList.Add(a2);

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
            CAction a1 = new CAction("a1", del1);
            CAction a2 = new CAction("a2", del2);
            CAction a3 = new CAction("a3", del3);
            
            actionList.Add(a3);
            actionList.Add(a1);
            actionList.Add(a2);
            
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
            CAction a1 = new CAction("a1", del1);
            CAction a2 = new CAction("a2", del2);
            CAction a3 = new CAction("a3", del3);
            
            actionList.Add(a3);
            actionList.Add(a1);
            actionList.Add(a2);

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

        #endregion
    }
}
using System;
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
            
            Assert.AreSame(a1, actionList.Find(a1.id));
            Assert.AreSame(a2, actionList.Find(a2.id));
            Assert.AreSame(a3, actionList.Find(a3.id));
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
            
            Assert.AreSame(a1, actionList.Find(a1.id));
            Assert.AreSame(a2, actionList.Find(a2.id));
            Assert.AreSame(a3, actionList.Find(a3.id));

            actionList.Remove(a1.id);
            
            Assert.IsNull(actionList.Find(a1.id));
            Assert.AreSame(a2, actionList.Find(a2.id));
            Assert.AreSame(a3, actionList.Find(a3.id));

            actionList.Remove(a2.id);
            
            Assert.IsNull(actionList.Find(a1.id));
            Assert.IsNull(actionList.Find(a2.id));
            Assert.AreSame(a3, actionList.Find(a3.id));

            actionList.Remove(a3.id);
            
            Assert.IsNull(actionList.Find(a1.id));
            Assert.IsNull(actionList.Find(a2.id));
            Assert.IsNull(actionList.Find(a3.id));

            actionList.Remove(a1.id);
            actionList.Remove(a2.id);
            actionList.Remove(a3.id);
            Assert.IsNull(actionList.Find(a1.id));
            Assert.IsNull(actionList.Find(a2.id));
            Assert.IsNull(actionList.Find(a3.id));
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

            Assert.AreSame(a1, actionList.Find(a1.id));
            Assert.AreSame(a2, actionList.Find(a2.id));
            Assert.AreSame(a3, actionList.Find(a3.id));

            Assert.AreSame(a1, actionList.Find(a1.name));
            Assert.AreSame(a2, actionList.Find(a2.name));
            Assert.AreSame(a3, actionList.Find(a3.name));
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
using System;
using System.Threading;
using System.Collections.Generic;

using NUnit.Framework;

using LunarConsolePlugin;
using LunarConsolePluginInternal;

namespace QuickActions
{
    using Assert = NUnit.Framework.Assert;

    [TestFixture]
    public class QuickActionGroupTest : TestFixtureBase
    {
        QuickActionGroup m_actionGroup;

        [SetUp]
        public void SetUp()
        {
            m_actionGroup = new QuickActionGroup("Action Group");
        }

        #region Testing

        [Test]
        public void TestSortedAdd()
        {
            AddAction("a2", ActionDelegate2);
            AddAction("a3", ActionDelegate3);
            AddAction("a1", ActionDelegate1);

            AssertActions(
                new QuickActionInfo("a1", ActionDelegate1),
                new QuickActionInfo("a2", ActionDelegate2),
                new QuickActionInfo("a3", ActionDelegate3)
            );
        }

        [Test]
        public void TestDuplicatedAction()
        {
            AddAction("a2", ActionDelegate2);
            AddAction("a3", ActionDelegate3);
            AddAction("a1", ActionDelegate1);
            AddAction("a3", ActionDelegate4);

            AssertActions(
                new QuickActionInfo("a1", ActionDelegate1),
                new QuickActionInfo("a2", ActionDelegate2),
                new QuickActionInfo("a3", ActionDelegate4)
            );
        }

        [Test]
        public void TestRemove()
        {
            CAction a1 = AddAction("a1", ActionDelegate1);
            CAction a2 = AddAction("a2", ActionDelegate2);
            CAction a3 = AddAction("a3", ActionDelegate3);

            AssertActions(
                new QuickActionInfo("a1", ActionDelegate1),
                new QuickActionInfo("a2", ActionDelegate2),
                new QuickActionInfo("a3", ActionDelegate3)
            );

            RemoveAction(a1);
            AssertActions(
                new QuickActionInfo("a2", ActionDelegate2),
                new QuickActionInfo("a3", ActionDelegate3)
            );

            RemoveAction(a2);
            AssertActions(
                new QuickActionInfo("a3", ActionDelegate3)
            );

            RemoveAction(a3);
            AssertActions();
        }

        #endregion

        #region Delegates

        void ActionDelegate1()
        {
        }

        void ActionDelegate2()
        {
        }

        void ActionDelegate3()
        {
        }

        void ActionDelegate4()
        {
        }

        #endregion

        #region Helpers

        CAction AddAction(string name, Action actionDelegate)
        {
            CAction action = new CAction(name, actionDelegate);
            m_actionGroup.AddAction(action);
            return action;
        }

        void RemoveAction(CAction action)
        {
            m_actionGroup.RemoveAction(action);
        }

        void AssertActions(params QuickActionInfo[] expected)
        {
            var actions = m_actionGroup.actions;
            Assert.AreEqual(expected.Length, actions.Count);

            int index = 0;
            foreach (var action in actions)
            {
                Assert.AreEqual(expected[index].name, action.name);
                Assert.AreEqual(expected[index].actionDelegate, action.actionDelegate);
                ++index;
            }
        }

        #endregion
    }
}


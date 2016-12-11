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
    public class QuickActionRegistryTest : TestFixtureBase, IQuickActionRegistryDelegate
    {
        QuickActionRegistry m_actionRegistry;

        #region Setup

        [SetUp]
        public void SetUp()
        {
            RunSetUp();

            m_actionRegistry = new QuickActionRegistry();
            m_actionRegistry.registryDelegate = this;
        }

        #endregion

        #region Register actions

        [Test]
        public void TestRegisterActionsDefaultGroup()
        {
            RegisterAction("a2", Del2);
            RegisterAction("a1", Del1);
            RegisterAction("a3", Del3);

            // should be in default group
            AssertGroups("");

            AssertActions("",
                new QuickActionInfo("a1", Del1),
                new QuickActionInfo("a2", Del2),
                new QuickActionInfo("a3", Del3)
            );
        }

        [Test]
        public void TestRegisterActionsMultipleGroups()
        {
            DelClass1 delClass1 = new DelClass1();
            DelClass2 delClass2 = new DelClass2();
            DelClass3 delClass3 = new DelClass3();

            RegisterAction("b", "b2", delClass2.Del2);
            RegisterAction("b", "b1", delClass2.Del1);
            RegisterAction("b", "b3", delClass2.Del3);

            RegisterAction("a", "a2", delClass1.Del2);
            RegisterAction("a", "a1", delClass1.Del1);
            RegisterAction("a", "a3", delClass1.Del3);

            RegisterAction("d2", Del2);
            RegisterAction("d1", Del1);
            RegisterAction("d3", Del3);

            RegisterAction("c", "c2", delClass3.Del2);
            RegisterAction("c", "c1", delClass3.Del1);
            RegisterAction("c", "c3", delClass3.Del3);

            AssertGroups("", "a", "b", "c");

            AssertActions("",
                new QuickActionInfo("d1", Del1),
                new QuickActionInfo("d2", Del2),
                new QuickActionInfo("d3", Del3)
            );

            AssertActions("a",
                new QuickActionInfo("a1", delClass1.Del1),
                new QuickActionInfo("a2", delClass1.Del2),
                new QuickActionInfo("a3", delClass1.Del3)
            );

            AssertActions("b",
                new QuickActionInfo("b1", delClass2.Del1),
                new QuickActionInfo("b2", delClass2.Del2),
                new QuickActionInfo("b3", delClass2.Del3)
            );

            AssertActions("c",
                new QuickActionInfo("c1", delClass3.Del1),
                new QuickActionInfo("c2", delClass3.Del2),
                new QuickActionInfo("c3", delClass3.Del3)
            );
        }

        [Test]
        public void TestRegisterMultipleActionsWithSameName()
        {
            RegisterAction("a2", Del2);
            RegisterAction("a3", Del3);
            RegisterAction("a1", Del1);
            RegisterAction("a3", Del4);

            // should be in default group
            AssertGroups("");

            AssertActions("",
                new QuickActionInfo("a1", Del1),
                new QuickActionInfo("a2", Del2),
                new QuickActionInfo("a3", Del4)
            );
        }

        #endregion

        #region Unregister actions

        [Test]
        public void TestUnregisterActionByName()
        {
            DelClass1 delClass1 = new DelClass1();
            DelClass2 delClass2 = new DelClass2();
            DelClass3 delClass3 = new DelClass3();

            RegisterAction("b", "a2", delClass2.Del2);
            RegisterAction("b", "a1", delClass2.Del1);
            RegisterAction("b", "a3", delClass2.Del3);

            RegisterAction("a", "a2", delClass1.Del2);
            RegisterAction("a", "a1", delClass1.Del1);
            RegisterAction("a", "a3", delClass1.Del3);

            RegisterAction("a2", Del2);
            RegisterAction("a1", Del1);
            RegisterAction("a3", Del3);

            RegisterAction("c", "a2", delClass3.Del2);
            RegisterAction("c", "a1", delClass3.Del1);
            RegisterAction("c", "a3", delClass3.Del3);

            UnregisterAction("a2");

            AssertActions("",
                new QuickActionInfo("a1", Del1),
                new QuickActionInfo("a3", Del3)
            );

            AssertActions("a",
                new QuickActionInfo("a1", delClass1.Del1),
                new QuickActionInfo("a3", delClass1.Del3)
            );

            AssertActions("b",
                new QuickActionInfo("a1", delClass2.Del1),
                new QuickActionInfo("a3", delClass2.Del3)
            );

            AssertActions("c",
                new QuickActionInfo("a1", delClass3.Del1),
                new QuickActionInfo("a3", delClass3.Del3)
            );

            UnregisterAction("a1");

            AssertActions("",
                new QuickActionInfo("a3", Del3)
            );

            AssertActions("a",
                new QuickActionInfo("a3", delClass1.Del3)
            );

            AssertActions("b",
                new QuickActionInfo("a3", delClass2.Del3)
            );

            AssertActions("c",
                new QuickActionInfo("a3", delClass3.Del3)
            );

            UnregisterAction("a3");

            AssertActions("");
            AssertActions("a");
            AssertActions("b");
            AssertActions("c");

            UnregisterAction("a3");

            AssertActions("");
            AssertActions("a");
            AssertActions("b");
            AssertActions("c");
        }

        [Test]
        public void TestUnregisterActionByDelegate()
        {
            RegisterAction("b", "a2", Del2);
            RegisterAction("b", "a1", Del1);
            RegisterAction("b", "a3", Del3);

            RegisterAction("a", "a2", Del2);
            RegisterAction("a", "a1", Del1);
            RegisterAction("a", "a3", Del3);

            RegisterAction("a2", Del2);
            RegisterAction("a1", Del1);
            RegisterAction("a3", Del3);

            RegisterAction("c", "a2", Del2);
            RegisterAction("c", "a1", Del1);
            RegisterAction("c", "a3", Del3);

            UnregisterAction(Del2);

            AssertActions("",
                new QuickActionInfo("a1", Del1),
                new QuickActionInfo("a3", Del3)
            );

            AssertActions("a",
                new QuickActionInfo("a1", Del1),
                new QuickActionInfo("a3", Del3)
            );

            AssertActions("b",
                new QuickActionInfo("a1", Del1),
                new QuickActionInfo("a3", Del3)
            );

            AssertActions("c",
                new QuickActionInfo("a1", Del1),
                new QuickActionInfo("a3", Del3)
            );

            UnregisterAction(Del1);

            AssertActions("",
                new QuickActionInfo("a3", Del3)
            );

            AssertActions("a",
                new QuickActionInfo("a3", Del3)
            );

            AssertActions("b",
                new QuickActionInfo("a3", Del3)
            );

            AssertActions("c",
                new QuickActionInfo("a3", Del3)
            );

            UnregisterAction(Del3);

            AssertActions("");
            AssertActions("a");
            AssertActions("b");
            AssertActions("c");

            UnregisterAction(Del3);

            AssertActions("");
            AssertActions("a");
            AssertActions("b");
            AssertActions("c");
        }

        [Test]
        public void TestUnregisterActionByTarget()
        {
            DelClass1 delClass1 = new DelClass1();
            DelClass2 delClass2 = new DelClass2();
            DelClass3 delClass3 = new DelClass3();

            Action del1 = delClass1.Del1;
            Action del2 = delClass2.Del2;
            Action del3 = delClass3.Del3;

            RegisterAction("b", "a2", del2);
            RegisterAction("b", "a1", del1);
            RegisterAction("b", "a3", del3);

            RegisterAction("a", "a2", del2);
            RegisterAction("a", "a1", del1);
            RegisterAction("a", "a3", del3);

            RegisterAction("a2", del2);
            RegisterAction("a1", del1);
            RegisterAction("a3", del3);

            RegisterAction("c", "a2", del2);
            RegisterAction("c", "a1", del1);
            RegisterAction("c", "a3", del3);

            UnregisterAllActions(delClass2);

            AssertActions("",
                new QuickActionInfo("a1", del1),
                new QuickActionInfo("a3", del3)
            );

            AssertActions("a",
                new QuickActionInfo("a1", del1),
                new QuickActionInfo("a3", del3)
            );

            AssertActions("b",
                new QuickActionInfo("a1", del1),
                new QuickActionInfo("a3", del3)
            );

            AssertActions("c",
                new QuickActionInfo("a1", del1),
                new QuickActionInfo("a3", del3)
            );

            UnregisterAllActions(delClass1);

            AssertActions("",
                new QuickActionInfo("a3", del3)
            );

            AssertActions("a",
                new QuickActionInfo("a3", del3)
            );

            AssertActions("b",
                new QuickActionInfo("a3", del3)
            );

            AssertActions("c",
                new QuickActionInfo("a3", del3)
            );

            UnregisterAllActions(delClass3);

            AssertActions("");
            AssertActions("a");
            AssertActions("b");
            AssertActions("c");

            UnregisterAllActions(delClass3);

            AssertActions("");
            AssertActions("a");
            AssertActions("b");
            AssertActions("c");
        }

        #endregion

        #region Lookup actions

        [Test]
        public void TestLookupAction()
        {
            QuickAction a = RegisterAction("a", "a1", Del1);
            QuickAction b = RegisterAction("b", "b1", Del1);
            QuickAction c = RegisterAction("c", "c1", Del1);
            QuickAction d = RegisterAction("",  "d1", Del1);

            QuickAction[] actions = { a, b, c, d };
            for (int i = 0; i < actions.Length - 1; ++i)
            {
                for (int j = i + 1; j < actions.Length; ++j)
                {
                    Assert.AreNotEqual(actions[i].id, actions[j].id);
                }
            }

            Assert.AreSame(a, FindAction(a.id));
            Assert.AreSame(b, FindAction(b.id));
            Assert.AreSame(c, FindAction(c.id));
            Assert.AreSame(d, FindAction(d.id));

            UnregisterAction(a.id);
            Assert.IsNull(FindAction(a.id));
            Assert.AreSame(b, FindAction(b.id));
            Assert.AreSame(c, FindAction(c.id));
            Assert.AreSame(d, FindAction(d.id));
            AssertActions("a");
            AssertActions("b", new QuickActionInfo("b1", Del1));
            AssertActions("c", new QuickActionInfo("c1", Del1));
            AssertActions("", new QuickActionInfo("d1", Del1));

            UnregisterAction(b.id);
            Assert.IsNull(FindAction(a.id));
            Assert.IsNull(FindAction(b.id));
            Assert.AreSame(c, FindAction(c.id));
            Assert.AreSame(d, FindAction(d.id));
            AssertActions("a");
            AssertActions("b");
            AssertActions("c", new QuickActionInfo("c1", Del1));
            AssertActions("", new QuickActionInfo("d1", Del1));

            UnregisterAction(c.id);
            Assert.IsNull(FindAction(a.id));
            Assert.IsNull(FindAction(b.id));
            Assert.IsNull(FindAction(c.id));
            Assert.AreSame(d, FindAction(d.id));
            AssertActions("a");
            AssertActions("b");
            AssertActions("c");
            AssertActions("", new QuickActionInfo("d1", Del1));

            UnregisterAction(d.id);
            Assert.IsNull(FindAction(a.id));
            Assert.IsNull(FindAction(b.id));
            Assert.IsNull(FindAction(c.id));
            Assert.IsNull(FindAction(d.id));
            AssertActions("a");
            AssertActions("b");
            AssertActions("c");
            AssertActions("");
        }

        #endregion

        #region Delegate notifications

        [Test]
        public void TestDelegateNotifications()
        {
            RegisterAction("a", "a1", Del1);
            AssertResult("added: a/a1");

            RegisterAction("b", "b1", Del1);
            AssertResult("added: b/b1");

            RegisterAction("c", "c1", Del1);
            AssertResult("added: c/c1");

            RegisterAction("d1", Del1);
            AssertResult("added: /d1");
        }

        #endregion

        #region IQuickActionRegistryDelegate implementation

        public void OnActionAdded(QuickActionRegistry registry, QuickAction action)
        {
            AddResult("added: " + action.groupName + "/" + action.name);
        }

        public void OnActionChanged(QuickActionRegistry registry, QuickAction action)
        {
            AddResult("changed: " + action.groupName + "/" + action.name);
        }

        public void OnActionRemoved(QuickActionRegistry registry, QuickAction action)
        {
            AddResult("removed: " + action.groupName + "/" + action.name);
        }

        public void OnActionsCleared(QuickActionRegistry registry)
        {
            AddResult("cleared");
        }

        public void OnVariableAdded(QuickActionRegistry registry, CVar cvar)
        {
        }

        #endregion

        #region Helpers

        QuickActionGroup FindGroup(string name)
        {
            return m_actionRegistry.FindActionGroup(name);
        }

        QuickAction FindAction(int id)
        {
            return m_actionRegistry.FindAction(id);
        }

        QuickAction RegisterAction(string name, Action actionDelegate)
        {
            return m_actionRegistry.RegisterAction(name, actionDelegate);
        }

        QuickAction RegisterAction(string groupName, string name, Action actionDelegate)
        {
            return m_actionRegistry.RegisterAction(groupName, name, actionDelegate);
        }

        void UnregisterAction(string name)
        {
            m_actionRegistry.Unregister(name);
        }

        void UnregisterAction(int id)
        {
            m_actionRegistry.Unregister(id);
        }

        void UnregisterAction(Action actionDelegate)
        {
            m_actionRegistry.Unregister(actionDelegate);
        }

        void UnregisterAllActions(object target)
        {
            m_actionRegistry.UnregisterAll(target);
        }

        void AssertGroups(params string[] expected)
        {
            var actionGroups = m_actionRegistry.actionGroups;
            Assert.AreEqual(expected.Length, actionGroups.Count);

            int index = 0;
            foreach (var actionGroup in actionGroups)
            {
                Assert.AreEqual(expected[index++], actionGroup.name);
            }
        }

        void AssertActions(string actionGroupName, params QuickActionInfo[] expected)
        {
            AssertActions(FindGroup(actionGroupName), expected);
        }

        void AssertActions(QuickActionGroup actionGroup, params QuickActionInfo[] expected)
        {
            var actions = actionGroup.actions;
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

        #region Action delegates

        void Del1()
        {
        }

        void Del2()
        {
        }

        void Del3()
        {
        }

        void Del4()
        {
        }

        #endregion

        #region Action classes

        class DelClass1
        {
            public void Del1()
            {
            }

            public void Del2()
            {
            }

            public void Del3()
            {
            }
        }

        class DelClass2
        {
            public void Del1()
            {
            }

            public void Del2()
            {
            }

            public void Del3()
            {
            }
        }

        class DelClass3
        {
            public void Del1()
            {
            }

            public void Del2()
            {
            }

            public void Del3()
            {
            }
        }

        #endregion
    }
}
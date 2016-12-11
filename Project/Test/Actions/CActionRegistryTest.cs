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
    public class CActionRegistryTest : TestFixtureBase, ICActionRegistryDelegate
    {
        CRegistry m_registry;

        #region Setup

        [SetUp]
        public void SetUp()
        {
            RunSetUp();

            m_registry = new CRegistry();
            m_registry.registryDelegate = this;
        }

        #endregion

        #region Register actions

        [Test]
        public void TestRegisterActions()
        {
            RegisterAction("a2", Del2);
            RegisterAction("a1", Del1);
            RegisterAction("a3", Del3);

            AssertActions(
                new CActionInfo("a1", Del1),
                new CActionInfo("a2", Del2),
                new CActionInfo("a3", Del3)
            );
        }

        [Test]
        public void TestRegisterMultipleActionsWithSameName()
        {
            RegisterAction("a2", Del2);
            RegisterAction("a3", Del3);
            RegisterAction("a1", Del1);
            RegisterAction("a3", Del4);

            AssertActions(
                new CActionInfo("a1", Del1),
                new CActionInfo("a2", Del2),
                new CActionInfo("a3", Del4)
            );
        }

        #endregion

        #region Unregister actions

        [Test]
        public void TestUnregisterActionByName()
        {
            RegisterAction("a2", Del2);
            RegisterAction("a1", Del1);
            RegisterAction("a3", Del3);

            UnregisterAction("a2");

            AssertActions(
                new CActionInfo("a1", Del1),
                new CActionInfo("a3", Del3)
            );

            UnregisterAction("a1");

            AssertActions(
                new CActionInfo("a3", Del3)
            );

            UnregisterAction("a3");
            AssertActions();

            UnregisterAction("a1");
            UnregisterAction("a2");
            UnregisterAction("a3");
            AssertActions();
        }

        [Test]
        public void TestUnregisterActionByDelegate()
        {
            RegisterAction("a2", Del2);
            RegisterAction("a1", Del1);
            RegisterAction("a3", Del3);

            UnregisterAction(Del2);

            AssertActions(
                new CActionInfo("a1", Del1),
                new CActionInfo("a3", Del3)
            );

            UnregisterAction(Del1);

            AssertActions(
                new CActionInfo("a3", Del3)
            );

            UnregisterAction(Del3);
            AssertActions();

            UnregisterAction(Del1);
            UnregisterAction(Del2);
            UnregisterAction(Del3);

            AssertActions();
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

            RegisterAction("a2", del2);
            RegisterAction("a1", del1);
            RegisterAction("a3", del3);

            UnregisterAllActions(delClass2);

            AssertActions(
                new CActionInfo("a1", del1),
                new CActionInfo("a3", del3)
            );

            UnregisterAllActions(delClass1);

            AssertActions(
                new CActionInfo("a3", del3)
            );

            UnregisterAllActions(delClass3);

            AssertActions();

            UnregisterAllActions(delClass3);

            AssertActions();
        }

        #endregion

        #region Lookup actions

        [Test]
        public void TestLookupAction()
        {
            CAction a = RegisterAction("a1", Del1);
            CAction b = RegisterAction("b1", Del1);
            CAction c = RegisterAction("c1", Del1);
            CAction d = RegisterAction("d1", Del1);

            CAction[] actions = { a, b, c, d };
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

            UnregisterAction(b.id);
            Assert.IsNull(FindAction(a.id));
            Assert.IsNull(FindAction(b.id));
            Assert.AreSame(c, FindAction(c.id));
            Assert.AreSame(d, FindAction(d.id));

            UnregisterAction(c.id);
            Assert.IsNull(FindAction(a.id));
            Assert.IsNull(FindAction(b.id));
            Assert.IsNull(FindAction(c.id));
            Assert.AreSame(d, FindAction(d.id));

            UnregisterAction(d.id);
            Assert.IsNull(FindAction(a.id));
            Assert.IsNull(FindAction(b.id));
            Assert.IsNull(FindAction(c.id));
            Assert.IsNull(FindAction(d.id));
        }

        #endregion

        #region Delegate notifications

        [Test]
        public void TestDelegateNotifications()
        {
            RegisterAction("a1", Del1);
            AssertResult("added: a1");

            RegisterAction("b1", Del1);
            AssertResult("added: b1");

            RegisterAction("c1", Del1);
            AssertResult("added: c1");

            RegisterAction("d1", Del1);
            AssertResult("added: d1");
        }

        #endregion

        #region IQuickActionRegistryDelegate implementation

        public void OnActionAdded(CRegistry registry, CAction action)
        {
            AddResult("added: " + action.name);
        }

        public void OnActionChanged(CRegistry registry, CAction action)
        {
            AddResult("changed: " + action.name);
        }

        public void OnActionRemoved(CRegistry registry, CAction action)
        {
            AddResult("removed: " + action.name);
        }

        public void OnActionsCleared(CRegistry registry)
        {
            AddResult("cleared");
        }

        public void OnVariableAdded(CRegistry registry, CVar cvar)
        {
        }

        #endregion

        #region Helpers

        CAction FindAction(int id)
        {
            return m_registry.FindAction(id);
        }

        CAction RegisterAction(string name, Action actionDelegate)
        {
            return m_registry.RegisterAction(name, actionDelegate);
        }

        void UnregisterAction(string name)
        {
            m_registry.Unregister(name);
        }

        void UnregisterAction(int id)
        {
            m_registry.Unregister(id);
        }

        void UnregisterAction(Action actionDelegate)
        {
            m_registry.Unregister(actionDelegate);
        }

        void UnregisterAllActions(object target)
        {
            m_registry.UnregisterAll(target);
        }

        void AssertActions(params CActionInfo[] expected)
        {
            int index = 0;
            foreach (var action in m_registry.actions)
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
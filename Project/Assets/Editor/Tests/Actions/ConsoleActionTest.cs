using System;
using LunarConsolePluginInternal;
using NUnit.Framework;

namespace Actions
{
    public class ConsoleActionTest
    {
        [Test]
        public void TestEquality1()
        {
            var action1 = new CAction(id: 0, name: "action", callback: (Action) Action1, requiresConfirmation: false);
            var action2 = new CAction(id: 0, name: "action", callback: (Action) Action1, requiresConfirmation: false);
            Assert.AreEqual(action1, action2);
        }
        
        [Test]
        public void TestNonEquality1()
        {
            var action1 = new CAction(id: 0, name: "action", callback: (Action) Action1, requiresConfirmation: false);
            var action2 = new CAction(id: 1, name: "action", callback: (Action) Action1, requiresConfirmation: false);
            Assert.AreNotEqual(action1, action2);
        }
        
        [Test]
        public void TestNonEquality2()
        {
            var action1 = new CAction(id: 0, name: "action", callback: (Action) Action1, requiresConfirmation: false);
            var action2 = new CAction(id: 0, name: "other action", callback: (Action) Action1, requiresConfirmation: false);
            Assert.AreNotEqual(action1, action2);
        }
        
        [Test]
        public void TestNonEquality3()
        {
            var action1 = new CAction(id: 0, name: "action", callback: (Action) Action1, requiresConfirmation: false);
            var action2 = new CAction(id: 0, name: "action", callback: (Action) Action2, requiresConfirmation: false);
            Assert.AreNotEqual(action1, action2);
        }
        
        [Test]
        public void TestNonEquality4()
        {
            var action1 = new CAction(id: 0, name: "action", callback: (Action) Action1, requiresConfirmation: false);
            var action2 = new CAction(id: 0, name: "action", callback: (Action) Action1, requiresConfirmation: true);
            Assert.AreNotEqual(action1, action2);
        }

        private static void Action1()
        {
        }
        
        private static void Action2()
        {
        }
    }
}
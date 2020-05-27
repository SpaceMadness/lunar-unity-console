using System.Collections;
using System.Collections.Generic;
using LunarConsolePlugin;
using NUnit.Framework;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.TestTools;

namespace Tests
{
    public class PlatformTest
    {
        // A Test behaves as an ordinary method
        [Test]
        public void PlatformTestSimplePasses()
        {
            // Use the Assert class to test conditions
        }

        // A UnityTest behaves like a coroutine in Play Mode. In Edit Mode you can use
        // `yield return null;` to skip a frame.
        [UnityTest]
        public IEnumerator TestOpenClose()
        {
            SceneManager.LoadScene("MainScene", LoadSceneMode.Single);
            
            yield return null;

            int openCount = 0;
            int closeCount = 0;
            
            LunarConsole.onConsoleOpened += () => ++openCount;
            LunarConsole.onConsoleClosed += () => ++closeCount;
            
            Assert.AreEqual(0, openCount);
            Assert.AreEqual(0, closeCount);
            
            LunarConsole.Show();
            Assert.AreEqual(1, openCount);
            
            LunarConsole.Show();
            Assert.AreEqual(1, openCount);
            
            LunarConsole.Hide();
            Assert.AreEqual(1, closeCount);
            
            LunarConsole.Hide();
            Assert.AreEqual(1, closeCount);
        }
    }
}

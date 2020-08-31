//
//  PlatformTest.cs
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


using System.Collections;
using LunarConsolePlugin;
using NUnit.Framework;
using UnityEngine.SceneManagement;
using UnityEngine.TestTools;

namespace Tests
{
    public class PlatformTest
    {
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

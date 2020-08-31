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


using System;
using System.Collections;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using LunarConsolePlugin;
using LunarConsolePluginInternal;
using NUnit.Framework;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.TestTools;
using Object = UnityEngine.Object;

namespace Tests
{
    public class ActionsTest
    {
        [UnityTest]
        public IEnumerator TestActions()
        {
            yield return SceneManager.LoadSceneAsync("Tests/Scenes/TestScene");

            AssertActions();
            
            var go = new GameObject();
            var behaviour = go.AddComponent<TestBehaviour>();
            
            yield return null;

            // actions should be registered as the component becomes enabled
            var actionId = 0;
            var expected = new List<CAction>();
            expected.Add(new CAction(id: actionId++, name: "Default Action", callback: (Action) behaviour.DefaultAction, requiresConfirmation: false));
            expected.Add(new CAction(id: actionId++, name: "My Action", callback: (Action) behaviour.CustomDisplayNameAction, requiresConfirmation: false));
            if (Application.platform == RuntimePlatform.Android)
            {
                expected.Add(new CAction(id: actionId++, name: "Android Action", callback: (Action) TestBehaviour.AndroidAction, requiresConfirmation: false));
            }
            if (Application.platform == RuntimePlatform.IPhonePlayer)
            {
                expected.Add(new CAction(id: actionId++, name: "IOS Action", callback: (Action) TestBehaviour.IOSAction, requiresConfirmation: false));
            }
            if (Application.isEditor)
            {
                expected.Add(new CAction(id: actionId++, name: "Editor Action", callback: (Action) TestBehaviour.EditorAction, requiresConfirmation: false));
            }
            if (Application.platform == RuntimePlatform.Android || Application.isEditor)
            {
                expected.Add(new CAction(id: actionId++, name: "Android And Editor Action", callback: (Action) TestBehaviour.AndroidAndEditorAction, requiresConfirmation: false));
            }
            if (Application.platform == RuntimePlatform.IPhonePlayer || Application.isEditor)
            {
                expected.Add(new CAction(id: actionId++, name: "iOS & Editor Action", callback: (Action) TestBehaviour.IOSAndEditorAction, requiresConfirmation: false));
            }
            expected.Add(new CAction(id: actionId++, name: "Requires Confirmation Action", callback: (Action) TestBehaviour.RequiresConfirmationAction, requiresConfirmation: true));

            AssertActions(expected);
            
            yield return null;

            // actions should be unregistered as the component becomes disabled
            
            behaviour.enabled = false;
            
            AssertActions();
            
            yield return null;
            
            // actions should be registered as the component becomes enabled

            behaviour.enabled = true;
            
            AssertActions(expected);
            
            yield return null;

            // actions should be registered as object is destroyed
            
            Object.Destroy(go);
            
            yield return null;
            
            AssertActions();
        }

        private static void AssertActions(IList<CAction> actions = null)
        {
            var expected = actions ?? new List<CAction>(0); 
            var actual = LunarConsole.instance.registry.actions.ToList();
            Assert.AreEqual(expected, actual);
        }
    }

    public class TestBehaviour : MonoBehaviour
    {
        private void OnEnable()
        {
            LunarConsole.Register(this);
        }

        [ConsoleAction]
        public void DefaultAction()
        {
        }
        
        [ConsoleAction(DisplayName = "My Action")]
        public void CustomDisplayNameAction()
        {
        }
        
        [ConsoleAction(Platform = TargetPlatform.Android)]
        public static void AndroidAction()
        {
        }
        
        [ConsoleAction(Platform = TargetPlatform.IOS)]
        public static void IOSAction()
        {
        }
        
        [ConsoleAction(Platform = TargetPlatform.Editor)]
        public static void EditorAction()
        {
        }
        
        [ConsoleAction(Platform = TargetPlatform.Android | TargetPlatform.Editor)]
        public static void AndroidAndEditorAction()
        {
        }
        
        [ConsoleAction(Platform = TargetPlatform.IOS | TargetPlatform.Editor, DisplayName = "iOS & Editor Action")]
        public static void IOSAndEditorAction()
        {
        }
        
        [ConsoleAction(RequiresConfirmation = true)]
        public static void RequiresConfirmationAction()
        {
        }
        
        [ConsoleAction(Platform = TargetPlatform.None)]
        public static void NoPlatformAction()
        {
        }
    }
}

//
//  StackTraceTest.java
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

package spacemadness.com.lunarconsole.utils;

import junit.framework.TestCase;

public class StackTraceTest extends TestCase
{
    public void testOptimize() throws Exception
    {
        String stackTrace = "UnityEngine.Debug:LogError(Object)\n" +
                "Test:Method(String) (at /Users/lunar-unity-console/Project/Assets/Scripts/Test.cs:30)\n" +
                "<LogMessages>c__Iterator0:MoveNext() (at /Users/lunar-unity-console/Project/Assets/Logger.cs:85)\n" +
                "UnityEngine.MonoBehaviour:StartCoroutine(IEnumerator)\n" +
                "Logger:LogMessages() (at /Users/lunar-unity-console/Project/Assets/Logger.cs:66)\n" +
                "UnityEngine.EventSystems.EventSystem:Update()";

        String expected = "UnityEngine.Debug:LogError(Object)\n" +
                "Test:Method(String) (at Assets/Scripts/Test.cs:30)\n" +
                "<LogMessages>c__Iterator0:MoveNext() (at Assets/Logger.cs:85)\n" +
                "UnityEngine.MonoBehaviour:StartCoroutine(IEnumerator)\n" +
                "Logger:LogMessages() (at Assets/Logger.cs:66)\n" +
                "UnityEngine.EventSystems.EventSystem:Update()";

        String actual = StackTrace.optimize(stackTrace);
        assertEquals(expected, actual);
    }
}
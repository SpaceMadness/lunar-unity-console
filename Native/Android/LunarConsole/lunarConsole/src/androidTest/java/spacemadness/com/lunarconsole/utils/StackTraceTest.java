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
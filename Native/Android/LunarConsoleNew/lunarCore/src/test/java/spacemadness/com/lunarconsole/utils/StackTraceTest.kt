package spacemadness.com.lunarconsole.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class StackTraceTest {
    @Test
    fun optimize() {
        val stackTrace = "UnityEngine.Debug:LogError(Object)\n" +
                "Test:Method(String) (at /Users/lunar-unity-console/Project/Assets/Scripts/Test.cs:30)\n" +
                "<LogMessages>c__Iterator0:MoveNext() (at /Users/lunar-unity-console/Project/Assets/Logger.cs:85)\n" +
                "UnityEngine.MonoBehaviour:StartCoroutine(IEnumerator)\n" +
                "Logger:LogMessages() (at /Users/lunar-unity-console/Project/Assets/Logger.cs:66)\n" +
                "UnityEngine.EventSystems.EventSystem:Update()"

        val expected = "UnityEngine.Debug:LogError(Object)\n" +
                "Test:Method(String) (at Assets/Scripts/Test.cs:30)\n" +
                "<LogMessages>c__Iterator0:MoveNext() (at Assets/Logger.cs:85)\n" +
                "UnityEngine.MonoBehaviour:StartCoroutine(IEnumerator)\n" +
                "Logger:LogMessages() (at Assets/Logger.cs:66)\n" +
                "UnityEngine.EventSystems.EventSystem:Update()"

        val actual = StackTrace.optimize(stackTrace)
        assertEquals(expected, actual)
    }
}
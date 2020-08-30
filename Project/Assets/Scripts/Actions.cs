using LunarConsolePlugin;
using UnityEngine;

public class Actions : MonoBehaviour
{
    [ConsoleAction]
    private void TestAction()
    {
        Debug.Log("Test action");
    }
    
    [ConsoleAction]
    private void TestStaticAction()
    {
        Debug.Log("Test static action");
    }
}

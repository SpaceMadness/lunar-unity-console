using UnityEngine;

using System;
using System.Collections;

public class Logger : MonoBehaviour
{
    public void LogDebug()
    {
        Debug.Log("Log debug");
    }

    public void LogWarning()
    {
        Debug.Log("Log warning");
    }

    public void LogError()
    {
        Debug.Log("Log error");
    }

    public void ThrowException()
    {
        throw new Exception("Error");
    }
}

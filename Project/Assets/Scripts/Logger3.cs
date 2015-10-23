using System;
using UnityEngine;

public static class Logger3
{
    public static void LogDebug(string message)
    {
        Debug.Log(message);
    }
    
    public static void LogWarning(string message)
    {
        Debug.LogWarning(message);
    }
    
    public static void LogError(string message)
    {
        Debug.LogError(message);
    }
    
    public static void ThrowException(string message)
    {
        throw new Exception(message);
    }
}

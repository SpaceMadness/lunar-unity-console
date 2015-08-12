using UnityEngine;

using System;
using System.Collections;

public class Logger : MonoBehaviour
{
    public void LogLines()
    {
        StartCoroutine(LogLines(10));
    }

    public void LogError()
    {
        throw new Exception("Error!");
    }

    IEnumerator LogLines(int count)
    {
        int index = 0;
        while (true)
        {
            print(index + ": java.lang.RuntimeException: Can't create handler inside thread that has not called Looper.prepare()");
            ++index;

            yield return new WaitForSeconds(0.1f);
        }
    }

}

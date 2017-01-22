using System.Collections;
using System.Collections.Generic;
using UnityEngine;

using LunarConsolePlugin;

public class TestActions : MonoBehaviour
{
    // Use this for initialization
    void Start()
    {
        LunarConsole.RegisterAction("Set Red Color", delegate() {
            StartCoroutine(SetColor(Color.red));
        });

        LunarConsole.RegisterAction("Set Green Color", delegate() {
            StartCoroutine(SetColor(Color.green));
        });

        LunarConsole.RegisterAction("Set Blue Color", delegate() {
            StartCoroutine(SetColor(Color.blue));
        });
    }

    void OnDestroy()
    {
        LunarConsole.UnregisterAllActions(this);
    }

    IEnumerator SetColor(Color color)
    {
        var currentColor = Camera.main.backgroundColor;
        float duration = 0.5f;
        float elapsed = 0.0f;

        while (elapsed < duration)
        {
            Camera.main.backgroundColor = Color.Lerp(currentColor, color, elapsed / duration);

            elapsed += Time.deltaTime;
            yield return null;
        }

        Camera.main.backgroundColor = color;
    }
}

//
//  LogScene.cs
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

﻿using System;
using System.Collections;
using System.Collections.Generic;
using System.Threading;

using UnityEngine;
using Random = System.Random;

public class LogScene : MonoBehaviour
{
    private static readonly string[] messages = {
        "V: We are <b>not</b> amused.",
        "V: We are <b><i>definitely not</i></b> amused",
        "V: We are <b>absolutely <i>definitely</i> not</b> amused",
        "V: We are <color=green>green</color> with envy",
    };

    [SerializeField]
    private float m_logDelay = 0.25f;

    [SerializeField]
    private UnityEngine.UI.Text m_logTitle;

    [SerializeField]
    private UnityEngine.UI.Text m_logOnThreadTitle;

    private Thread m_logThread;
    private Random m_random;
    private bool m_logActive;

    void Awake()
    {
        m_random = new Random();
    }

    void OnDestroy()
    {
        StopLogThread();
    }

    #region Start Log

    public void ToggleLog()
    {
        if (!m_logActive)
        {
            m_logTitle.text = "Stop Log";
            StartLog();
        }
        else
        {
            m_logTitle.text = "Start Log";
            StopLog();
        }
    }

    public void StartLog()
    {
        m_logActive = true;
        StartCoroutine(LogMessages(messages));
    }

    public void StopLog()
    {
        m_logActive = false;
    }

    IEnumerator LogMessages(string[] messages)
    {
        while (m_logActive)
        {
            LogRandomMessage(messages);
            yield return new WaitForSeconds(m_logDelay);
        }
    }

    void LogRandomMessage(string[] messages)
    {
        var line = messages[m_random.Next(messages.Length)];
        var level = line.Substring(0, 1);
        var message = line.Substring(2);
        if (level == "E")
        {
            Debug.LogError(message);
        }
        else
            if (level == "W")
            {
                Debug.LogWarning(message);
            }
            else
            {
                Debug.Log(message);
            }
    }

    #endregion

    #region Fine Logs

    public void Log()
    {
        Debug.Log("Log message");
    }

    public void Warning()
    {
        Debug.LogWarning("Warning message");
    }

    public void Error()
    {
        Debug.LogError("Error message");
    }

    public void ThrowException()
    {
        Method1();
    }

    void Method1()
    {
        Method2();
    }

    void Method2()
    {
        throw new Exception("Test exception");
    }

    #endregion

    #region Log on Thread

    public void ToggleLogOnThread()
    {
        if (m_logThread != null)
        {
            m_logOnThreadTitle.text = "Start Log Thread";
            StopLogThread();
        }
        else
        {
            m_logOnThreadTitle.text = "Stop Log Thread";
            StartLogThread();
        }
    }

    private void StartLogThread()
    {
        StopLogThread();

        m_logThread = new Thread(delegate()
        {
            try
            {
                while (true)
                {
                    LogRandomMessage(messages);
                    Thread.Sleep((int) (1000 * m_logDelay));
                }
            }
            catch (ThreadInterruptedException)
            {
            }
        });
        m_logThread.Start();
    }

    private void StopLogThread()
    {
        if (m_logThread != null)
        {
            m_logThread.Interrupt();
            m_logThread.Join();
            m_logThread = null;
        }
    }

    #endregion
}

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
        "W WifiMode: , Invalid SupportedRates!!!",
        "W WifiMode: , Invalid SupportedRates!!!",
        "W ctxmgr  : [AclManager]No 2 for (accnt=account#1740201043#, com.google.android.gms(10013):UserLocationProducer, vrsn=10298000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#1740201043#",
        "I art     : Background partial concurrent mark sweep GC freed 87018(2MB) AllocSpace objects, 0(0B) LOS objects, 40% free, 4MB/7MB, paused 185us total 136.531ms",
        "I art     : Background partial concurrent mark sweep GC freed 49471(3MB) AllocSpace objects, 0(0B) LOS objects, 33% free, 30MB/45MB, paused 1.554ms total 113.167ms",
        "I art     : Background partial concurrent mark sweep GC freed 1152(46KB) AllocSpace objects, 0(0B) LOS objects, 33% free, 30MB/45MB, paused 1.057ms total 107.601ms",
        "E BatteryStatsService: no controller energy info supplied",
        "I nanohub : queueFlush: sensor=30, handle= 3776 W WifiMode: , Invalid SupportedRates!!!",
        "W WifiMode: , Invalid SupportedRates!!!",
        "W WifiMode: , Invalid SupportedRates!!!",
        "E Spotify : [main@gxx$2:355] The application has been idle too long, stopping service",
        "I CastMediaRouteProvider: in onDiscoveryRequestChanged: request=DiscoveryRequest{ selector=MediaRouteSelector{ controlCategories=[com.google.android.gms.cast.CATEGORY_CAST//urn:x-cast:com.google.cast.media] }, activeScan=false, isValid=true }",
        "I DeviceScanner: [MDNS] Filter criteria: %urn:x-cast:com.google.cast.media",
        "I DeviceScanner: [MDNS] updateScannerState: No filter criteria was added.",
        "I DeviceScanner: [MDNS] updateScannerState: criteriaRemoved: CC32E19983 W MdnsClient_Cast: Multicast lock held. Releasing. Subtypes:\"%9E5E7C8F47989526C9BCD95D24084F6F0B27C5ED,CC32E753\"",
        "V MediaRouter: Dispatching route change: UserRouteInfo{ name=null, description=null, status=null, category=RouteCategory{ name=Devices types=ROUTE_TYPE_USER  groupable=false }, supportedTypes=ROUTE_TYPE_USER , presentationDisplay=null }",
        "V MediaRouter: Dispatching route change: UserRouteInfo{ name=Nearby device, description=null, status=null, category=RouteCategory{ name=Devices types=ROUTE_TYPE_USER  groupable=false }, supportedTypes=ROUTE_TYPE_USER , presentationDisplay=null }",
        "V MediaRouter: Dispatching route change: UserRouteInfo{ name=Nearby device, description=Google Cast, status=null, category=RouteCategory{ name=Devices types=ROUTE_TYPE_USER  groupable=false }, supportedTypes=ROUTE_TYPE_USER , presentationDisplay=null }",
        "V MediaRouter: Adding route: UserRouteInfo{ name=Nearby device, description=Google Cast, status=null, category=RouteCategory{ name=Devices types=ROUTE_TYPE_USER  groupable=false }, supportedTypes=ROUTE_TYPE_USER , presentationDisplay=null }",
        "V MediaRouter: Dispatching route change: UserRouteInfo{ name=Alex's Chromecast, description=Chromecast, status=null, category=RouteCategory{ name=Devices types=ROUTE_TYPE_USER  groupable=false }, supportedTypes=ROUTE_TYPE_USER , presentationDisplay=null }",
        "V MediaRouter: Removing route: UserRouteInfo{ name=Alex's Chromecast, description=Chromecast, status=null, category=RouteCategory{ name=Devices types=ROUTE_TYPE_USER  groupable=false }, supportedTypes=ROUTE_TYPE_USER , presentationDisplay=null }",
        "W MdnsClient: unicast receiver thread is already dead.",
        "W MdnsClient_Cast: #acquireLock. Multicast lock not held. Acquiring. Subtypes:\"%9E5E7C8F47989526C9BCD95D24084F6F0B27C5ED\"",
        "I DeviceScanner: [CastNearby] Filter criteria: %urn:x-cast:com.google.cast.media",
        "I DeviceScanner: [CastNearby] updateScannerState: No filter criteria was added.",
        "I DeviceScanner: [CastNearby] updateScannerState: criteriaRemoved: CC32E30195 V MediaRouter: Dispatching route change: UserRouteInfo{ name=Nearby device, description=Google Cast, status=null, category=RouteCategory{ name=Devices types=ROUTE_TYPE_USER  groupable=false }, supportedTypes=ROUTE_TYPE_USER , presentationDisplay=null }",
        "V MediaRouter: Removing route: UserRouteInfo{ name=Nearby device, description=Google Cast, status=null, category=RouteCategory{ name=Devices types=ROUTE_TYPE_USER  groupable=false }, supportedTypes=ROUTE_TYPE_USER , presentationDisplay=null }",
        "V MediaRouter: Dispatching route change: UserRouteInfo{ name=Alex's Chromecast, description=Chromecast, status=null, category=RouteCategory{ name=Devices types=ROUTE_TYPE_USER  groupable=false }, supportedTypes=ROUTE_TYPE_USER , presentationDisplay=null }",
        "V MediaRouter: Removing route: UserRouteInfo{ name=Alex's Chromecast, description=Chromecast, status=null, category=RouteCategory{ name=Devices types=ROUTE_TYPE_USER  groupable=false }, supportedTypes=ROUTE_TYPE_USER , presentationDisplay=null }",
        "E Spotify : [main@service.SpotifyService:1102] Destroying service",
        "I MediaFocusControl:  AudioFocus  abandonAudioFocus() from uid/pid 10119/1133 clientId=android.media.AudioManager@ca9d784ewj$2@8132b6d",
        "V Avrcp   : Active sessions changed, 0 sessions",
        "D AudioTrack: Client defaulted notificationFrames to 14700 for frameCount 30195 V MediaRouter: Dispatching route change: UserRouteInfo{ name=null, description=null, status=null, category=RouteCategory{ name=Devices types=ROUTE_TYPE_USER  groupable=false }, supportedTypes=ROUTE_TYPE_USER , presentationDisplay=null }",
        "V MediaRouter: Dispatching route change: UserRouteInfo{ name=Alex's Chromecast, description=null, status=null, category=RouteCategory{ name=Devices types=ROUTE_TYPE_USER  groupable=false }, supportedTypes=ROUTE_TYPE_USER , presentationDisplay=null }",
        "V MediaRouter: Dispatching route change: UserRouteInfo{ name=Alex's Chromecast, description=Chromecast, status=null, category=RouteCategory{ name=Devices types=ROUTE_TYPE_USER  groupable=false }, supportedTypes=ROUTE_TYPE_USER , presentationDisplay=null }",
        "V MediaRouter: Adding route: UserRouteInfo{ name=Alex's Chromecast, description=Chromecast, status=null, category=RouteCategory{ name=Devices types=ROUTE_TYPE_USER  groupable=false }, supportedTypes=ROUTE_TYPE_USER , presentationDisplay=null }",
        "D AudioTrack: Client defaulted notificationFrames to 14700 for frameCount  1133 E Spotify : [main@service.SpotifyService:34068] orbit stopped",
        "I ActivityManager: Killing 1059:com.google.android.apps.gcs/u0a12 (adj 906): empty # 1079 D ActivityManager: cleanUpApplicationRecord -- 27094 W ctxmgr  : [AclManager]No 2 for (accnt=account#1740201043#, com.google.android.gms(10013):UserLocationProducer, vrsn=10298000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#1740201043#",
        "I ProcessStatsService: Prepared write state in 20ms",
        "I ProcessStatsService: Pruning old procstats: /data/system/procstats/state-2017-03-06-11-16-24.bin",
        "I nanohub : queueFlush: sensor=30, handle= 3776 W WifiMode: , Invalid SupportedRates!!!",
        "W WifiMode: , Invalid SupportedRates!!!",
        "W WifiMode: , Invalid SupportedRates!!!",
        "W WifiMode: , Invalid SupportedRates!!!",
        "W WifiMode: , Invalid SupportedRates!!!",
        "W WifiMode: , Invalid SupportedRates!!!"
    };

    [SerializeField]
    private float m_logDelay = 0.25f;

    private Thread m_logThread;
    private Random m_random;

    void Awake()
    {
        m_random = new Random();
    }

    void OnDestroy()
    {
        StopLogThread();
    }

    #region Start Log

    public void StartLog()
    {
        StartCoroutine(LogMessages(messages));
    }

    IEnumerator LogMessages(string[] messages)
    {
        while (true)
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

    #region Throw Exception

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

    public void LogOnThread()
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

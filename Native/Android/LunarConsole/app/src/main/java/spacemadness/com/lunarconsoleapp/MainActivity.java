//
//  MainActivity.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2015 Alex Lementuev, SpaceMadness.
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

package spacemadness.com.lunarconsoleapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import spacemadness.com.lunarconsole.console.ConsolePlugin;

import static spacemadness.com.lunarconsole.console.ConsoleLogType.*;

public class MainActivity extends AppCompatActivity
{
    private static final String KEY_TEXT_DELAY = "delay";
    private static final String KEY_TEXT_CAPACITY = "capacity";
    private static final String KEY_CHECKBOX_USE_MAIN_THREAD = "use_main_thread";

    private static final String[] MESSAGES = {
        "I/ActivityManager(  748): Start proc 9996:com.google.android.deskclock/u0a33 for broadcast com.google.android.deskclock/com.android.alarmclock.DigitalAppWidgetProvider",
        "W/ProcessCpuTracker(  748): Skipping unknown process pid 10026",
        "W/ProcessCpuTracker(  748): Skipping unknown process pid 10027",
        "W/ProcessCpuTracker(  748): Skipping unknown process pid 10030",
        "I/ActivityManager(  748): Killing 8819:android.process.acore/u0a3 (adj 15): empty #17",
        "I/PowerManagerService(  748): Waking up from sleep (uid 1000)...",
        "I/DisplayPowerController(  748): Blocking screen on until initial contents have been drawn.",
        "I/DisplayPowerController(  748): Unblocked screen on after 152 ms",
        "I/DisplayManagerService(  748): Display device changed: DisplayDeviceInfo{\"Built-in Screen\": uniqueId=\"local:0\", 1080 x 1920, 60.0 fps, supportedRefreshRates [60.0], density 480, 442.451 x 443.345 dpi, appVsyncOff 7500000, presDeadline 12666667, touch INTERNAL, rotation 0, type BUILT_IN, state ON, FLAG_DEFAULT_DISPLAY, FLAG_ROTATES_WITH_CONTENT, FLAG_SECURE, FLAG_SUPPORTS_PROTECTED_BUFFERS}",
        "V/ActivityManager(  748): Display changed displayId=0",
        "D/WifiService(  748): acquireWifiLockLocked: WifiLock{NlpWifiLock type=2 binder=android.os.BinderProxy1fa4438}",
        "D/WifiService(  748): releaseWifiLockLocked: WifiLock{NlpWifiLock type=2 binder=android.os.BinderProxy1fa4438}",
        "D/ConnectivityService(  748): reportBadNetwork(NetworkAgentInfo [WIFI () - 123]) by 10007",
        "D/ConnectivityService(  748): setProvNotificationVisibleIntent: E visible=false networkType=0 extraInfo=null",
        "D/ConnectivityService(  748): Validated NetworkAgentInfo [WIFI () - 123]",
        "D/ConnectivityService(  748): updateNetworkScore for NetworkAgentInfo [WIFI () - 123] to 56",
        "D/ConnectivityService(  748): rematching NetworkAgentInfo [WIFI () - 123]",
        "D/ConnectivityService(  748): Network NetworkAgentInfo [WIFI () - 123] was already satisfying request 1. No change.",
        "D/ConnectivityService(  748): notifyType AVAILABLE for NetworkAgentInfo [WIFI () - 123]",
        "D/ConnectivityService(  748): updateNetworkScore for NetworkAgentInfo [WIFI () - 123] to 60",
        "D/ConnectivityService(  748): rematching NetworkAgentInfo [WIFI () - 123]",
        "D/ConnectivityService(  748): Network NetworkAgentInfo [WIFI () - 123] was already satisfying request 1. No change.",
        "D/ConnectivityService(  748): notifyType AVAILABLE for NetworkAgentInfo [WIFI () - 123]",
        "I/PowerManagerService(  748): Going to sleep due to screen timeout (uid 1000)...",
        "I/PowerManagerService(  748): Sleeping (uid 1000)...",
        "I/DisplayManagerService(  748): Display device changed: DisplayDeviceInfo{\"Built-in Screen\": uniqueId=\"local:0\", 1080 x 1920, 60.0 fps, supportedRefreshRates [60.0], density 480, 442.451 x 443.345 dpi, appVsyncOff 7500000, presDeadline 12666667, touch INTERNAL, rotation 0, type BUILT_IN, state OFF, FLAG_DEFAULT_DISPLAY, FLAG_ROTATES_WITH_CONTENT, FLAG_SECURE, FLAG_SUPPORTS_PROTECTED_BUFFERS}",
        "V/ActivityManager(  748): Display changed displayId=0",
        "D/WifiService(  748): acquireWifiLockLocked: WifiLock{WifiScanner type=2 binder=android.os.BinderProxyf8da662}",
        "D/ConnectivityService(  748): requestNetwork for NetworkRequest [ id=7, legacyType=3, [ Transports: CELLULAR Capabilities: SUPL&NOT_RESTRICTED&TRUSTED&NOT_VPN] ]",
        "D/ConnectivityService(  748): handleRegisterNetworkRequest checking NetworkAgentInfo [WIFI () - 123]",
        "D/ConnectivityService(  748): sending new NetworkRequest to factories",
        "D/ConnectivityService(  748): setProvNotificationVisible: E visible=false networkType=0 action=com.android.internal.telephony.PROVISION0",
        "D/ConnectivityService(  748): setProvNotificationVisibleIntent: E visible=false networkType=0 extraInfo=null",
        "D/ConnectivityService(  748): registerNetworkAgent NetworkAgentInfo{ ni{[type: MOBILE[HSPA], state: CONNECTED/CONNECTED, reason: connected, extra: phone, roaming: false, failover: false, isAvailable: true, isConnectedToProvisioningNetwork: false]}  network{null}  lp{{InterfaceName: rmnet0 LinkAddresses: [10.9.7.7/28,]  Routes: [0.0.0.0/0 -> 10.9.7.8 rmnet0,] DnsAddresses: [172.26.38.1,172.26.38.2,] Domains: null MTU: 1410 TcpBufferSizes: 4094,87380,704512,4096,16384,110208}}  nc{[ Transports: CELLULAR Capabilities: MMS&SUPL&FOTA&INTERNET&NOT_RESTRICTED&TRUSTED&NOT_VPN LinkUpBandwidth>=5898Kbps LinkDnBandwidth>=14336Kbps Specifier: <1>]}  Score{10}  everValidated{false}  lastValidated{false}  created{false}  explicitlySelected{false} }",
        "D/ConnectivityService(  748): NetworkAgentInfo [MOBILE (HSPA) - 124] EVENT_NETWORK_INFO_CHANGED, going from null to CONNECTED",
        "D/ConnectivityService(  748): Adding iface rmnet0 to network 124",
        "D/ConnectivityService(  748): Setting MTU size: rmnet0, 1410",
        "D/ConnectivityService(  748): Adding Route [0.0.0.0/0 -> 10.9.7.8 rmnet0] to network 124",
        "D/ConnectivityService(  748): Setting Dns servers for network 124 to [/172.26.38.1, /172.26.38.2]",
        "D/ConnectivityService(  748): notifyType IP_CHANGED for NetworkAgentInfo [MOBILE (HSPA) - 124]",
        "D/ConnectivityService(  748): notifyType PRECHECK for NetworkAgentInfo [MOBILE (HSPA) - 124]",
        "D/ConnectivityService(  748): rematching NetworkAgentInfo [MOBILE (HSPA) - 124]",
        "D/ConnectivityService(  748):    accepting network in place of null",
        "D/ConnectivityService(  748): notifyType AVAILABLE for NetworkAgentInfo [MOBILE (HSPA) - 124]",
        "D/CSLegacyTypeTracker(  748): Sending connected broadcast for type 3 NetworkAgentInfo [MOBILE (HSPA) - 124] isDefaultNetwork=false",
        "D/ConnectivityService(  748): sendStickyBroadcast: action=android.net.conn.CONNECTIVITY_CHANGE_IMMEDIATE",
        "D/ConnectivityService(  748): sendStickyBroadcast: action=android.net.conn.CONNECTIVITY_CHANGE",
        "D/ConnectivityService(  748): Adding ::/128 -> :: rmnet0 for interface rmnet0",
        "D/ConnectivityService(  748): requestRouteToHostAddress ok=true",
        "D/ConnectivityService(  748): setProvNotificationVisibleIntent: E visible=false networkType=0 extraInfo=null",
        "D/ConnectivityService(  748): Validated NetworkAgentInfo [MOBILE (HSPA) - 124]",
        "D/ConnectivityService(  748): rematching NetworkAgentInfo [MOBILE (HSPA) - 124]",
        "D/ConnectivityService(  748): Network NetworkAgentInfo [MOBILE (HSPA) - 124] was already satisfying request 7. No change.",
        "D/ConnectivityService(  748): notifyType AVAILABLE for NetworkAgentInfo [MOBILE (HSPA) - 124]",
        "E/ConnectivityService(  748): Attempting to register duplicate agent for type 3: NetworkAgentInfo{ ni{[type: MOBILE[HSPA], state: CONNECTED/CONNECTED, reason: connected, extra: phone, roaming: false, failover: false, isAvailable: true, isConnectedToProvisioningNetwork: false]}  network{124}  lp{{InterfaceName: rmnet0 LinkAddresses: [10.9.7.7/28,]  Routes: [0.0.0.0/0 -> 10.9.7.8 rmnet0,] DnsAddresses: [172.26.38.1,172.26.38.2,] Domains: null MTU: 1410 TcpBufferSizes: 4094,87380,704512,4096,16384,110208}}  nc{[ Transports: CELLULAR Capabilities: MMS&SUPL&FOTA&INTERNET&NOT_RESTRICTED&TRUSTED&NOT_VPN LinkUpBandwidth>=5898Kbps LinkDnBandwidth>=14336Kbps Specifier: <1>]}  Score{50}  everValidated{true}  lastValidated{true}  created{true}  explicitlySelected{false} }"
    };

    private Thread loggerThread;
    private int logIndex;

    private EditText delayEditText;
    private EditText capacityEditText;
    private CheckBox useMainThreadCheckBox;

    private DispatchQueue mainQueue;
    private DispatchQueue backgroundQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainQueue = new DispatchQueue(Looper.getMainLooper());
        backgroundQueue = BackgroundDispatchQueue.create("Background");

        delayEditText = (EditText) findViewById(R.id.edit_text_delay);
        capacityEditText = (EditText) findViewById(R.id.edit_text_capacity);
        useMainThreadCheckBox = (CheckBox) findViewById(R.id.checkbox_use_main_thread);

        restoreUIState();

        final int capacity = Integer.parseInt(capacityEditText.getText().toString());

        dispatchOnSelectedQueue(new Runnable()
        {
            @Override
            public void run()
            {
                ConsolePlugin.init(MainActivity.this, "0.0.0b", capacity);
            }
        });

        final Button loggerButton = (Button) findViewById(R.id.button_start_logger);
        loggerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (loggerThread == null)
                {
                    loggerThread = new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            long delay = Integer.parseInt(delayEditText.getText().toString());

                            while (!Thread.currentThread().isInterrupted())
                            {
                                final String message = MESSAGES[logIndex];
                                logIndex = (logIndex + 1) % MESSAGES.length;

                                final int type = getLogType(message);

                                dispatchOnSelectedQueue(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        ConsolePlugin.logMessage(message, "", type);
                                    }
                                });

                                try
                                {
                                    Thread.sleep(delay);
                                }
                                catch (InterruptedException e)
                                {
                                    break;
                                }
                            }
                        }

                        private int getLogType(String message)
                        {
                            if (message.startsWith("W/")) return WARNING;
                            if (message.startsWith("E/")) return WARNING;

                            return LOG;
                        }
                    });
                    loggerThread.start();
                    loggerButton.setText(R.string.button_logger_stop);
                }
                else
                {
                    loggerThread.interrupt();
                    loggerThread = null;
                    loggerButton.setText(R.string.button_logger_start);
                }
            }
        });

        Button errorButton = (Button) findViewById(R.id.button_log_exception);
        errorButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dispatchOnSelectedQueue(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ConsolePlugin.logMessage("Error", "", EXCEPTION);
                    }
                });
            }
        });

        Button showConsole = (Button) findViewById(R.id.button_show_console);
        showConsole.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dispatchOnSelectedQueue(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ConsolePlugin.show();
                    }
                });
            }
        });
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        saveUIState();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (loggerThread != null)
        {
            loggerThread.interrupt();
            loggerThread = null;
        }

        dispatchOnSelectedQueue(new Runnable()
        {
            @Override
            public void run()
            {
                ConsolePlugin.shutdown();
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Threading

    private void dispatchOnSelectedQueue(Runnable r)
    {
        DispatchQueue queue = shouldUseMainThread() ? mainQueue : backgroundQueue;
        queue.dispatch(r);
    }

    private boolean shouldUseMainThread()
    {
        return useMainThreadCheckBox.isChecked();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // UI state

    private void saveUIState()
    {
        SharedPreferences prefs = getSharedPreferences();
        SharedPreferences.Editor editor = prefs.edit();
        saveState(editor, KEY_TEXT_CAPACITY, capacityEditText);
        saveState(editor, KEY_TEXT_DELAY, delayEditText);
        saveState(editor, KEY_CHECKBOX_USE_MAIN_THREAD, useMainThreadCheckBox);
        editor.apply();
    }

    private void restoreUIState()
    {
        SharedPreferences prefs = getSharedPreferences();

        loadState(prefs, KEY_TEXT_CAPACITY, capacityEditText);
        loadState(prefs, KEY_TEXT_DELAY, delayEditText);
        loadState(prefs, KEY_CHECKBOX_USE_MAIN_THREAD, useMainThreadCheckBox);
    }

    private void saveState(SharedPreferences.Editor editor, String key, EditText editText)
    {
        editor.putString(key, editText.getText().toString());
    }

    private void saveState(SharedPreferences.Editor editor, String key, CheckBox checkBox)
    {
        editor.putBoolean(key, checkBox.isChecked());
    }

    private void loadState(SharedPreferences prefs, String key, EditText editText)
    {
        String text = prefs.getString(key, null);
        if (text != null)
        {
            editText.setText(text);
        }
    }

    private void loadState(SharedPreferences prefs, String key, CheckBox checkBox)
    {
        checkBox.setChecked(prefs.getBoolean(key, checkBox.isChecked()));
    }

    private SharedPreferences getSharedPreferences()
    {
        return getSharedPreferences("spacemadness.com.lunarconsole.Preferences", MODE_PRIVATE);
    }
}

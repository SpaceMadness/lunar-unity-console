//
//  Data.m
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

#import "Data.h"

static NSArray * _messages;

@implementation Data

+ (NSArray *)messages
{
    if (_messages == nil) {
        _messages = @[
         @"I/ActivityManager(  748): Start proc 9996:com.google.android.deskclock/u0a33 for broadcast com.google.android.deskclock/com.android.alarmclock.DigitalAppWidgetProvider",
         @"W/ProcessCpuTracker(  748): Skipping unknown process pid 10026",
         @"W/ProcessCpuTracker(  748): Skipping unknown process pid 10027",
         @"W/ProcessCpuTracker(  748): Skipping unknown process pid 10030",
         @"I/ActivityManager(  748): Killing 8819:android.process.acore/u0a3 (adj 15): empty #17",
         @"I/PowerManagerService(  748): Waking up from sleep (uid 1000)...",
         @"I/DisplayPowerController(  748): Blocking screen on until initial contents have been drawn.",
         @"I/DisplayPowerController(  748): Unblocked screen on after 152 ms",
         @"I/DisplayManagerService(  748): Display device changed: DisplayDeviceInfo{\"Built-in Screen\": uniqueId=\"local:0\", 1080 x 1920, 60.0 fps, supportedRefreshRates [60.0], density 480, 442.451 x 443.345 dpi, appVsyncOff 7500000, presDeadline 12666667, touch INTERNAL, rotation 0, type BUILT_IN, state ON, FLAG_DEFAULT_DISPLAY, FLAG_ROTATES_WITH_CONTENT, FLAG_SECURE, FLAG_SUPPORTS_PROTECTED_BUFFERS}",
         @"V/ActivityManager(  748): Display changed displayId=0",
         @"D/WifiService(  748): acquireWifiLockLocked: WifiLock{NlpWifiLock type=2 binder=android.os.BinderProxy@1fa4438}",
         @"D/WifiService(  748): releaseWifiLockLocked: WifiLock{NlpWifiLock type=2 binder=android.os.BinderProxy@1fa4438}",
         @"D/ConnectivityService(  748): reportBadNetwork(NetworkAgentInfo [WIFI () - 123]) by 10007",
         @"D/ConnectivityService(  748): setProvNotificationVisibleIntent: E visible=false networkType=0 extraInfo=null",
         @"D/ConnectivityService(  748): Validated NetworkAgentInfo [WIFI () - 123]",
         @"D/ConnectivityService(  748): updateNetworkScore for NetworkAgentInfo [WIFI () - 123] to 56",
         @"D/ConnectivityService(  748): rematching NetworkAgentInfo [WIFI () - 123]",
         @"D/ConnectivityService(  748): Network NetworkAgentInfo [WIFI () - 123] was already satisfying request 1. No change.",
         @"D/ConnectivityService(  748): notifyType AVAILABLE for NetworkAgentInfo [WIFI () - 123]",
         @"D/ConnectivityService(  748): updateNetworkScore for NetworkAgentInfo [WIFI () - 123] to 60",
         @"D/ConnectivityService(  748): rematching NetworkAgentInfo [WIFI () - 123]",
         @"D/ConnectivityService(  748): Network NetworkAgentInfo [WIFI () - 123] was already satisfying request 1. No change.",
         @"D/ConnectivityService(  748): notifyType AVAILABLE for NetworkAgentInfo [WIFI () - 123]",
         @"I/PowerManagerService(  748): Going to sleep due to screen timeout (uid 1000)...",
         @"I/PowerManagerService(  748): Sleeping (uid 1000)...",
         @"I/DisplayManagerService(  748): Display device changed: DisplayDeviceInfo{\"Built-in Screen\": uniqueId=\"local:0\", 1080 x 1920, 60.0 fps, supportedRefreshRates [60.0], density 480, 442.451 x 443.345 dpi, appVsyncOff 7500000, presDeadline 12666667, touch INTERNAL, rotation 0, type BUILT_IN, state OFF, FLAG_DEFAULT_DISPLAY, FLAG_ROTATES_WITH_CONTENT, FLAG_SECURE, FLAG_SUPPORTS_PROTECTED_BUFFERS}",
         @"V/ActivityManager(  748): Display changed displayId=0",
         @"D/WifiService(  748): acquireWifiLockLocked: WifiLock{WifiScanner type=2 binder=android.os.BinderProxy@f8da662}",
         @"D/ConnectivityService(  748): requestNetwork for NetworkRequest [ id=7, legacyType=3, [ Transports: CELLULAR Capabilities: SUPL&NOT_RESTRICTED&TRUSTED&NOT_VPN] ]",
         @"D/ConnectivityService(  748): handleRegisterNetworkRequest checking NetworkAgentInfo [WIFI () - 123]",
         @"D/ConnectivityService(  748): sending new NetworkRequest to factories",
         @"D/ConnectivityService(  748): setProvNotificationVisible: E visible=false networkType=0 action=com.android.internal.telephony.PROVISION0",
         @"D/ConnectivityService(  748): setProvNotificationVisibleIntent: E visible=false networkType=0 extraInfo=null",
         @"D/ConnectivityService(  748): registerNetworkAgent NetworkAgentInfo{ ni{[type: MOBILE[HSPA], state: CONNECTED/CONNECTED, reason: connected, extra: phone, roaming: false, failover: false, isAvailable: true, isConnectedToProvisioningNetwork: false]}  network{null}  lp{{InterfaceName: rmnet0 LinkAddresses: [10.9.7.7/28,]  Routes: [0.0.0.0/0 -> 10.9.7.8 rmnet0,] DnsAddresses: [172.26.38.1,172.26.38.2,] Domains: null MTU: 1410 TcpBufferSizes: 4094,87380,704512,4096,16384,110208}}  nc{[ Transports: CELLULAR Capabilities: MMS&SUPL&FOTA&INTERNET&NOT_RESTRICTED&TRUSTED&NOT_VPN LinkUpBandwidth>=5898Kbps LinkDnBandwidth>=14336Kbps Specifier: <1>]}  Score{10}  everValidated{false}  lastValidated{false}  created{false}  explicitlySelected{false} }",
         @"D/ConnectivityService(  748): NetworkAgentInfo [MOBILE (HSPA) - 124] EVENT_NETWORK_INFO_CHANGED, going from null to CONNECTED",
         @"D/ConnectivityService(  748): Adding iface rmnet0 to network 124",
         @"D/ConnectivityService(  748): Setting MTU size: rmnet0, 1410",
         @"D/ConnectivityService(  748): Adding Route [0.0.0.0/0 -> 10.9.7.8 rmnet0] to network 124",
         @"D/ConnectivityService(  748): Setting Dns servers for network 124 to [/172.26.38.1, /172.26.38.2]",
         @"D/ConnectivityService(  748): notifyType IP_CHANGED for NetworkAgentInfo [MOBILE (HSPA) - 124]",
         @"D/ConnectivityService(  748): notifyType PRECHECK for NetworkAgentInfo [MOBILE (HSPA) - 124]",
         @"D/ConnectivityService(  748): rematching NetworkAgentInfo [MOBILE (HSPA) - 124]",
         @"D/ConnectivityService(  748):    accepting network in place of null",
         @"D/ConnectivityService(  748): notifyType AVAILABLE for NetworkAgentInfo [MOBILE (HSPA) - 124]",
         @"D/CSLegacyTypeTracker(  748): Sending connected broadcast for type 3 NetworkAgentInfo [MOBILE (HSPA) - 124] isDefaultNetwork=false",
         @"D/ConnectivityService(  748): sendStickyBroadcast: action=android.net.conn.CONNECTIVITY_CHANGE_IMMEDIATE",
         @"D/ConnectivityService(  748): sendStickyBroadcast: action=android.net.conn.CONNECTIVITY_CHANGE",
         @"D/ConnectivityService(  748): Adding ::/128 -> :: rmnet0 for interface rmnet0",
         @"D/ConnectivityService(  748): requestRouteToHostAddress ok=true",
         @"D/ConnectivityService(  748): setProvNotificationVisibleIntent: E visible=false networkType=0 extraInfo=null",
         @"D/ConnectivityService(  748): Validated NetworkAgentInfo [MOBILE (HSPA) - 124]",
         @"D/ConnectivityService(  748): rematching NetworkAgentInfo [MOBILE (HSPA) - 124]",
         @"D/ConnectivityService(  748): Network NetworkAgentInfo [MOBILE (HSPA) - 124] was already satisfying request 7. No change.",
         @"D/ConnectivityService(  748): notifyType AVAILABLE for NetworkAgentInfo [MOBILE (HSPA) - 124]",
         @"E/ConnectivityService(  748): Attempting to register duplicate agent for type 3: NetworkAgentInfo{ ni{[type: MOBILE[HSPA], state: CONNECTED/CONNECTED, reason: connected, extra: phone, roaming: false, failover: false, isAvailable: true, isConnectedToProvisioningNetwork: false]}  network{124}  lp{{InterfaceName: rmnet0 LinkAddresses: [10.9.7.7/28,]  Routes: [0.0.0.0/0 -> 10.9.7.8 rmnet0,] DnsAddresses: [172.26.38.1,172.26.38.2,] Domains: null MTU: 1410 TcpBufferSizes: 4094,87380,704512,4096,16384,110208}}  nc{[ Transports: CELLULAR Capabilities: MMS&SUPL&FOTA&INTERNET&NOT_RESTRICTED&TRUSTED&NOT_VPN LinkUpBandwidth>=5898Kbps LinkDnBandwidth>=14336Kbps Specifier: <1>]}  Score{50}  everValidated{true}  lastValidated{true}  created{true}  explicitlySelected{false} }"
        ];
    }
    return _messages;
}


@end

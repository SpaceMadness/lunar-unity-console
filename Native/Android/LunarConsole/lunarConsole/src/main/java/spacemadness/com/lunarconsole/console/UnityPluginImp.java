//
//  UnityPluginImp.java
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

package spacemadness.com.lunarconsole.console;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.unity3d.player.UnityPlayer;

import java.lang.ref.WeakReference;
import java.util.Map;

import spacemadness.com.lunarconsole.core.LunarConsoleException;
import spacemadness.com.lunarconsole.debug.Log;
import spacemadness.com.lunarconsole.utils.UIUtils;
import static spacemadness.com.lunarconsole.debug.Tags.*;

public class UnityPluginImp implements ConsolePluginImp
{
    private final WeakReference<UnityPlayer> playerRef;
    private final UnityScriptMessenger scriptMessenger;

    public UnityPluginImp(Activity activity, String target, String method)
    {
        UnityPlayer player = resolveUnityPlayerInstance(activity);
        if (player == null)
        {
            throw new LunarConsoleException("Can't initialize plugin: UnityPlayer instance not resolved");
        }

        playerRef = new WeakReference<>(player);
        scriptMessenger = new UnityScriptMessenger(target, method);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Helper

    private static UnityPlayer resolveUnityPlayerInstance(Activity activity)
    {
        return resolveUnityPlayerInstance(UIUtils.getRootViewGroup(activity));
    }

    private static UnityPlayer resolveUnityPlayerInstance(ViewGroup root)
    {
        if (root instanceof UnityPlayer)
        {
            return (UnityPlayer) root;
        }

        for (int i = 0; i < root.getChildCount(); ++i)
        {
            View child = root.getChildAt(i);
            if (child instanceof UnityPlayer)
            {
                return (UnityPlayer) child;
            }

            if (child instanceof ViewGroup)
            {
                UnityPlayer player = resolveUnityPlayerInstance((ViewGroup) child);
                if (player != null)
                {
                    return player;
                }
            }
        }

        return null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ConsolePluginImp

    @Override
    public View getTouchRecepientView()
    {
        return getPlayer();
    }

    @Override
    public void sendUnityScriptMessage(String name, Map<String, Object> data)
    {
        try
        {
            scriptMessenger.sendMessage(name, data);
        }
        catch (Exception e)
        {
            Log.e(PLUGIN, "Error while sending Unity script message: name=%s param=%s", name, data);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    private UnityPlayer getPlayer()
    {
        return playerRef.get();
    }
}

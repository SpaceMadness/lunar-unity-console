//
//  DefaultPluginImp.java
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

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import spacemadness.com.lunarconsole.core.LunarConsoleException;
import spacemadness.com.lunarconsole.debug.Log;
import spacemadness.com.lunarconsole.debug.TestHelper;
import spacemadness.com.lunarconsole.utils.UIUtils;
import static spacemadness.com.lunarconsole.debug.Tags.*;
import static spacemadness.com.lunarconsole.debug.TestHelper.TEST_EVENT_NATIVE_CALLBACK;

class DefaultPluginImp implements ConsolePluginImp
{
    private final WeakReference<View> rootViewRef;

    public DefaultPluginImp(Activity activity)
    {
        View rootView = UIUtils.getRootViewGroup(activity);
        if (rootView == null)
        {
            throw new LunarConsoleException("Can't initialize plugin: root view not found");
        }

        rootViewRef = new WeakReference<>(rootView);
    }

    @Override
    public View getTouchRecepientView()
    {
        return rootViewRef.get();
    }

    @Override
    public void sendUnityScriptMessage(String name, Map<String, Object> data)
    {
        Log.d(PLUGIN, "Send script message: %s(%s)", name, data);
        TestHelper.testEvent(TEST_EVENT_NATIVE_CALLBACK, "name", name, "arguments", data);
    }
}

//
//  UnityScriptMessenger.java
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

import com.unity3d.player.UnityPlayer;

import java.util.HashMap;
import java.util.Map;

import spacemadness.com.lunarconsole.utils.StringUtils;

public class UnityScriptMessenger
{
    private final String target;
    private final String methodName;

    public UnityScriptMessenger(String target, String methodName)
    {
        if (target == null)
        {
            throw new NullPointerException("Target is null");
        }

        if (methodName == null)
        {
            throw new NullPointerException("Method name is null");
        }

        this.target = target;
        this.methodName = methodName;
    }

    public void sendMessage(String name)
    {
        sendMessage(name, null);
    }

    public void sendMessage(String name, Map<String, Object> data)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        if (data != null && data.size() > 0)
        {
            params.putAll(data);
        }

        String param = StringUtils.serializeToString(params);
        UnityPlayer.UnitySendMessage(target, methodName, param);
    }
}

//
//  UnityScriptMessenger.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2016 Alex Lementuev, SpaceMadness.
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

public class UnityScriptMessenger
{
    private final String target;

    public UnityScriptMessenger(String target)
    {
        if (target == null)
        {
            throw new NullPointerException("Target is null");
        }

        this.target = target;
    }

    public void sendMessage(String message)
    {
        sendMessage(message, "");
    }

    public void sendMessage(String message, String param)
    {
        UnityPlayer.UnitySendMessage(target, message, param);
    }
}

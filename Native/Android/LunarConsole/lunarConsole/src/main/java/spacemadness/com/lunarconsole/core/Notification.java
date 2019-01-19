//
//  Notification.java
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

package spacemadness.com.lunarconsole.core;

import java.util.Map;

import spacemadness.com.lunarconsole.utils.ObjectUtils;

public class Notification
{
    private final String name;
    private final Map<String, Object> userData;

    public Notification(String name, Map<String, Object> userData)
    {
        if (name == null)
        {
            throw new NullPointerException("Name is null");
        }

        this.name = name;
        this.userData = userData;
    }

    public String getName()
    {
        return name;
    }

    public Map<String, Object> getUserData()
    {
        return userData;
    }

    public Object getUserData(String key)
    {
        return userData != null ? userData.get(key) : null;
    }

    public <T> T getUserData(String key, Class<T> cls)
    {
        return ObjectUtils.as(getUserData(key), cls);
    }
}

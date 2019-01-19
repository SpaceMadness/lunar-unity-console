//
//  NotificationCenter.java
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spacemadness.com.lunarconsole.debug.Log;

public class NotificationCenter // FIXME: cover with unit-tests
{
    private final Map<String, NotificationList> listLookup;

    public NotificationCenter()
    {
        this.listLookup = new HashMap<>();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Notifications

    public void postNotification(String name)
    {
        postNotification(name, null);
    }

    public void postNotification(String name, String key, Object value)
    {
        Map<String, Object> userData = new HashMap<>();
        userData.put(key, value);
        postNotification(name, userData);
    }

    public void postNotification(String name, Map<String, Object> userData)
    {
        final NotificationList list = findNotificationList(name);
        if (list != null)
        {
            final Notification notification = new Notification(name, userData);
            list.postNotification(notification);
        }
    }

    public NotificationCenter addListener(String name, OnNotificationListener listener)
    {
        NotificationList list = resolveNotificationList(name);
        list.add(listener);
        return this;
    }

    public NotificationCenter removeListener(String name, OnNotificationListener listener)
    {
        NotificationList list = findNotificationList(name);
        if (list != null)
        {
            list.remove(listener);
            if (list.isEmpty())
            {
                removeNotificationList(name);
            }
        }
        return this;
    }

    public void removeListener(OnNotificationListener listener)
    {
        List<String> keysToRemove = null;
        for (Map.Entry<String, NotificationList> entry : listLookup.entrySet())
        {
            NotificationList list = entry.getValue();
            if (list.remove(listener) && list.isEmpty())
            {
                if (keysToRemove == null)
                {
                    keysToRemove = new ArrayList<>();
                }
                keysToRemove.add(entry.getKey());
            }
        }

        if (keysToRemove != null)
        {
            for (String key : keysToRemove)
            {
                listLookup.remove(key);
            }
        }
    }

    private NotificationList resolveNotificationList(String name)
    {
        NotificationList list = listLookup.get(name);
        if (list == null)
        {
            list = new NotificationList();
            listLookup.put(name, list);
        }
        return list;
    }

    private void removeNotificationList(String name)
    {
        listLookup.remove(name);
    }

    private NotificationList findNotificationList(String name)
    {
        return listLookup.get(name);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    public static NotificationCenter defaultCenter()
    {
        return Holder.INSTANCE;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Instance holder

    private static class Holder
    {
        private static final NotificationCenter INSTANCE = new NotificationCenter();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Callback

    public interface OnNotificationListener
    {
        void onNotification(Notification notification);
    }

    private static class NotificationList extends ArrayList<OnNotificationListener>
    {
        public void postNotification(Notification notification)
        {
            for (OnNotificationListener listener : this)
            {
                try
                {
                    listener.onNotification(notification);
                }
                catch (Exception e)
                {
                    Log.e(e, "Exception while notifying listener: %s", listener.getClass());
                }
            }
        }
    }
}

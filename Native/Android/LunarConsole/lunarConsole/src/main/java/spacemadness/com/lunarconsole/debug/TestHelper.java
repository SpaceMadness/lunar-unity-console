//
//  TestHelper.java
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

package spacemadness.com.lunarconsole.debug;

import java.util.HashMap;
import java.util.Map;

public class TestHelper // TODO: make a separate flavor for this class
{
    public static final String TEST_EVENT_OVERLAY_ADD_ITEM              = "TEST_EVENT_OVERLAY_ADD_ITEM"; // data: CycleArray<ConsoleLogEntry>
    public static final String TEST_EVENT_OVERLAY_REMOVE_ITEM           = "TEST_EVENT_OVERLAY_REMOVE_ITEM"; // data: CycleArray<ConsoleLogEntry>
    public static final String TEST_EVENT_OVERLAY_SCHEDULE_ITEM_REMOVAL = "TEST_EVENT_OVERLAY_SCHEDULE_ITEM_REMOVAL";
    public static final String TEST_EVENT_NATIVE_CALLBACK               = "TEST_EVENT_NATIVE_CALLBACK"; // data: Map<String, Object>

    public static boolean forceSeekBarChangeDelegate;

    private static TestHelper instance;
    private final EventListener listener;

    public TestHelper(EventListener listener)
    {
        this.listener = listener;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Initialization

    public static void init(EventListener listener)
    {
        instance = new TestHelper(listener);
    }

    public static void shutdown()
    {
        instance = null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Initialization

    public static void testEvent(String name)
    {
        if (instance != null)
        {
            instance.event(name, null);
        }
    }

    public static void testEvent(String name, Object data)
    {
        if (instance != null)
        {
            instance.event(name, data);
        }
    }

    public static void testEvent(String name, String key1, Object value1, String key2, Object value2)
    {
        if (instance != null)
        {
            Map<String, Object> data = new HashMap<>();
            data.put(key1, value1);
            data.put(key2, value2);
            instance.event(name, data);
        }
    }

    private synchronized void event(String name, Object data)
    {
        listener.onTestEvent(name, data);
    }

    public interface EventListener
    {
        void onTestEvent(String name, Object data);
    }
}

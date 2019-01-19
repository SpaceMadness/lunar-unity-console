//
//  ConsoleLogEntryDispatcher.java
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

import java.util.ArrayList;
import java.util.List;

import spacemadness.com.lunarconsole.debug.Log;
import spacemadness.com.lunarconsole.utils.ThreadUtils;

/** Class for handling batches of console entries on UI-thread */
class ConsoleLogEntryDispatcher
{
    private final OnDispatchListener listener;
    private final List<ConsoleLogEntry> entries;
    private final Runnable dispatchRunnable;

    public ConsoleLogEntryDispatcher(OnDispatchListener listener)
    {
        if (listener == null)
        {
            throw new NullPointerException("Listener is null");
        }

        this.listener = listener;
        this.entries = new ArrayList<>();
        this.dispatchRunnable = createDispatchRunnable();
    }

    public void add(ConsoleLogEntry entry)
    {
        synchronized (entries)
        {
            entries.add(entry);

            if (entries.size() == 1)
            {
                postEntriesDispatch();
            }
        }
    }

    protected void postEntriesDispatch()
    {
        ThreadUtils.runOnUIThread(dispatchRunnable);
    }

    protected void cancelEntriesDispatch()
    {
        ThreadUtils.cancel(dispatchRunnable);
    }

    protected void dispatchEntries()
    {
        synchronized (entries)
        {
            try
            {
                listener.onDispatchEntries(entries);
            }
            catch (Exception e)
            {
                Log.e(e, "Can't dispatch entries");
            }
            entries.clear();
        }
    }

    private Runnable createDispatchRunnable()
    {
        return new Runnable()
        {
            @Override
            public void run()
            {
                dispatchEntries();
            }
        };
    }


    public void cancelAll()
    {
        cancelEntriesDispatch();

        synchronized (entries)
        {
            entries.clear();
        }
    }

    public interface OnDispatchListener
    {
        void onDispatchEntries(List<ConsoleLogEntry> entries);
    }
}

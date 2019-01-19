//
//  ConsoleOverlayLogView.java
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

import android.content.Context;
import android.view.MotionEvent;
import android.widget.ListView;

import spacemadness.com.lunarconsole.core.Destroyable;
import spacemadness.com.lunarconsole.debug.Log;
import spacemadness.com.lunarconsole.utils.CycleArray;
import spacemadness.com.lunarconsole.utils.ThreadUtils;

import static spacemadness.com.lunarconsole.console.BaseConsoleLogAdapter.DataSource;
import static spacemadness.com.lunarconsole.debug.Tags.OVERLAY_VIEW;

import static spacemadness.com.lunarconsole.debug.TestHelper.*;

public class ConsoleOverlayLogView extends ListView implements Destroyable, DataSource, LunarConsoleListener
{
    private final Console console;

    private final Settings settings;

    private final ConsoleOverlayLogAdapter consoleAdapter;

    private final CycleArray<ConsoleLogEntry> entries;

    private final Runnable entryRemovalCallback = new Runnable()
    {
        @Override
        public void run()
        {
            if (entryRemovalScheduled)
            {
                entryRemovalScheduled = false;

                // remove first visible row
                if (entries.realLength() > 0)
                {
                    testEvent(TEST_EVENT_OVERLAY_REMOVE_ITEM, entries);

                    entries.trimHeadIndex(1);
                    reloadData();
                }

                // if more entries are visible - schedule another removal
                if (entries.realLength() > 0)
                {
                    scheduleEntryRemoval();
                }
            }
        }
    };
    private boolean entryRemovalScheduled;

    public ConsoleOverlayLogView(Context context, Console console, Settings settings)
    {
        super(context);

        if (console == null)
        {
            throw new NullPointerException("Console is null");
        }

        if (settings == null)
        {
            throw new NullPointerException("Settings is null");
        }

        this.console = console;
        this.console.setConsoleListener(this);

        this.settings = settings;

        entries = new CycleArray<>(ConsoleLogEntry.class, settings.maxVisibleEntries);
        consoleAdapter = new ConsoleOverlayLogAdapter(this);

        setDivider(null);
        setDividerHeight(0);
        setAdapter(consoleAdapter);
        setOverScrollMode(ListView.OVER_SCROLL_NEVER);
        setScrollingCacheEnabled(false);

        reloadData();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Events

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        return false; // no touch events
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Data

    private void reloadData()
    {
        consoleAdapter.notifyDataSetChanged();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Entry removal

    private void scheduleEntryRemoval()
    {
        if (!entryRemovalScheduled)
        {
            entryRemovalScheduled = true;
            ThreadUtils.runOnUIThread(entryRemovalCallback, settings.entryDisplayTimeMillis);

            testEvent(TEST_EVENT_OVERLAY_SCHEDULE_ITEM_REMOVAL);
        }
    }

    private void cancelEntryRemoval()
    {
        entryRemovalScheduled = false;
        ThreadUtils.cancel(entryRemovalCallback);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Destroyable

    @Override
    public void destroy()
    {
        Log.d(OVERLAY_VIEW, "Destroy overlay view");
        if (console.getConsoleListener() == this)
        {
            console.setConsoleListener(null);
        }
        cancelEntryRemoval();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // DataSource

    @Override
    public ConsoleLogEntry getEntry(int position)
    {
        return entries.get(entries.getHeadIndex() + position);
    }

    @Override
    public int getEntryCount()
    {
        return entries.realLength();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LunarConsoleListener

    @Override
    public void onAddEntry(Console console, ConsoleLogEntry entry, boolean filtered)
    {
        entries.add(entry); // cycle array will handle entries trim
        reloadData();

        testEvent(TEST_EVENT_OVERLAY_ADD_ITEM, entries);

        scheduleEntryRemoval();
    }

    @Override
    public void onRemoveEntries(Console console, int start, int length)
    {
        // just do nothing
    }

    @Override
    public void onChangeEntries(Console console)
    {
        // just do nothing
    }

    @Override
    public void onClearEntries(Console console)
    {
        cancelEntryRemoval();
        entries.clear();
        reloadData();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Settings

    public static class Settings
    {
        /** How many entries can be visible at the same time */
        public int maxVisibleEntries;

        /** How much time each row would be displayed on the screen */
        public long entryDisplayTimeMillis;

        public Settings()
        {
            maxVisibleEntries = 3;
            entryDisplayTimeMillis = 1000;
        }
    }
}

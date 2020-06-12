//
//  LogOverlayView.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2015-2020 Alex Lementuev, SpaceMadness.
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

import spacemadness.com.lunarconsole.concurrent.DispatchQueue;
import spacemadness.com.lunarconsole.concurrent.DispatchTask;
import spacemadness.com.lunarconsole.core.Destroyable;
import spacemadness.com.lunarconsole.debug.Log;
import spacemadness.com.lunarconsole.settings.LogColors;
import spacemadness.com.lunarconsole.settings.LogEntryColors;
import spacemadness.com.lunarconsole.settings.LogOverlaySettings;
import spacemadness.com.lunarconsole.utils.CycleArray;

import static spacemadness.com.lunarconsole.console.BaseConsoleLogAdapter.DataSource;
import static spacemadness.com.lunarconsole.debug.Tags.OVERLAY_VIEW;

import static spacemadness.com.lunarconsole.debug.TestHelper.*;

public class LogOverlayView extends ListView implements Destroyable, DataSource, LunarConsoleListener {
    private final Console console;

    private final DispatchQueue dispatchQueue;

    private final LogOverlaySettings settings;

    private final LogOverlayAdapter adapter;

    private final CycleArray<OverlayEntry> entries;

    private final DispatchTask entryRemovalTask = new DispatchTask() {
        @Override
        protected void execute() {
            // remove first visible row
            if (entries.realLength() > 0) {
                testEvent(TEST_EVENT_OVERLAY_REMOVE_ITEM, entries);

                entries.trimHeadIndex(1);
                reloadData();
            }

            // if more entries are visible - schedule another removal
            if (entries.realLength() > 0) {
                scheduleEntryRemoval(entries.get(entries.getHeadIndex()));
            }
        }
    };

    public LogOverlayView(Context context, Console console, LogOverlaySettings settings) {
        this(context, console, settings, DispatchQueue.mainQueue());
    }

    public LogOverlayView(Context context, Console console, LogOverlaySettings settings, DispatchQueue dispatchQueue) {
        super(context);

        if (console == null) {
            throw new NullPointerException("Console is null");
        }

        if (settings == null) {
            throw new NullPointerException("Settings is null");
        }

        this.console = console;
        this.dispatchQueue = dispatchQueue;
        this.console.setConsoleListener(this);

        this.settings = settings;

        entries = new CycleArray<>(OverlayEntry.class, settings.maxVisibleLines);
        adapter = new LogOverlayAdapter(this, createLogViewStyle(settings.colors));

        setDivider(null);
        setDividerHeight(0);
        setAdapter(adapter);
        setOverScrollMode(ListView.OVER_SCROLL_NEVER);
        setScrollingCacheEnabled(false);

        reloadData();
    }

    private static LogViewStyle createLogViewStyle(LogColors colors) {
        return new LogViewStyle(
                createLogEntryStyle(colors.exception),
                createLogEntryStyle(colors.error),
                createLogEntryStyle(colors.warning),
                createLogEntryStyle(colors.debug)
        );
    }

    private static LogEntryStyle createLogEntryStyle(LogEntryColors colors) {
        return new LogEntryStyle(colors.foreground.toARGB(), colors.background.toARGB());
    }

    //region Events

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false; // no touch events
    }

    //endregion

    //region Data

    private void reloadData() {
        adapter.notifyDataSetChanged();
    }

    //endregion

    //region Entry removal

    private void scheduleEntryRemoval(OverlayEntry entry) {
        long delay = Math.max(0, entry.removalTimeStamp - System.currentTimeMillis());

        dispatchQueue.dispatchOnce(entryRemovalTask, delay);

        testEvent(TEST_EVENT_OVERLAY_SCHEDULE_ITEM_REMOVAL);
    }

    private void cancelEntryRemoval() {
        entryRemovalTask.cancel();
    }

    //endregion

    //region Destroyable

    @Override
    public void destroy() {
        Log.d(OVERLAY_VIEW, "Destroy overlay view");
        if (console.getConsoleListener() == this) {
            console.setConsoleListener(null);
        }
        cancelEntryRemoval();
    }

    //endregion

    //region DataSource

    @Override
    public ConsoleLogEntry getEntry(int position) {
        return entries.get(entries.getHeadIndex() + position).entry;
    }

    @Override
    public int getEntryCount() {
        return entries.realLength();
    }

    //endregion

    //region LunarConsoleListener

    @Override
    public void onAddEntry(Console console, ConsoleLogEntry entry, boolean filtered) {

        OverlayEntry overlayEntry = new OverlayEntry(entry, System.currentTimeMillis() + (long) (1000L * settings.timeout));
        entries.add(overlayEntry); // cycle array will handle entries trim
        reloadData();

        testEvent(TEST_EVENT_OVERLAY_ADD_ITEM, entries);

        scheduleEntryRemoval(overlayEntry);
    }

    @Override
    public void onRemoveEntries(Console console, int start, int length) {
        // just do nothing
    }

    @Override
    public void onChangeEntries(Console console) {
        // just do nothing
    }

    @Override
    public void onClearEntries(Console console) {
        cancelEntryRemoval();
        entries.clear();
        reloadData();
    }

    //endregion

    //region Overlay Entry

    private static class OverlayEntry {
        final ConsoleLogEntry entry;
        final long removalTimeStamp;

        OverlayEntry(ConsoleLogEntry entry, long removalTimeStamp) {
            this.entry = entry;
            this.removalTimeStamp = removalTimeStamp;
        }
    }

    //endregion
}

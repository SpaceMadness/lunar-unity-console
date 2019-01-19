//
//  Console.java
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

import spacemadness.com.lunarconsole.core.Destroyable;
import spacemadness.com.lunarconsole.debug.Log;

public class Console implements
        Destroyable,
        ConsoleLogAdapter.DataSource
{
    private static final LunarConsoleListener NULL_LISTENER = new LunarConsoleListener()
    {
        @Override
        public void onAddEntry(Console console, ConsoleLogEntry entry, boolean filtered)
        {
        }

        @Override
        public void onRemoveEntries(Console console, int start, int length)
        {
        }

        @Override
        public void onChangeEntries(Console console)
        {
        }

        @Override
        public void onClearEntries(Console console)
        {
        }
    };

    private final Options options;
    private final ConsoleLogEntryList entries;

    private LunarConsoleListener consoleListener;

    public Console(Options options)
    {
        if (options == null)
        {
            throw new NullPointerException("Options is null");
        }

        this.options = options.clone();
        this.entries = new ConsoleLogEntryList(options.getCapacity(), options.getTrimCount());
        this.consoleListener = NULL_LISTENER;
    }

    public ConsoleLogEntry entryAtIndex(int index)
    {
        return entries.getEntry(index);
    }

    public void logMessage(String message, String stackTrace, byte type)
    {
        logMessage(new ConsoleLogEntry(type, message, stackTrace));
    }

    void logMessage(ConsoleLogEntry entry)
    {
        final int oldTrimmedCount = entries.trimmedCount(); // trimmed count before we added a new item

        // add new entry
        int index = entries.addEntry(entry);

        boolean filtered = index != -1;
        if (filtered)
        {
            entry.index = index; // update index for correct rendering
        }

        // notify listener
        final int trimmedCount = entries.trimmedCount() - oldTrimmedCount;
        if (trimmedCount > 0)
        {
            notifyRemoveEntries(0, trimmedCount);
        }

        notifyEntryAdded(entry, filtered);
    }

    public void clear()
    {
        entries.clear();
        notifyEntriesCleared();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Destroyable

    @Override
    public void destroy()
    {
        entries.clear();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Listener notifications

    private void notifyEntryAdded(ConsoleLogEntry entry, boolean filtered)
    {
        try
        {
            consoleListener.onAddEntry(this, entry, filtered);
        }
        catch (Exception e)
        {
            Log.e(e, "Error while notifying delegate");
        }
    }

    private void notifyRemoveEntries(int start, int length)
    {
        try
        {
            consoleListener.onRemoveEntries(this, start, length);
        }
        catch (Exception e)
        {
            Log.e(e, "Error while notifying delegate");
        }
    }

    private void notifyEntriesChanged()
    {
        try
        {
            consoleListener.onChangeEntries(this);
        }
        catch (Exception e)
        {
            Log.e(e, "Error while notifying delegate");
        }
    }

    private void notifyEntriesCleared()
    {
        try
        {
            consoleListener.onClearEntries(this);
        }
        catch (Exception e)
        {
            Log.e(e, "Error while notifying delegate");
        }
    }

    public String getText()
    {
        return entries.getText();
    }

    public LunarConsoleListener getConsoleListener()
    {
        return consoleListener;
    }

    public void setConsoleListener(LunarConsoleListener listener)
    {
        this.consoleListener = listener != null ? listener : NULL_LISTENER;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    public int getCapacity()
    {
        return entries.capacity();
    }

    public int getTrimSize()
    {
        return entries.trimCount();
    }

    public ConsoleLogEntryList entries()
    {
        return entries;
    }

    public void setCollapsed(boolean collapsed)
    {
        entries.collapsed(collapsed);
        notifyEntriesChanged();
    }

    public boolean isCollapsed()
    {
        return entries.isCollapsed();
    }

    public int entriesCount()
    {
        return entries.count();
    }

    public int trimmedCount()
    {
        return entries.trimmedCount();
    }

    public boolean isTrimmed()
    {
        return entries.isTrimmed();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ConsoleLogAdapter.DataSource

    @Override
    public ConsoleLogEntry getEntry(int position)
    {
        return entries.getEntry(position);
    }

    @Override
    public int getEntryCount()
    {
        return entries.count();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Options

    public static class Options
    {
        private final int capacity;
        private int trimCount;

        public Options(int capacity)
        {
            if (capacity <= 0)
            {
                throw new IllegalArgumentException("Invalid capacity: " + capacity);
            }
            this.capacity = capacity;
            this.trimCount = 1;
        }

        public Options clone()
        {
            Options options = new Options(capacity);
            options.trimCount = trimCount;
            return options;
        }

        public int getCapacity()
        {
            return capacity;
        }

        public int getTrimCount()
        {
            return trimCount;
        }

        public void setTrimCount(int count)
        {
            if (count <= 0 || count > capacity)
            {
                throw new IllegalArgumentException("Illegal trim count: " + count);
            }

            this.trimCount = count;
        }
    }
}

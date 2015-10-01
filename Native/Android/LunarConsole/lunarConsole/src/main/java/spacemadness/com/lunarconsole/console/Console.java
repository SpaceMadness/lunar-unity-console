package spacemadness.com.lunarconsole.console;

import spacemadness.com.lunarconsole.core.Destroyable;
import spacemadness.com.lunarconsole.debug.Log;

public class Console implements
        Destroyable,
        ConsoleAdapter.DataSource
{
    private static final LunarConsoleListener NULL_LISTENER = new LunarConsoleListener()
    {
        @Override
        public void onAddEntry(Console console, ConsoleEntry entry, boolean filtered)
        {
        }

        @Override
        public void onRemoveEntries(Console console, int start, int length)
        {
        }

        @Override
        public void onClearEntries(Console console)
        {
        }
    };

    private final Options options;
    private final ConsoleEntryList entries;

    private LunarConsoleListener consoleListener;

    public Console(Options options)
    {
        if (options == null)
        {
            throw new NullPointerException("Options is null");
        }

        this.options = options.clone();
        this.entries = new ConsoleEntryList(options.getCapacity());
        this.consoleListener = NULL_LISTENER;
    }

    public ConsoleEntry entryAtIndex(int index)
    {
        return entries.getEntry(index);
    }

    public void logMessage(String message, String stackTrace, byte type)
    {
        logMessage(new ConsoleEntry(type, message, stackTrace));
    }

    void logMessage(ConsoleEntry entry)
    {
        entry.index = entries.totalCount();

        boolean filtered = entries.filterEntry(entry);

        // free some space is entries overflow
        if (filtered && entries.willOverflow())
        {
            int clearCount = Math.min(options.getOverflowFreeAmount(), entries.count());
            if (clearCount > 0)
            {
                entries.trimHead(clearCount);

                // notify region removed
                notifyRemoveEntriesRange(0, clearCount);
            }
        }

        // add new entry
        entries.addEntry(entry);

        // notify entry added
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

    private void notifyEntryAdded(ConsoleEntry entry, boolean filtered)
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

    private void notifyRemoveEntriesRange(int start, int length)
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

    public ConsoleEntryList entries()
    {
        return entries;
    }

    public int entriesCount()
    {
        return entries.count();
    }

    public int overflowAmount()
    {
        return entries.overflowAmount();
    }

    public boolean isOverfloating()
    {
        return entries.isOverfloating();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ConsoleAdapter.DataSource

    @Override
    public ConsoleEntry getEntry(int position)
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

        private int overflowFreeAmount;

        public Options(int capacity)
        {
            if (capacity <= 0)
            {
                throw new IllegalArgumentException("Invalid capacity: " + capacity);
            }
            this.capacity = capacity;
            this.overflowFreeAmount = 1;
        }

        public Options clone()
        {
            Options options = new Options(capacity);
            options.overflowFreeAmount = overflowFreeAmount;
            return options;
        }

        public int getCapacity()
        {
            return capacity;
        }

        public int getOverflowFreeAmount()
        {
            return overflowFreeAmount;
        }

        public void setOverflowFreeAmount(int overflowFreeAmount)
        {
            if (overflowFreeAmount <= 0 || overflowFreeAmount > capacity)
            {
                throw new IllegalArgumentException("Illegal free overflow: " + overflowFreeAmount);
            }
            this.overflowFreeAmount = overflowFreeAmount;
        }
    }
}

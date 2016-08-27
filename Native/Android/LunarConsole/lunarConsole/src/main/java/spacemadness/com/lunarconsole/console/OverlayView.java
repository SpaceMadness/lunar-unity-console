package spacemadness.com.lunarconsole.console;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.ListView;

import spacemadness.com.lunarconsole.core.Destroyable;
import spacemadness.com.lunarconsole.debug.Log;
import spacemadness.com.lunarconsole.utils.CycleArray;
import spacemadness.com.lunarconsole.utils.ThreadUtils;

import static spacemadness.com.lunarconsole.console.BaseConsoleAdapter.DataSource;
import static spacemadness.com.lunarconsole.debug.Tags.OVERLAY_VIEW;

public class OverlayView extends ListView implements Destroyable, DataSource, LunarConsoleListener
{
    private final Console console;
    private final ConsoleOverlayAdapter consoleAdapter;
    private final CycleArray<ConsoleEntry> entries;
    private final Callback[] callbacks;
    private int callbackIndex;

    public OverlayView(Context context, Console console)
    {
        super(context);

        if (console == null)
        {
            throw new NullPointerException("Console is null");
        }

        this.console = console;
        this.console.setConsoleListener(this);

        entries = new CycleArray<>(ConsoleEntry.class, 3); // TODO: make a setting
        callbacks = new Callback[entries.getCapacity()];
        for (int i = 0; i < callbacks.length; ++i)
        {
            callbacks[i] = new Callback();
        }

        consoleAdapter = new ConsoleOverlayAdapter(this);

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
    // Callbacks

    private void cancelCallbacks()
    {
        for (int i = 0; i < callbacks.length; ++i)
        {
            callbacks[i].cancel();
        }
        callbackIndex = 0;
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
        cancelCallbacks();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // DataSource

    @Override
    public ConsoleEntry getEntry(int position)
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
    public void onAddEntry(Console console, ConsoleEntry entry, boolean filtered)
    {
        entries.add(entry);
        reloadData();

        Callback callback = callbacks[callbackIndex];
        callbackIndex = (callbackIndex + 1) % callbacks.length;
        callback.schedule(entry, 1000); // TODO: make it configurable
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
        cancelCallbacks();
        entries.clear();
        reloadData();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Callback

    private class Callback implements Runnable
    {
        public ConsoleEntry entry;

        public synchronized void schedule(ConsoleEntry entry, long delay)
        {
            cancel();

            this.entry = entry;
            ThreadUtils.runOnUIThread(this, delay);
        }

        public synchronized void cancel()
        {
            if (this.entry != null)
            {
                this.entry = null;
                ThreadUtils.cancel(this);
            }
        }

        @Override
        public synchronized void run()
        {
            final int index = entries.getHeadIndex();
            if (entries.length() > 0 && entries.get(index) == entry)
            {
                entries.trimHeadIndex(1);
                reloadData();
            }
            entry = null;
        }
    }

}

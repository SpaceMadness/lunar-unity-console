package spacemadness.com.lunarconsole.console;

import java.util.ArrayList;
import java.util.List;

import spacemadness.com.lunarconsole.debug.Log;
import spacemadness.com.lunarconsole.utils.ThreadUtils;

/** Class for handling batches of console entries on UI-thread */
class ConsoleEntryDispatcher
{
    private final OnDispatchListener listener;
    private final List<ConsoleEntry> entries;
    private final Runnable dispatchRunnable;

    public ConsoleEntryDispatcher(OnDispatchListener listener)
    {
        if (listener == null)
        {
            throw new NullPointerException("Listener is null");
        }

        this.listener = listener;
        this.entries = new ArrayList<>();
        this.dispatchRunnable = createDispatchRunnable();
    }

    public void add(ConsoleEntry entry)
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
        void onDispatchEntries(List<ConsoleEntry> entries);
    }
}

package spacemadness.com.lunarconsole.console;

import android.content.Context;
import android.widget.ListView;

import spacemadness.com.lunarconsole.core.Destroyable;
import spacemadness.com.lunarconsole.debug.Log;

import static spacemadness.com.lunarconsole.debug.Tags.OVERLAY_VIEW;

public class OverlayView extends ListView implements Destroyable, LunarConsoleListener
{
    private final Console console;
    private final ConsoleOverlayAdapter consoleAdapter;

    public OverlayView(Context context, Console console)
    {
        super(context);

        if (console == null)
        {
            throw new NullPointerException("Console is null");
        }

        this.console = console;
        this.console.setConsoleListener(this);

        consoleAdapter = new ConsoleOverlayAdapter(console);

        setDivider(null);
        setDividerHeight(0);
        setAdapter(consoleAdapter);
        setOverScrollMode(ListView.OVER_SCROLL_NEVER);
        setScrollingCacheEnabled(false);

        reloadData();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Data

    private void reloadData()
    {
        consoleAdapter.notifyDataSetChanged();
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
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LunarConsoleListener

    @Override
    public void onAddEntry(Console console, ConsoleEntry entry, boolean filtered)
    {
        reloadData();
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
        reloadData();
    }
}

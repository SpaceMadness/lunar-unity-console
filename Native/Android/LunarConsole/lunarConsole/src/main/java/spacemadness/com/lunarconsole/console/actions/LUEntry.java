package spacemadness.com.lunarconsole.console.actions;

import android.content.Context;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.console.ConsoleEntry;

public abstract class LUEntry extends ConsoleEntry implements Comparable<LUEntry> // FIXME: rename
{
    private final int actionId; // FIXME: rename
    private final String name;

    public LUEntry(int actionId, String name)
    {
        this.actionId = actionId;
        this.name = name;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Appearance

    @SuppressWarnings("deprecation")
    public int getBackgroundColor(Context context, int position)
    {
        int colorId = position % 2 == 0 ?
                R.color.lunar_console_color_cell_background_dark :
                R.color.lunar_console_color_cell_background_light;
        return context.getResources().getColor(colorId);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    public int actionId() // FIXME: rename
    {
        return actionId;
    }

    public String name() // FIXME: rename
    {
        return name;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Comparable

    @Override
    public int compareTo(LUEntry another)
    {
        return name.compareTo(another.name);
    }
}

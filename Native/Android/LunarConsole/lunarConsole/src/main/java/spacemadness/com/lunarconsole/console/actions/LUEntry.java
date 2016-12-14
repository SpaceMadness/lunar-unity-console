package spacemadness.com.lunarconsole.console.actions;

/**
 * Created by alementuev on 12/13/16.
 */

public class LUEntry implements Comparable<LUEntry> // FIXME: rename
{
    private final int actionId; // FIXME: rename
    private final String name;

    public LUEntry(int actionId, String name)
    {
        this.actionId = actionId;
        this.name = name;
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

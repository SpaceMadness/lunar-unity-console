package spacemadness.com.lunarconsole.console.actions;

/**
 * Created by alementuev on 1/23/17.
 */

public class LUHeaderEntry extends LUEntry
{
    public LUHeaderEntry(String name)
    {
        super(-1, name);
    }

    @Override
    protected LUEntryType getEntryType()
    {
        return LUEntryType.Header;
    }
}

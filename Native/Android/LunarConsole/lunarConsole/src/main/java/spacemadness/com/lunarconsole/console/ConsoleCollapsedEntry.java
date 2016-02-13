package spacemadness.com.lunarconsole.console;

/**
 * Created by alementuev on 2/4/16.
 */
public class ConsoleCollapsedEntry extends ConsoleEntry
{
    /** Total amount of encounters */
    public int count;

    public ConsoleCollapsedEntry(ConsoleEntry entry)
    {
        super(entry.type, entry.message, entry.stackTrace);

        count = 1;
        index = -1;
    }

    public void increaseCount()
    {
        ++count;
    }
}

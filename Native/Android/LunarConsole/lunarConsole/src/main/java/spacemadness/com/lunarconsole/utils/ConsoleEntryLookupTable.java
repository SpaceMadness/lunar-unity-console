package spacemadness.com.lunarconsole.utils;

import java.util.HashMap;
import java.util.Map;

import spacemadness.com.lunarconsole.console.ConsoleCollapsedEntry;
import spacemadness.com.lunarconsole.console.ConsoleEntry;

public class ConsoleEntryLookupTable
{
    private final Map<String, ConsoleCollapsedEntry> table;

    public ConsoleEntryLookupTable()
    {
        table = new HashMap<>();
    }

    public ConsoleCollapsedEntry addEntry(ConsoleEntry entry)
    {
        ConsoleCollapsedEntry collapsedEntry = table.get(entry.message);
        if (collapsedEntry == null)
        {
            collapsedEntry = new ConsoleCollapsedEntry(entry);
            table.put(collapsedEntry.message, collapsedEntry);
        }
        else
        {
            collapsedEntry.increaseCount();
        }

        return collapsedEntry;
    }

    public void removeEntry(ConsoleCollapsedEntry entry)
    {
        table.remove(entry.message);
    }

    public void clear()
    {
        table.clear();
    }
}

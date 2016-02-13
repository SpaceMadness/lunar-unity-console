package spacemadness.com.lunarconsole.utils;

import junit.framework.TestCase;

import spacemadness.com.lunarconsole.console.ConsoleCollapsedEntry;
import spacemadness.com.lunarconsole.console.ConsoleEntry;
import spacemadness.com.lunarconsole.console.ConsoleLogType;

public class ConsoleEntryLookupTableTest extends TestCase
{
    private ConsoleEntryLookupTable table;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        table = new ConsoleEntryLookupTable();
    }

    public void testAddEntry()
    {
        ConsoleCollapsedEntry entry = addEntryMessage("message1");
        assertEquals(1, entry.count);

        entry = addEntryMessage("message1");
        assertEquals(2, entry.count);

        entry = addEntryMessage("message2");
        assertEquals(1, entry.count);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Helpers

    private ConsoleCollapsedEntry addEntryMessage(String message)
    {
        return addEntryType(ConsoleLogType.LOG, message, "");
    }

    private ConsoleCollapsedEntry addEntryType(byte type, String message, String stackTrace)
    {
        ConsoleEntry entry = new ConsoleEntry(type, message, stackTrace);
        return table.addEntry(entry);
    }
}
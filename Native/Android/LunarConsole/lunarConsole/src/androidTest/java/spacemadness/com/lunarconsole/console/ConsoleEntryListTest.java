package spacemadness.com.lunarconsole.console;

import junit.framework.TestCase;

import static spacemadness.com.lunarconsole.console.ConsoleLogType.*;

public class ConsoleEntryListTest extends TestCase
{
    public void testFilteringByText()
    {
        ConsoleEntryList list = createEntryListWithMessages(
            "line1",
            "line11",
            "line111",
            "line1111",
            "foo");

        assertTrue(!list.isFiltering());

        assertTrue(list.setFilterByText("l"));
        listAssertMessages(list, "line1", "line11", "line111", "line1111");
        assertTrue(list.isFiltering());

        assertTrue(!list.setFilterByText("l"));
        listAssertMessages(list, "line1", "line11", "line111", "line1111");
        assertTrue(list.isFiltering());

        assertTrue(list.setFilterByText("li"));
        listAssertMessages(list, "line1", "line11", "line111", "line1111");
        assertTrue(list.isFiltering());

        assertTrue(list.setFilterByText("lin"));
        listAssertMessages(list, "line1", "line11", "line111", "line1111");
        assertTrue(list.isFiltering());

        assertTrue(list.setFilterByText("line"));
        listAssertMessages(list, "line1", "line11", "line111", "line1111");
        assertTrue(list.isFiltering());

        assertTrue(list.setFilterByText("line1"));
        listAssertMessages(list, "line1", "line11", "line111", "line1111");
        assertTrue(list.isFiltering());

        assertTrue(list.setFilterByText("line11"));
        listAssertMessages(list, "line11", "line111", "line1111");
        assertTrue(list.isFiltering());

        assertTrue(list.setFilterByText("line111"));
        listAssertMessages(list, "line111", "line1111");
        assertTrue(list.isFiltering());

        assertTrue(list.setFilterByText("line1111"));
        listAssertMessages(list, "line1111");
        assertTrue(list.isFiltering());

        assertTrue(list.setFilterByText("line11111"));
        listAssertMessages(list);
        assertTrue(list.isFiltering());

        assertTrue(list.setFilterByText("line1111"));
        listAssertMessages(list, "line1111");
        assertTrue(list.isFiltering());

        assertTrue(list.setFilterByText("line111"));
        listAssertMessages(list, "line111", "line1111");
        assertTrue(list.isFiltering());

        assertTrue(list.setFilterByText("line11"));
        listAssertMessages(list, "line11", "line111", "line1111");
        assertTrue(list.isFiltering());

        assertTrue(list.setFilterByText("line1"));
        listAssertMessages(list, "line1", "line11", "line111", "line1111");
        assertTrue(list.isFiltering());

        assertTrue(list.setFilterByText("line"));
        listAssertMessages(list, "line1", "line11", "line111", "line1111");
        assertTrue(list.isFiltering());

        assertTrue(list.setFilterByText("lin"));
        listAssertMessages(list, "line1", "line11", "line111", "line1111");
        assertTrue(list.isFiltering());

        assertTrue(list.setFilterByText("li"));
        listAssertMessages(list, "line1", "line11", "line111", "line1111");
        assertTrue(list.isFiltering());

        assertTrue(list.setFilterByText("l"));
        listAssertMessages(list, "line1", "line11", "line111", "line1111");
        assertTrue(list.isFiltering());

        assertTrue(list.setFilterByText(""));
        listAssertMessages(list, "line1", "line11", "line111", "line1111", "foo");
        assertTrue(!list.isFiltering());
    }


    public void testFilteringByLogType()
    {
        ConsoleEntryList list = createEntryListWithEntries(
                new ConsoleEntry(ERROR, "error1"),
                new ConsoleEntry(ERROR, "error2"),
                new ConsoleEntry(ASSERT, "assert1"),
                new ConsoleEntry(ASSERT, "assert2"),
                new ConsoleEntry(WARNING, "warning1"),
                new ConsoleEntry(WARNING, "warning2"),
                new ConsoleEntry(LOG, "log1"),
                new ConsoleEntry(LOG, "log2"),
                new ConsoleEntry(EXCEPTION, "exception1"),
                new ConsoleEntry(EXCEPTION, "exception2"));

        assertTrue(!list.isFiltering());

        assertTrue(list.setFilterByLogType(ERROR, true));
        listAssertMessages(list,
            "assert1", "assert2",
            "warning1", "warning2",
            "log1", "log2",
            "exception1", "exception2");
        assertTrue(list.isFiltering());

        assertTrue(!list.setFilterByLogType(ERROR, true));
        listAssertMessages(list,
            "assert1", "assert2",
            "warning1", "warning2",
            "log1", "log2",
            "exception1", "exception2");
        assertTrue(list.isFiltering());

        assertTrue(list.setFilterByLogType(ASSERT, true));
        listAssertMessages(list,
            "warning1", "warning2",
            "log1", "log2",
            "exception1", "exception2");
        assertTrue(list.isFiltering());

        assertTrue(list.setFilterByLogType(WARNING, true));
        listAssertMessages(list,
            "log1", "log2",
            "exception1", "exception2");
        assertTrue(list.isFiltering());

        assertTrue(list.setFilterByLogType(LOG, true));
        listAssertMessages(list,
            "exception1", "exception2");
        assertTrue(list.isFiltering());

        assertTrue(list.setFilterByLogType(EXCEPTION, true));
        listAssertMessages(list);
        assertTrue(list.isFiltering());

        assertTrue(list.setFilterByLogType(EXCEPTION, false));
        listAssertMessages(list,
            "exception1", "exception2");
        assertTrue(list.isFiltering());

        assertTrue(list.setFilterByLogType(LOG, false));
        listAssertMessages(list,
            "log1", "log2",
            "exception1", "exception2");
        assertTrue(list.isFiltering());

        assertTrue(list.setFilterByLogType(WARNING, false));
        listAssertMessages(list,
            "warning1", "warning2",
            "log1", "log2",
            "exception1", "exception2");
        assertTrue(list.isFiltering());

        assertTrue(list.setFilterByLogType(ASSERT, false));
        listAssertMessages(list,
            "assert1", "assert2",
            "warning1", "warning2",
            "log1", "log2",
            "exception1", "exception2");
        assertTrue(list.isFiltering());

        assertTrue(!list.setFilterByLogType(ASSERT, false));
        listAssertMessages(list,
            "assert1", "assert2",
            "warning1", "warning2",
            "log1", "log2",
            "exception1", "exception2");
        assertTrue(list.isFiltering());

        assertTrue(list.setFilterByLogType(ERROR, false));
        listAssertMessages(list,
            "error1", "error2",
            "assert1", "assert2",
            "warning1", "warning2",
            "log1", "log2",
            "exception1", "exception2");
        assertTrue(!list.isFiltering());
    }

    public void testFilteringByLogTypeMask()
    {
        ConsoleEntryList list = createEntryListWithEntries(
                new ConsoleEntry(ERROR, "error1"),
                new ConsoleEntry(ERROR, "error2"),
                new ConsoleEntry(ASSERT, "assert1"),
                new ConsoleEntry(ASSERT, "assert2"),
                new ConsoleEntry(WARNING, "warning1"),
                new ConsoleEntry(WARNING, "warning2"),
                new ConsoleEntry(LOG, "log1"),
                new ConsoleEntry(LOG, "log2"),
                new ConsoleEntry(EXCEPTION, "exception1"),
                new ConsoleEntry(EXCEPTION, "exception2"));

        assertTrue(!list.isFiltering());

        int mask = getMask(ERROR) |
                   getMask(EXCEPTION) |
                   getMask(ASSERT);

        assertTrue(list.setFilterByLogTypeMask(mask, true));
        listAssertMessages(list,
            "warning1", "warning2",
            "log1", "log2");
        assertTrue(list.isFiltering());

        mask = getMask(ERROR) |
               getMask(ASSERT);

        assertTrue(list.setFilterByLogTypeMask(mask, false));
        listAssertMessages(list,
            "error1", "error2",
            "assert1", "assert2",
            "warning1", "warning2",
            "log1", "log2");
        assertTrue(list.isFiltering());

        assertTrue(!list.setFilterByLogTypeMask(mask, false));
        listAssertMessages(list,
            "error1", "error2",
            "assert1", "assert2",
            "warning1", "warning2",
            "log1", "log2");
        assertTrue(list.isFiltering());

        mask = getMask(ERROR) |
                getMask(EXCEPTION) |
                getMask(ASSERT);

        assertTrue(list.setFilterByLogTypeMask(mask, false));
        listAssertMessages(list,
            "error1", "error2",
            "assert1", "assert2",
            "warning1", "warning2",
            "log1", "log2",
            "exception1", "exception2");
        assertTrue(!list.isFiltering());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Helpers

    private void listAssertMessages(ConsoleEntryList list, String... expected)
    {
        assertEquals(expected.length, list.count());
        for (int i = 0; i < expected.length; ++i)
        {
            ConsoleEntry entry = list.getEntry(i);
            assertEquals(expected[i], entry.message);
        }
    }

    private ConsoleEntryList createEntryListWithMessages(String... messages)
    {
        ConsoleEntryList list = new ConsoleEntryList(100);
        for (String message : messages)
        {
            list.addEntry(new ConsoleEntry(LOG, message));
        }
        return list;
    }

    private ConsoleEntryList createEntryListWithEntries(ConsoleEntry... entries)
    {
        ConsoleEntryList list = new ConsoleEntryList(100);
        for (ConsoleEntry entry : entries)
        {
            list.addEntry(entry);
        }
        return list;
    }
}
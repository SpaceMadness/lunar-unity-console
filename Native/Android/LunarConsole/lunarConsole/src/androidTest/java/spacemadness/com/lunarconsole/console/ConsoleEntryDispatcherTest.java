package spacemadness.com.lunarconsole.console;

import java.util.List;

import spacemadness.com.lunarconsole.TestCaseEx;

public class ConsoleEntryDispatcherTest extends TestCaseEx implements
        ConsoleEntryDispatcher.OnDispatchListener
{
    public void testAddEntries() throws Exception
    {
        MockConsoleEntryDispatcher dispatcher = new MockConsoleEntryDispatcher();
        dispatcher.add("1");
        dispatcher.add("2");
        dispatcher.add("3");

        assertResult(); // no items should be dispatched yet

        dispatcher.runDispatch();
        assertResult("1", "2", "3");

        dispatcher.runDispatch();
        assertResult();

        dispatcher.add("4");
        dispatcher.add("5");
        dispatcher.add("6");

        assertResult(); // no items should be dispatched

        dispatcher.runDispatch();
        assertResult("4", "5", "6");

        dispatcher.runDispatch();
        assertResult();
    }

    public void testRemoveEntries() throws Exception
    {
        MockConsoleEntryDispatcher dispatcher = new MockConsoleEntryDispatcher();
        dispatcher.add("1");
        dispatcher.add("2");
        dispatcher.add("3");

        dispatcher.cancelAll();
        assertResult();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ConsoleEntryDispatcher.OnDispatchListener

    @Override
    public void onDispatchEntries(List<ConsoleEntry> entries)
    {
        for (int i = 0; i < entries.size(); ++i)
        {
            addResult(entries.get(i).message);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Helpers

    @Override
    protected void assertResult(String... expected)
    {
        super.assertResult(expected);
        clearResult();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Mocks

    private class MockConsoleEntryDispatcher extends ConsoleEntryDispatcher
    {
        public MockConsoleEntryDispatcher()
        {
            super(ConsoleEntryDispatcherTest.this);
        }

        public void add(String message)
        {
            add(new ConsoleEntry((byte) 0, message));
        }

        public void runDispatch()
        {
            dispatchEntries();
        }

        @Override
        protected void postEntriesDispatch()
        {
        }

        @Override
        protected void cancelEntriesDispatch()
        {
        }
    }
}
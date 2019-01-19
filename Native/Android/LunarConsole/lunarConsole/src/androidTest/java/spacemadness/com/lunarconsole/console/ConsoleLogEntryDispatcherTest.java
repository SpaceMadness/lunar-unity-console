//
//  ConsoleLogEntryDispatcherTest.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2019 Alex Lementuev, SpaceMadness.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

package spacemadness.com.lunarconsole.console;

import java.util.List;

import spacemadness.com.lunarconsole.TestCaseEx;

public class ConsoleLogEntryDispatcherTest extends TestCaseEx implements
        ConsoleLogEntryDispatcher.OnDispatchListener
{
    public void testAddEntries() throws Exception
    {
        MockConsoleLogEntryDispatcher dispatcher = new MockConsoleLogEntryDispatcher();
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
        MockConsoleLogEntryDispatcher dispatcher = new MockConsoleLogEntryDispatcher();
        dispatcher.add("1");
        dispatcher.add("2");
        dispatcher.add("3");

        dispatcher.cancelAll();
        assertResult();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ConsoleLogEntryDispatcher.OnDispatchListener

    @Override
    public void onDispatchEntries(List<ConsoleLogEntry> entries)
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

    private class MockConsoleLogEntryDispatcher extends ConsoleLogEntryDispatcher
    {
        public MockConsoleLogEntryDispatcher()
        {
            super(ConsoleLogEntryDispatcherTest.this);
        }

        public void add(String message)
        {
            add(new ConsoleLogEntry((byte) 0, message));
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
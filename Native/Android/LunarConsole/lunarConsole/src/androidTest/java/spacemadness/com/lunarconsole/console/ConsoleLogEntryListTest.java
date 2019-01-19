//
//  ConsoleLogEntryListTest.java
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

import junit.framework.TestCase;

import static spacemadness.com.lunarconsole.console.ConsoleLogType.*;

public class ConsoleLogEntryListTest extends TestCase
{
    private static final int kDefaultCapacity = 100;
    private static final int kDefaultTrim = 1;

    public void testFilteringByText()
    {
        ConsoleLogEntryList list = createEntryListWithMessages(
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
        ConsoleLogEntryList list = createEntryListWithEntries(
                new ConsoleLogEntry(ERROR, "error1"),
                new ConsoleLogEntry(ERROR, "error2"),
                new ConsoleLogEntry(ASSERT, "assert1"),
                new ConsoleLogEntry(ASSERT, "assert2"),
                new ConsoleLogEntry(WARNING, "warning1"),
                new ConsoleLogEntry(WARNING, "warning2"),
                new ConsoleLogEntry(LOG, "log1"),
                new ConsoleLogEntry(LOG, "log2"),
                new ConsoleLogEntry(EXCEPTION, "exception1"),
                new ConsoleLogEntry(EXCEPTION, "exception2"));

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
        ConsoleLogEntryList list = createEntryListWithEntries(
                new ConsoleLogEntry(ERROR, "error1"),
                new ConsoleLogEntry(ERROR, "error2"),
                new ConsoleLogEntry(ASSERT, "assert1"),
                new ConsoleLogEntry(ASSERT, "assert2"),
                new ConsoleLogEntry(WARNING, "warning1"),
                new ConsoleLogEntry(WARNING, "warning2"),
                new ConsoleLogEntry(LOG, "log1"),
                new ConsoleLogEntry(LOG, "log2"),
                new ConsoleLogEntry(EXCEPTION, "exception1"),
                new ConsoleLogEntry(EXCEPTION, "exception2"));

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
    // Collapse items

    public void testCollapseEntries()
    {
        ConsoleLogEntryList list = createEntryListWithEntries(kDefaultCapacity, kDefaultTrim,
        new ConsoleLogEntry("message1"),
        new ConsoleLogEntry("message1"),
        new ConsoleLogEntry("message1"),
        new ConsoleLogEntry("message12"),
        new ConsoleLogEntry("message12"));

        list.collapsed(true);
        assertTrue(list.isFiltering());

        listAssertMessages(list, "message1", "message12");

        assertEntry(list, 0, "message1", 3);
        assertEntry(list, 1, "message12", 2);

        list.collapsed(false);
        assertTrue(!list.isFiltering());

        listAssertMessages(list, "message1", "message1", "message1", "message12", "message12");
    }

    public void testCollapseAddEntries()
    {
        ConsoleLogEntryList list = createEntryListWithEntries(kDefaultCapacity, kDefaultTrim,
            new ConsoleLogEntry("message1"),
            new ConsoleLogEntry("message1"),
            new ConsoleLogEntry("message1"));

        list.collapsed(true);
        assertTrue(list.isFiltering());

        listAssertMessages(list, "message1");
        assertEntry(list, 0, "message1", 3);

        list.addEntry(new ConsoleLogEntry("message1"));
        listAssertMessages(list, "message1");
        assertEntry(list, 0, "message1", 4);

        list.addEntry(new ConsoleLogEntry("message12"));
        listAssertMessages(list, "message1", "message12");
        assertEntry(list, 0, "message1", 4);
        assertEntry(list, 1, "message12", 1);

        list.addEntry(new ConsoleLogEntry("message2"));
        listAssertMessages(list, "message1", "message12", "message2");
        assertEntry(list, 0, "message1", 4);
        assertEntry(list, 1, "message12", 1);
        assertEntry(list, 2, "message2", 1);

        list.addEntry(new ConsoleLogEntry("message1"));
        listAssertMessages(list, "message1", "message12", "message2");
        assertEntry(list, 0, "message1", 5);
        assertEntry(list, 1, "message12", 1);
        assertEntry(list, 2, "message2", 1);

        list.addEntry(new ConsoleLogEntry("message12"));
        listAssertMessages(list, "message1", "message12", "message2");
        assertEntry(list, 0, "message1", 5);
        assertEntry(list, 1, "message12", 2);
        assertEntry(list, 2, "message2", 1);

        list.addEntry(new ConsoleLogEntry("message2"));
        listAssertMessages(list, "message1", "message12", "message2");
        assertEntry(list, 0, "message1", 5);
        assertEntry(list, 1, "message12", 2);
        assertEntry(list, 2, "message2", 2);

        list.collapsed(false);
        assertTrue(!list.isFiltering());

        listAssertMessages(list,
         "message1",
         "message1",
         "message1",
         "message1",
         "message12",
         "message2",
         "message1",
         "message12",
         "message2");
    }

    public void testCollapseAddEntriesOverflow()
    {
        ConsoleLogEntryList list = createEntryListWithEntries(3, 1,
          new ConsoleLogEntry("message1"),
          new ConsoleLogEntry("message1"),
          new ConsoleLogEntry("message1"));

        list.collapsed(true);
        assertTrue(list.isFiltering());

        list.addEntry(new ConsoleLogEntry("message1"));
        list.addEntry(new ConsoleLogEntry("message1"));
        list.addEntry(new ConsoleLogEntry("message1"));

        listAssertMessages(list, "message1");

        assertEntry(list, 0, "message1", 6);

        list.collapsed(false);
        assertTrue(!list.isFiltering());

        listAssertMessages(list, "message1", "message1", "message1");
    }

    public void testCollapseAddEntriesOverflowDistinctive()
    {
        ConsoleLogEntryList list = createEntryListWithEntries(3, 1,
          new ConsoleLogEntry("message1"),
          new ConsoleLogEntry("message1"),
          new ConsoleLogEntry("message1"));

        list.collapsed(true);
        assertTrue(list.isFiltering());

        list.addEntry(new ConsoleLogEntry("message12"));
        list.addEntry(new ConsoleLogEntry("message12"));

        listAssertMessages(list, "message1", "message12");

        assertEntry(list, 0, "message1", 3);
        assertEntry(list, 1, "message12", 2);

        list.collapsed(false);
        assertTrue(!list.isFiltering());

        listAssertMessages(list, "message1", "message12", "message12");
    }

    public void testCollapseFilteredEntries()
    {
        ConsoleLogEntryList list = createEntryListWithEntries(kDefaultCapacity, kDefaultTrim,
          new ConsoleLogEntry("message1"),
          new ConsoleLogEntry("message12"),
          new ConsoleLogEntry("message2"),
          new ConsoleLogEntry("message1"),
          new ConsoleLogEntry("message12"),
          new ConsoleLogEntry("message2"),
          new ConsoleLogEntry("message1"),
          new ConsoleLogEntry("message12"),
          new ConsoleLogEntry("message2"));

        list.setFilterByText("message1");
        assertTrue(list.isFiltering());

        list.collapsed(true);
        assertTrue(list.isFiltering());

        listAssertMessages(list, "message1", "message12");

        assertEntry(list, 0, "message1", 3);
        assertEntry(list, 1, "message12", 3);

        list.collapsed(false);
        assertTrue(list.isFiltering());

        listAssertMessages(list, "message1", "message12", "message1", "message12", "message1", "message12");
    }

    public void testCollapseAddFilteredEntries()
    {
        ConsoleLogEntryList list = createEntryListWithEntries(kDefaultCapacity, kDefaultTrim,
          new ConsoleLogEntry("message1"),
          new ConsoleLogEntry("message1"),
          new ConsoleLogEntry("message1"));

        list.setFilterByText("message1");
        assertTrue(list.isFiltering());

        list.collapsed(true);
        assertTrue(list.isFiltering());

        listAssertMessages(list, "message1");
        assertEntry(list, 0, "message1", 3);

        list.addEntry(new ConsoleLogEntry("message1"));
        listAssertMessages(list, "message1");
        assertEntry(list, 0, "message1", 4);

        list.addEntry(new ConsoleLogEntry("message12"));
        listAssertMessages(list, "message1", "message12");
        assertEntry(list, 0, "message1", 4);
        assertEntry(list, 1, "message12", 1);

        list.addEntry(new ConsoleLogEntry("message2"));
        listAssertMessages(list, "message1", "message12");
        assertEntry(list, 0, "message1", 4);
        assertEntry(list, 1, "message12", 1);

        list.addEntry(new ConsoleLogEntry("message1"));
        listAssertMessages(list, "message1", "message12");
        assertEntry(list, 0, "message1", 5);
        assertEntry(list, 1, "message12", 1);

        list.addEntry(new ConsoleLogEntry("message12"));
        listAssertMessages(list, "message1", "message12");
        assertEntry(list, 0, "message1", 5);
        assertEntry(list, 1, "message12", 2);

        list.addEntry(new ConsoleLogEntry("message2"));
        listAssertMessages(list, "message1", "message12");
        assertEntry(list, 0, "message1", 5);
        assertEntry(list, 1, "message12", 2);

        list.collapsed(false);
        assertTrue(list.isFiltering());

        listAssertMessages(list,
         "message1",
         "message1",
         "message1",
         "message1",
         "message12",
         "message1",
         "message12");

        list.setFilterByText("");
        assertTrue(!list.isFiltering());

        listAssertMessages(list,
         "message1",
         "message1",
         "message1",
         "message1",
         "message12",
         "message2",
         "message1",
         "message12",
         "message2");
    }

    public void testCollapseAddFilteredEntriesOverflow()
    {
        ConsoleLogEntryList list = createEntryListWithEntries(3, 1,
          new ConsoleLogEntry("message1"),
          new ConsoleLogEntry("message1"),
          new ConsoleLogEntry("message1"));

        list.setFilterByText("message1");
        assertTrue(list.isFiltering());

        list.collapsed(true);
        assertTrue(list.isFiltering());

        listAssertMessages(list, "message1");
        assertEntry(list, 0, "message1", 3);

        list.addEntry(new ConsoleLogEntry("message1"));
        listAssertMessages(list, "message1");
        assertEntry(list, 0, "message1", 4);

        list.addEntry(new ConsoleLogEntry("message12"));
        listAssertMessages(list, "message1", "message12");
        assertEntry(list, 0, "message1", 4);
        assertEntry(list, 1, "message12", 1);

        list.addEntry(new ConsoleLogEntry("message2"));
        listAssertMessages(list, "message1", "message12");
        assertEntry(list, 0, "message1", 4);
        assertEntry(list, 1, "message12", 1);

        list.addEntry(new ConsoleLogEntry("message1"));
        listAssertMessages(list, "message1", "message12");
        assertEntry(list, 0, "message1", 5);
        assertEntry(list, 1, "message12", 1);

        list.addEntry(new ConsoleLogEntry("message12"));
        listAssertMessages(list, "message1", "message12");
        assertEntry(list, 0, "message1", 5);
        assertEntry(list, 1, "message12", 2);

        list.addEntry(new ConsoleLogEntry("message2"));
        listAssertMessages(list, "message1", "message12");
        assertEntry(list, 0, "message1", 5);
        assertEntry(list, 1, "message12", 2);

        list.collapsed(false);
        assertTrue(list.isFiltering());

        listAssertMessages(list,
         "message1",
         "message12");

        list.setFilterByText("");
        assertTrue(!list.isFiltering());

        listAssertMessages(list,
         "message1",
         "message12",
         "message2");
    }

    public void testFilterCollapsedEntries()
    {
        ConsoleLogEntryList list = createEntryListWithEntries(kDefaultCapacity, kDefaultTrim,
          new ConsoleLogEntry("message1"),
          new ConsoleLogEntry("message12"),
          new ConsoleLogEntry("message2"),
          new ConsoleLogEntry("message1"),
          new ConsoleLogEntry("message12"),
          new ConsoleLogEntry("message2"),
          new ConsoleLogEntry("message1"),
          new ConsoleLogEntry("message12"),
          new ConsoleLogEntry("message2"));

        list.collapsed(true);
        assertTrue(list.isFiltering());

        list.setFilterByText("message1");
        assertTrue(list.isFiltering());

        listAssertMessages(list, "message1", "message12");

        assertEntry(list, 0, "message1", 3);
        assertEntry(list, 1, "message12", 3);

        list.setFilterByText("");
        assertTrue(list.isFiltering());

        listAssertMessages(list, "message1", "message12", "message2");

        assertEntry(list, 0, "message1", 3);
        assertEntry(list, 1, "message12", 3);
        assertEntry(list, 2, "message2", 3);

        list.collapsed(false);
        assertTrue(!list.isFiltering());

        listAssertMessages(list,
         "message1",
         "message12",
         "message2",
         "message1",
         "message12",
         "message2",
         "message1",
         "message12",
         "message2");
    }

    public void testFilterCollapsedEntriesAndAddEntries()
    {
        ConsoleLogEntryList list = createEntryListWithEntries(kDefaultCapacity, kDefaultTrim,
          new ConsoleLogEntry("message1"),
          new ConsoleLogEntry("message1"),
          new ConsoleLogEntry("message1"));

        list.collapsed(true);
        assertTrue(list.isFiltering());

        list.setFilterByText("message1");
        assertTrue(list.isFiltering());

        listAssertMessages(list, "message1");
        assertEntry(list, 0, "message1", 3);

        list.addEntry(new ConsoleLogEntry("message1"));
        listAssertMessages(list, "message1");
        assertEntry(list, 0, "message1", 4);

        list.addEntry(new ConsoleLogEntry("message12"));
        listAssertMessages(list, "message1", "message12");
        assertEntry(list, 0, "message1", 4);
        assertEntry(list, 1, "message12", 1);

        list.addEntry(new ConsoleLogEntry("message2"));
        listAssertMessages(list, "message1", "message12");
        assertEntry(list, 0, "message1", 4);
        assertEntry(list, 1, "message12", 1);

        list.addEntry(new ConsoleLogEntry("message1"));
        listAssertMessages(list, "message1", "message12");
        assertEntry(list, 0, "message1", 5);
        assertEntry(list, 1, "message12", 1);

        list.addEntry(new ConsoleLogEntry("message12"));
        listAssertMessages(list, "message1", "message12");
        assertEntry(list, 0, "message1", 5);
        assertEntry(list, 1, "message12", 2);

        list.addEntry(new ConsoleLogEntry("message2"));
        listAssertMessages(list, "message1", "message12");
        assertEntry(list, 0, "message1", 5);
        assertEntry(list, 1, "message12", 2);

        list.setFilterByText("");
        assertTrue(list.isFiltering());

        listAssertMessages(list, "message1", "message12", "message2");
        assertEntry(list, 0, "message1", 5);
        assertEntry(list, 1, "message12", 2);
        assertEntry(list, 2, "message2", 2);

        list.collapsed(false);
        assertTrue(!list.isFiltering());

        listAssertMessages(list,
         "message1",
         "message1",
         "message1",
         "message1",
         "message12",
         "message2",
         "message1",
         "message12",
         "message2");
    }

    public void testFilterCollapsedEntriesAndAddEntriesOverflow()
    {
        ConsoleLogEntryList list = createEntryListWithEntries(3, 1,
          new ConsoleLogEntry("message1"),
          new ConsoleLogEntry("message1"),
          new ConsoleLogEntry("message1"));

        list.collapsed(true);
        assertTrue(list.isFiltering());

        list.setFilterByText("message1");
        assertTrue(list.isFiltering());

        listAssertMessages(list, "message1");
        assertEntry(list, 0, "message1", 3);

        list.addEntry(new ConsoleLogEntry("message1"));
        listAssertMessages(list, "message1");
        assertEntry(list, 0, "message1", 4);

        list.addEntry(new ConsoleLogEntry("message12"));
        listAssertMessages(list, "message1", "message12");
        assertEntry(list, 0, "message1", 4);
        assertEntry(list, 1, "message12", 1);

        list.addEntry(new ConsoleLogEntry("message2"));
        listAssertMessages(list, "message1", "message12");
        assertEntry(list, 0, "message1", 4);
        assertEntry(list, 1, "message12", 1);

        list.addEntry(new ConsoleLogEntry("message1"));
        listAssertMessages(list, "message1", "message12");
        assertEntry(list, 0, "message1", 5);
        assertEntry(list, 1, "message12", 1);

        list.addEntry(new ConsoleLogEntry("message12"));
        listAssertMessages(list, "message1", "message12");
        assertEntry(list, 0, "message1", 5);
        assertEntry(list, 1, "message12", 2);

        list.addEntry(new ConsoleLogEntry("message2"));
        listAssertMessages(list, "message1", "message12");
        assertEntry(list, 0, "message1", 5);
        assertEntry(list, 1, "message12", 2);

        list.setFilterByText("");
        assertTrue(list.isFiltering());

        listAssertMessages(list, "message1", "message12", "message2");
        assertEntry(list, 0, "message1", 1);
        assertEntry(list, 1, "message12", 1);
        assertEntry(list, 1, "message12", 1);

        list.collapsed(false);
        assertTrue(!list.isFiltering());

        listAssertMessages(list, "message1", "message12", "message2");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Helpers

    private void listAssertMessages(ConsoleLogEntryList list, String... expected)
    {
        assertEquals(expected.length, list.count());
        for (int i = 0; i < expected.length; ++i)
        {
            ConsoleLogEntry entry = list.getEntry(i);
            assertEquals(expected[i], entry.message);
        }
    }

    private ConsoleLogEntryList createEntryListWithMessages(String... messages)
    {
        ConsoleLogEntryList list = new ConsoleLogEntryList(100, 1);
        for (String message : messages)
        {
            list.addEntry(new ConsoleLogEntry(LOG, message));
        }
        return list;
    }

    private ConsoleLogEntryList createEntryListWithEntries(ConsoleLogEntry... entries)
    {
        return createEntryListWithEntries(kDefaultCapacity, kDefaultTrim, entries);
    }

    private ConsoleLogEntryList createEntryListWithEntries(int capacity, int trimSize, ConsoleLogEntry... entries)
    {
        ConsoleLogEntryList list = new ConsoleLogEntryList(capacity, trimSize);
        for (ConsoleLogEntry entry : entries)
        {
            list.addEntry(entry);
        }
        return list;
    }

    private void assertEntry(ConsoleLogEntryList list, int index, String message, int count)
    {
        assertEntry(list, index, message, count, index);
    }

    private void assertEntry(ConsoleLogEntryList list, int index, String expectedMessage, int expectedCount, int expectedIndex)
    {
        ConsoleCollapsedLogEntry entry = list.getCollapsedEntry(index);
        assertEquals(expectedMessage, entry.message);
        assertEquals(expectedCount, entry.count);
        assertEquals(expectedIndex, entry.index);
    }
}
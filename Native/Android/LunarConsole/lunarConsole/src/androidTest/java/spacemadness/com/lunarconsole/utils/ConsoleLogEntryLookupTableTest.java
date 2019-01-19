//
//  ConsoleLogEntryLookupTableTest.java
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

package spacemadness.com.lunarconsole.utils;

import junit.framework.TestCase;

import spacemadness.com.lunarconsole.console.ConsoleCollapsedLogEntry;
import spacemadness.com.lunarconsole.console.ConsoleLogEntry;
import spacemadness.com.lunarconsole.console.ConsoleLogType;

public class ConsoleLogEntryLookupTableTest extends TestCase
{
    private ConsoleLogEntryLookupTable table;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        table = new ConsoleLogEntryLookupTable();
    }

    public void testAddEntry()
    {
        ConsoleCollapsedLogEntry entry = addEntryMessage("message1");
        assertEquals(1, entry.count);

        entry = addEntryMessage("message1");
        assertEquals(2, entry.count);

        entry = addEntryMessage("message2");
        assertEquals(1, entry.count);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Helpers

    private ConsoleCollapsedLogEntry addEntryMessage(String message)
    {
        return addEntryType(ConsoleLogType.LOG, message, "");
    }

    private ConsoleCollapsedLogEntry addEntryType(byte type, String message, String stackTrace)
    {
        ConsoleLogEntry entry = new ConsoleLogEntry(type, message, stackTrace);
        return table.addEntry(entry);
    }
}
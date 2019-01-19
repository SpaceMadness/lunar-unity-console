//
//  ConsoleLogEntryLookupTable.java
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

import java.util.HashMap;
import java.util.Map;

import spacemadness.com.lunarconsole.console.ConsoleCollapsedLogEntry;
import spacemadness.com.lunarconsole.console.ConsoleLogEntry;

// TODO: use trie data structure for a faster lookup
public class ConsoleLogEntryLookupTable
{
    private final Map<String, ConsoleCollapsedLogEntry> table;

    public ConsoleLogEntryLookupTable()
    {
        table = new HashMap<>();
    }

    public ConsoleCollapsedLogEntry addEntry(ConsoleLogEntry entry)
    {
        ConsoleCollapsedLogEntry collapsedEntry = table.get(entry.message);
        if (collapsedEntry == null)
        {
            collapsedEntry = new ConsoleCollapsedLogEntry(entry);
            table.put(collapsedEntry.message, collapsedEntry);
        }
        else
        {
            collapsedEntry.increaseCount();
        }

        return collapsedEntry;
    }

    public void removeEntry(ConsoleCollapsedLogEntry entry)
    {
        table.remove(entry.message);
    }

    public void clear()
    {
        table.clear();
    }
}

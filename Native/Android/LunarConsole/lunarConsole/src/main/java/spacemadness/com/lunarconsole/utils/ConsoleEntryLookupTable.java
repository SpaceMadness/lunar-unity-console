//
//  ConsoleEntryLookupTable.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2016 Alex Lementuev, SpaceMadness.
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

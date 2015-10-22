//
//  ConsoleEntryList.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2015 Alex Lementuev, SpaceMadness.
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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.*;
import static spacemadness.com.lunarconsole.utils.StringUtils.*;
import static spacemadness.com.lunarconsole.console.ConsoleLogType.*;

public class ConsoleEntryList
{
    /** Stores all entries */
    @NonNull
    private final LimitSizeEntryList entries;

    /** Stores only filtered entries */
    @Nullable
    private LimitSizeEntryList filteredEntries;

    /** Holds a reference to current entries list */
    @NonNull
    private LimitSizeEntryList currentEntries;

    /** Current filtering text (can be null) */
    @Nullable
    private String filterText;

    /** Holds disabled log entries types bit mask */
    private int logDisabledTypesMask;

    /** Total count of 'plain' log messages */
    private int logCount;

    /** Total count of 'warning' log messages */
    private int warningCount;

    /** Total count of 'error' log messages */
    private int errorCount;

    public ConsoleEntryList(int capacity, int trimSize)
    {
        entries = new LimitSizeEntryList(capacity, trimSize);
        currentEntries = entries;
        logDisabledTypesMask = 0;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Entries

    public boolean filterEntry(ConsoleEntry entry)
    {
        // filter
        if (isFiltering())
        {
            if (isFiltered(entry))
            {
                filteredEntries.addObject(entry);
                return true;
            }

            return false; // if item was rejected - we don't need to update table cells
        }

        return true;
    }

    public void addEntry(ConsoleEntry entry)
    {
        // add entry
        entries.addObject(entry);

        // count types
        int entryType = entry.type;
        if (entryType == LOG)
        {
            ++logCount;
        }
        else if (entryType == WARNING)
        {
            ++warningCount;
        }
        else if (isErrorType(entryType))
        {
            ++errorCount;
        }
    }

    public ConsoleEntry getEntry(int index)
    {
        return currentEntries.objectAtIndex(index);
    }

    public void trimHead(int count)
    {
        entries.trimHead(count);
    }

    public void clear()
    {
        entries.clear();
        if (filteredEntries != null)
        {
            filteredEntries.clear();
        }

        logCount = 0;
        warningCount = 0;
        errorCount = 0;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Filtering

    public boolean setFilterByText(String text)
    {
        if (!areEqual(filterText, text)) // filter text has changed
        {
            String oldFilterText = filterText;
            filterText = text;

            if (length(text) > length(oldFilterText) && (length(oldFilterText) == 0 || hasPrefix(text, oldFilterText))) // added more characters
            {
                return appendFilter();
            }

            return applyFilter();
        }

        return NO;
    }

    public boolean setFilterByLogType(int logType, boolean disabled)
    {
        return setFilterByLogTypeMask(getMask(logType), disabled);
    }

    public boolean setFilterByLogTypeMask(int logTypeMask, boolean disabled)
    {
        int oldDisabledTypesMask = logDisabledTypesMask;
        if (disabled)
        {
            logDisabledTypesMask |= logTypeMask;
        }
        else
        {
            logDisabledTypesMask &= ~logTypeMask;
        }

        if (oldDisabledTypesMask != logDisabledTypesMask)
        {
            return disabled ? appendFilter() : applyFilter();
        }

        return NO;
    }

    public boolean isFilterLogTypeEnabled(int type)
    {
        return (logDisabledTypesMask & getMask(type)) == 0;
    }

    private boolean appendFilter()
    {
        if (isFiltering())
        {
            useFilteredFromEntries(filteredEntries);
            return YES;
        }

        return applyFilter();
    }

    private boolean applyFilter()
    {
        boolean filtering = length(filterText) > 0 || hasLogTypeFilters(); // needs filtering?
        if (filtering)
        {
            useFilteredFromEntries(entries);
            return YES;
        }

        return removeFilter();
    }

    private boolean removeFilter()
    {
        if (isFiltering())
        {
            currentEntries = entries;
            filteredEntries = null;

            return YES;
        }

        return NO;
    }

    private void useFilteredFromEntries(LimitSizeEntryList entries)
    {
        LimitSizeEntryList filteredEntries = filterEntries(entries);

        // use filtered items
        this.currentEntries = filteredEntries;

        // store filtered items
        this.filteredEntries = filteredEntries;
    }

    private LimitSizeEntryList filterEntries(LimitSizeEntryList entries)
    {
        LimitSizeEntryList list = new LimitSizeEntryList(entries.capacity(), entries.getTrimSize()); // same as original list
        for (ConsoleEntry entry : entries)
        {
            if (isFiltered(entry))
            {
                list.addObject(entry);
            }
        }

        return list;
    }

    private boolean isFiltered(ConsoleEntry entry)
    {
        // filter by log type
        if ((logDisabledTypesMask & getMask(entry.type)) != 0)
        {
            return NO;
        }

        // filter by message
        return length(filterText) == 0 || containsIgnoreCase(entry.message, filterText);
    }

    private boolean hasLogTypeFilters()
    {
        return logDisabledTypesMask != 0;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Text representation

    public String getText()
    {
        StringBuilder text = new StringBuilder();

        int index = 0;
        int count = currentEntries.count();
        for (ConsoleEntry entry : currentEntries)
        {
            text.append(entry.message);
            if (++index < count)
            {
                text.append('\n');
            }
        }

        return text.toString();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    public int count() // FIXME: java naming conventions
    {
        return currentEntries.count();
    }

    public int totalCount() // FIXME: java naming conventions
    {
        return currentEntries.totalCount();
    }

    public int getLogCount()
    {
        return logCount;
    }

    public int getWarningCount()
    {
        return warningCount;
    }

    public int getErrorCount()
    {
        return errorCount;
    }

    @Nullable
    public String getFilterText()
    {
        return filterText;
    }

    public boolean isFiltering()
    {
        return filteredEntries != null;
    }

    public int overflowAmount() // FIXME: java naming conventions
    {
        return currentEntries.overflowCount();
    }

    public boolean isOverfloating()
    {
        return currentEntries.isOverfloating();
    }

    public int trimmedCount()
    {
        return currentEntries.trimmedCount();
    }

    public boolean isTrimmed()
    {
        return currentEntries.isTrimmed();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LimitSizeEntryList

    private static class LimitSizeEntryList extends LimitSizeList<ConsoleEntry>
    {
        public LimitSizeEntryList(int capacity, int trimSize)
        {
            super(ConsoleEntry.class, capacity, trimSize);
        }
    }
}

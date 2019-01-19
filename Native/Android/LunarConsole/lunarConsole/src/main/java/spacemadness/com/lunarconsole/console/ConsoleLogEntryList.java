//
//  ConsoleLogEntryList.java
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

import spacemadness.com.lunarconsole.debug.Assert;
import spacemadness.com.lunarconsole.utils.ConsoleLogEntryLookupTable;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.*;
import static spacemadness.com.lunarconsole.utils.StringUtils.*;
import static spacemadness.com.lunarconsole.console.ConsoleLogType.*;

/** A class which represents a console entry list.
 * Supports collapsing similar items and filtering by text and log type.
 */
public class ConsoleLogEntryList
{
    /** Stores all entries */
    private final LimitSizeEntryList entries;

    /** Stores only filtered entries */
    private LimitSizeEntryList filteredEntries;

    /** Holds a reference to current entries list */
    private LimitSizeEntryList currentEntries;

    /** Current filtering text (can be null) */
    private String filterText;

    /** Lookup table for collapsed entries (or null is entries are not collapsed) */
    private ConsoleLogEntryLookupTable entryLookup;

    /** Holds disabled log entries types bit mask */
    private int logDisabledTypesMask;

    /** Total count of 'plain' log messages */
    private int logCount;

    /** Total count of 'warning' log messages */
    private int warningCount;

    /** Total count of 'error' log messages */
    private int errorCount;

    /** True if similar entries are collapsed */
    private boolean collapsed;

    /**
     * Creates a new list based on capacity and trim size
     *
     * @param capacity the maximum amount of entries this list can store
     * @param trimSize the number of items trimmed when list overflows
     */
    public ConsoleLogEntryList(int capacity, int trimSize)
    {
        entries = new LimitSizeEntryList(capacity, trimSize);
        currentEntries = entries;
        logDisabledTypesMask = 0;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Entries

    /** Adds a new entry object */
    public int addEntry(ConsoleLogEntry entry)
    {
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

        // add entry
        entries.addObject(entry);

        // filter entry
        if (isFiltering())
        {
            if (filterEntry(entry))
            {
                if (collapsed)
                {
                    ConsoleCollapsedLogEntry collapsedEntry = entryLookup.addEntry(entry);
                    if (collapsedEntry.index < filteredEntries.trimmedCount()) // first encounter or trimmed?
                    {
                        collapsedEntry.index = filteredEntries.totalCount();   // we use total count in case if list overflows
                        filteredEntries.addObject(collapsedEntry);
                    }

                    return collapsedEntry.index - filteredEntries.trimmedCount();
                }

                filteredEntries.addObject(entry);
                return filteredEntries.totalCount() - 1;
            }

            return -1; // if item was rejected - we don't need to update table cells
        }

        return entries.totalCount() - 1; // entry was added at the end of the list
    }

    /** Returns entry at index */
    public ConsoleLogEntry getEntry(int index)
    {
        return currentEntries.objectAtIndex(index);
    }

    /** Return collapsed entry at index or null if entry is not collapsed */
    public ConsoleCollapsedLogEntry getCollapsedEntry(int index)
    {
        return as(getEntry(index), ConsoleCollapsedLogEntry.class);
    }

    /** Removes all entries from the list */
    public void clear()
    {
        entries.clear();

        if (filteredEntries != null)
        {
            filteredEntries.clear();
        }

        if (entryLookup != null)
        {
            entryLookup.clear();
        }

        logCount = 0;
        warningCount = 0;
        errorCount = 0;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Filtering

    /** Sets a text-based filtering
     *
     * @param text a new text to filter or "" to remove the text filter
     * @return true if list was changed
     */
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

        return false;
    }

    /**
     * Sets log type based filtering
     *
     * @param logType the target log type
     * @param disabled true if log type should be disabled
     * @return true if list was changed
     */
    public boolean setFilterByLogType(int logType, boolean disabled)
    {
        return setFilterByLogTypeMask(getMask(logType), disabled);
    }

    /**
     * Sets log type based filtering
     *
     * @param logTypeMask the target log type mask
     * @param disabled true if log type mask should be disabled
     * @return true if list was changed
     */
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

        return false;
    }

    /**
     * Checks if log type is enabled
     *
     * @param type the target log type
     * @return true if log type is enabled
     */
    public boolean isFilterLogTypeEnabled(int type)
    {
        return (logDisabledTypesMask & getMask(type)) == 0;
    }

    /**
     * Appends new filter to already filtered items
     * @return true if list was changed
     */
    private boolean appendFilter()
    {
        if (isFiltering())
        {
            useFilteredFromEntries(filteredEntries);
            return true;
        }

        return applyFilter();
    }

    /**
     * Replaces or removes the current filter
     * @return true if list was changed
     */
    private boolean applyFilter()
    {
        boolean needsFiltering = collapsed || length(filterText) > 0 || hasLogTypeFilters(); // needs filtering?
        if (needsFiltering)
        {
            if (entryLookup != null)
            {
                entryLookup.clear(); // if we have collapsed items - we need to rebuild the lookup
            }

            useFilteredFromEntries(entries);
            return true;
        }

        return removeFilter();
    }

    /**
     * Returns current filter
     * @return true if filter was removed
     */
    private boolean removeFilter()
    {
        if (isFiltering())
        {
            currentEntries = entries;
            filteredEntries = null;

            return true;
        }

        return false;
    }

    /**
     * Filter entries and set them as current entries
     *
     * @param entries entries to filter
     */
    private void useFilteredFromEntries(LimitSizeEntryList entries)
    {
        LimitSizeEntryList filteredEntries = filterEntries(entries);

        // use filtered items
        this.currentEntries = filteredEntries;

        // store filtered items
        this.filteredEntries = filteredEntries;
    }

    /**
     * Creates new list based by applying current filter to entries
     * @param entries entries to filter
     * @return new list
     */
    private LimitSizeEntryList filterEntries(LimitSizeEntryList entries)
    {
        LimitSizeEntryList list = new LimitSizeEntryList(entries.capacity(), entries.getTrimSize()); // same as original list

        if (collapsed)
        {
            for (ConsoleLogEntry entry : entries)
            {
                if (filterEntry(entry))
                {
                    ConsoleCollapsedLogEntry collapsedEntry = as(entry, ConsoleCollapsedLogEntry.class);
                    if (collapsedEntry != null)
                    {
                        collapsedEntry.index = list.totalCount(); // update item's position
                        list.addObject(collapsedEntry);
                    }
                    else
                    {
                        collapsedEntry= entryLookup.addEntry(entry);
                        if (collapsedEntry.count == 1) // first encounter
                        {
                            collapsedEntry.index = list.totalCount();
                            list.addObject(collapsedEntry);
                        }
                    }
                }
            }
        }
        else
        {
            for (ConsoleLogEntry entry : entries)
            {
                if (filterEntry(entry))
                {
                    list.addObject(entry);
                }
            }
        }

        return list;
    }

    /**
     * Checks if entry passes the current filter
     * @param entry entry to check
     * @return true if entry passes the filter
     */
    private boolean filterEntry(ConsoleLogEntry entry)
    {
        // filter by log type
        if ((logDisabledTypesMask & getMask(entry.type)) != 0)
        {
            return false;
        }

        // filter by message
        return length(filterText) == 0 || containsIgnoreCase(entry.message, filterText);
    }

    /**
     * Check if list is filtering by log type mask
     * @return true if list is filtering by log type mask
     */
    private boolean hasLogTypeFilters()
    {
        return logDisabledTypesMask != 0;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Text representation

    /**
     * Concatenates every message from all the entries into a single string
     */
    public String getText()
    {
        StringBuilder text = new StringBuilder();

        int index = 0;
        int count = currentEntries.count();
        for (ConsoleLogEntry entry : currentEntries)
        {
            text.append(entry.message);
            if (entry.type == ConsoleLogType.EXCEPTION && entry.hasStackTrace())
            {
                text.append('\n');
                text.append(entry.stackTrace);
            }

            if (++index < count)
            {
                text.append('\n');
            }
        }

        return text.toString();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    /**
     * Sets the boolean flag controlling similar entries collapse
     * @param collapsed <code>true</code> if similar entries should collapse
     */
    public void collapsed(boolean collapsed) // FIXME: java naming conventions
    {
        this.collapsed = collapsed;
        if (collapsed)
        {
            Assert.IsNull(entryLookup);
            entryLookup = new ConsoleLogEntryLookupTable();
        }
        else
        {
            entryLookup = null;
        }

        applyFilter();
    }

    /**
     * Returns true if similar entries are collapsed
     */
    public boolean isCollapsed()
    {
        return collapsed;
    }

    public int capacity() // FIXME: java naming conventions
    {
        return currentEntries.capacity();
    }

    public int count() // FIXME: java naming conventions
    {
        return currentEntries.count();
    }

    public int trimCount()
    {
        return currentEntries.getTrimSize();
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

    private static class LimitSizeEntryList extends LimitSizeList<ConsoleLogEntry>
    {
        public LimitSizeEntryList(int capacity, int trimSize)
        {
            super(ConsoleLogEntry.class, capacity, trimSize);
        }
    }
}

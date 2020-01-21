package spacemadness.com.lunarconsole.model.log

import org.junit.Assert.*
import org.junit.Test
import spacemadness.com.lunarconsole.console.ConsoleLogEntryList
import spacemadness.com.lunarconsole.console.LogEntry
import spacemadness.com.lunarconsole.console.LogEntryType
import spacemadness.com.lunarconsole.console.LogEntryType.*
import spacemadness.com.lunarconsole.console.getMask
import java.lang.AssertionError

class LogEntryListTest {
    //region Filtering

    @Test
    fun testFilteringByText() {
        val list = createEntryListWithMessages(
            "line1",
            "line11",
            "line111",
            "line1111",
            "foo"
        )

        listAssertMessages(list, "line1", "line11", "line111", "line1111", "foo")

        assertFalse(list.isFiltering)

        assertTrue(list.setFilterByText("l"))
        assertTrue(list.isFiltering)
        listAssertMessages(list, "line1", "line11", "line111", "line1111")

        assertFalse(list.setFilterByText("l")) // list should not get modified with the same filter
        assertTrue(list.isFiltering)
        listAssertMessages(list, "line1", "line11", "line111", "line1111")

        assertTrue(list.setFilterByText("li"))
        assertTrue(list.isFiltering)
        listAssertMessages(list, "line1", "line11", "line111", "line1111")

        assertTrue(list.setFilterByText("lin"))
        assertTrue(list.isFiltering)
        listAssertMessages(list, "line1", "line11", "line111", "line1111")

        assertTrue(list.setFilterByText("line"))
        assertTrue(list.isFiltering)
        listAssertMessages(list, "line1", "line11", "line111", "line1111")

        assertTrue(list.setFilterByText("line1"))
        assertTrue(list.isFiltering)
        listAssertMessages(list, "line1", "line11", "line111", "line1111")

        assertTrue(list.setFilterByText("line11"))
        assertTrue(list.isFiltering)
        listAssertMessages(list, "line11", "line111", "line1111")

        assertTrue(list.setFilterByText("line111"))
        assertTrue(list.isFiltering)
        listAssertMessages(list, "line111", "line1111")

        assertTrue(list.setFilterByText("line1111"))
        assertTrue(list.isFiltering)
        listAssertMessages(list, "line1111")

        assertTrue(list.setFilterByText("line11111"))
        assertTrue(list.isFiltering)
        listAssertMessages(list)

        assertTrue(list.setFilterByText("line1111"))
        assertTrue(list.isFiltering)
        listAssertMessages(list, "line1111")

        assertTrue(list.setFilterByText("line111"))
        assertTrue(list.isFiltering)
        listAssertMessages(list, "line111", "line1111")

        assertTrue(list.setFilterByText("line11"))
        assertTrue(list.isFiltering)
        listAssertMessages(list, "line11", "line111", "line1111")

        assertTrue(list.setFilterByText("line1"))
        assertTrue(list.isFiltering)
        listAssertMessages(list, "line1", "line11", "line111", "line1111")

        assertTrue(list.setFilterByText("line"))
        assertTrue(list.isFiltering)
        listAssertMessages(list, "line1", "line11", "line111", "line1111")

        assertTrue(list.setFilterByText("lin"))
        assertTrue(list.isFiltering)
        listAssertMessages(list, "line1", "line11", "line111", "line1111")

        assertTrue(list.setFilterByText("li"))
        assertTrue(list.isFiltering)
        listAssertMessages(list, "line1", "line11", "line111", "line1111")

        assertTrue(list.setFilterByText("l"))
        assertTrue(list.isFiltering)
        listAssertMessages(list, "line1", "line11", "line111", "line1111")

        assertTrue(list.setFilterByText(""))
        assertFalse(list.isFiltering)
        listAssertMessages(list, "line1", "line11", "line111", "line1111", "foo")
    }

    @Test
    fun testFilteringByLogType() {
        val list = createEntryListWithEntries(
            makeEntry(ERROR, "error1"),
            makeEntry(ERROR, "error2"),
            makeEntry(ASSERT, "assert1"),
            makeEntry(ASSERT, "assert2"),
            makeEntry(WARNING, "warning1"),
            makeEntry(WARNING, "warning2"),
            makeEntry(LOG, "log1"),
            makeEntry(LOG, "log2"),
            makeEntry(EXCEPTION, "exception1"),
            makeEntry(EXCEPTION, "exception2")
        )

        assertFalse(list.isFiltering)

        assertTrue(list.setFilterByLogType(ERROR, true))
        assertTrue(list.isFiltering)
        listAssertMessages(
            list,
            "assert1",
            "assert2",
            "warning1",
            "warning2",
            "log1",
            "log2",
            "exception1",
            "exception2"
        )

        assertFalse(list.setFilterByLogType(ERROR, true))
        assertTrue(list.isFiltering)
        listAssertMessages(
            list,
            "assert1",
            "assert2",
            "warning1",
            "warning2",
            "log1",
            "log2",
            "exception1",
            "exception2"
        )

        assertTrue(list.setFilterByLogType(ASSERT, true))
        assertTrue(list.isFiltering)
        listAssertMessages(list, "warning1", "warning2", "log1", "log2", "exception1", "exception2")

        assertTrue(list.setFilterByLogType(WARNING, true))
        assertTrue(list.isFiltering)
        listAssertMessages(list, "log1", "log2", "exception1", "exception2")

        assertTrue(list.setFilterByLogType(LOG, true))
        assertTrue(list.isFiltering)
        listAssertMessages(list, "exception1", "exception2")

        assertTrue(list.setFilterByLogType(EXCEPTION, true))
        assertTrue(list.isFiltering)
        listAssertMessages(list)

        assertTrue(list.setFilterByLogType(EXCEPTION, false))
        assertTrue(list.isFiltering)
        listAssertMessages(list, "exception1", "exception2")

        assertTrue(list.setFilterByLogType(LOG, false))
        assertTrue(list.isFiltering)
        listAssertMessages(list, "log1", "log2", "exception1", "exception2")

        assertTrue(list.setFilterByLogType(WARNING, false))
        assertTrue(list.isFiltering)
        listAssertMessages(list, "warning1", "warning2", "log1", "log2", "exception1", "exception2")

        assertTrue(list.setFilterByLogType(ASSERT, false))
        assertTrue(list.isFiltering)
        listAssertMessages(
            list,
            "assert1",
            "assert2",
            "warning1",
            "warning2",
            "log1",
            "log2",
            "exception1",
            "exception2"
        )

        assertFalse(list.setFilterByLogType(ASSERT, false))
        assertTrue(list.isFiltering)
        listAssertMessages(
            list,
            "assert1",
            "assert2",
            "warning1",
            "warning2",
            "log1",
            "log2",
            "exception1",
            "exception2"
        )

        assertTrue(list.setFilterByLogType(ERROR, false))
        assertFalse(list.isFiltering)
        listAssertMessages(
            list,
            "error1",
            "error2",
            "assert1",
            "assert2",
            "warning1",
            "warning2",
            "log1",
            "log2",
            "exception1",
            "exception2"
        )
    }

    @Test
    fun testFilteringByLogTypeMask() {
        val list = createEntryListWithEntries(
            makeEntry(ERROR, "error1"),
            makeEntry(ERROR, "error2"),
            makeEntry(ASSERT, "assert1"),
            makeEntry(ASSERT, "assert2"),
            makeEntry(WARNING, "warning1"),
            makeEntry(WARNING, "warning2"),
            makeEntry(LOG, "log1"),
            makeEntry(LOG, "log2"),
            makeEntry(EXCEPTION, "exception1"),
            makeEntry(EXCEPTION, "exception2")
        )

        assertFalse(list.isFiltering)

        var mask = getMask(ERROR) or getMask(EXCEPTION) or getMask(ASSERT)

        assertTrue(list.setFilterByLogTypeMask(mask, true))
        assertTrue(list.isFiltering)
        listAssertMessages(list, "warning1", "warning2", "log1", "log2")

        mask = getMask(ERROR) or getMask(ASSERT)

        assertTrue(list.setFilterByLogTypeMask(mask, false))
        assertTrue(list.isFiltering)
        listAssertMessages(
            list,
            "error1",
            "error2",
            "assert1",
            "assert2",
            "warning1",
            "warning2",
            "log1",
            "log2"
        )

        assertFalse(list.setFilterByLogTypeMask(mask, false))
        assertTrue(list.isFiltering)
        listAssertMessages(
            list,
            "error1",
            "error2",
            "assert1",
            "assert2",
            "warning1",
            "warning2",
            "log1",
            "log2"
        )

        mask = getMask(ERROR) or getMask(EXCEPTION) or getMask(ASSERT)

        assertTrue(list.setFilterByLogTypeMask(mask, false))
        assertFalse(list.isFiltering)
        listAssertMessages(
            list,
            "error1",
            "error2",
            "assert1",
            "assert2",
            "warning1",
            "warning2",
            "log1",
            "log2",
            "exception1",
            "exception2"
        )
    }

    //endregion

    //region Collapse items

    @Test
    fun testCollapseEntries() {
        val list = createEntryListWithEntries(
            kDefaultCapacity, kDefaultTrim,
            makeEntry("message1"),
            makeEntry("message1"),
            makeEntry("message1"),
            makeEntry("message12"),
            makeEntry("message12")
        )

        list.setCollapsed(true)
        assertTrue(list.isFiltering)

        listAssertMessages(list, "message1", "message12")

        assertEntry(list, 0, "message1", 3)
        assertEntry(list, 1, "message12", 2)

        list.setCollapsed(false)
        assertFalse(list.isFiltering)

        listAssertMessages(list, "message1", "message1", "message1", "message12", "message12")
    }

    @Test
    fun testCollapseAddEntries() {
        val list = createEntryListWithEntries(
            kDefaultCapacity, kDefaultTrim,
            makeEntry("message1"),
            makeEntry("message1"),
            makeEntry("message1")
        )

        list.setCollapsed(true)
        assertTrue(list.isFiltering)

        listAssertMessages(list, "message1")
        assertEntry(list, 0, "message1", 3)

        list.addEntry(makeEntry("message1"))
        listAssertMessages(list, "message1")
        assertEntry(list, 0, "message1", 4)

        list.addEntry(makeEntry("message12"))
        listAssertMessages(list, "message1", "message12")
        assertEntry(list, 0, "message1", 4)
        assertEntry(list, 1, "message12", 1)

        list.addEntry(makeEntry("message2"))
        listAssertMessages(list, "message1", "message12", "message2")
        assertEntry(list, 0, "message1", 4)
        assertEntry(list, 1, "message12", 1)
        assertEntry(list, 2, "message2", 1)

        list.addEntry(makeEntry("message1"))
        listAssertMessages(list, "message1", "message12", "message2")
        assertEntry(list, 0, "message1", 5)
        assertEntry(list, 1, "message12", 1)
        assertEntry(list, 2, "message2", 1)

        list.addEntry(makeEntry("message12"))
        listAssertMessages(list, "message1", "message12", "message2")
        assertEntry(list, 0, "message1", 5)
        assertEntry(list, 1, "message12", 2)
        assertEntry(list, 2, "message2", 1)

        list.addEntry(makeEntry("message2"))
        listAssertMessages(list, "message1", "message12", "message2")
        assertEntry(list, 0, "message1", 5)
        assertEntry(list, 1, "message12", 2)
        assertEntry(list, 2, "message2", 2)

        list.setCollapsed(false)
        assertFalse(list.isFiltering)

        listAssertMessages(
            list,
            "message1",
            "message1",
            "message1",
            "message1",
            "message12",
            "message2",
            "message1",
            "message12",
            "message2"
        )
    }

    @Test
    fun testCollapseAddEntriesOverflow() {
        val list = createEntryListWithEntries(
            3, 1,
            makeEntry("message1"),
            makeEntry("message1"),
            makeEntry("message1")
        )

        list.setCollapsed(true)
        assertTrue(list.isFiltering)

        list.addEntry(makeEntry("message1"))
        list.addEntry(makeEntry("message1"))
        list.addEntry(makeEntry("message1"))

        listAssertMessages(list, "message1")

        assertEntry(list, 0, "message1", 6)

        list.setCollapsed(false)
        assertFalse(list.isFiltering)

        listAssertMessages(list, "message1", "message1", "message1")
    }

    @Test
    fun testCollapseAddEntriesOverflowDistinctive() {
        val list = createEntryListWithEntries(
            3, 1,
            makeEntry("message1"),
            makeEntry("message1"),
            makeEntry("message1")
        )

        list.setCollapsed(true)
        assertTrue(list.isFiltering)

        list.addEntry(makeEntry("message12"))
        list.addEntry(makeEntry("message12"))

        listAssertMessages(list, "message1", "message12")

        assertEntry(list, 0, "message1", 3)
        assertEntry(list, 1, "message12", 2)

        list.setCollapsed(false)
        assertFalse(list.isFiltering)

        listAssertMessages(list, "message1", "message12", "message12")
    }

    @Test
    fun testCollapseFilteredEntries() {
        val list = createEntryListWithEntries(
            kDefaultCapacity, kDefaultTrim,
            makeEntry("message1"),
            makeEntry("message12"),
            makeEntry("message2"),
            makeEntry("message1"),
            makeEntry("message12"),
            makeEntry("message2"),
            makeEntry("message1"),
            makeEntry("message12"),
            makeEntry("message2")
        )

        list.setFilterByText("message1")
        assertTrue(list.isFiltering)

        list.setCollapsed(true)
        assertTrue(list.isFiltering)

        listAssertMessages(list, "message1", "message12")

        assertEntry(list, 0, "message1", 3)
        assertEntry(list, 1, "message12", 3)

        list.setCollapsed(false)
        assertTrue(list.isFiltering)

        listAssertMessages(
            list,
            "message1",
            "message12",
            "message1",
            "message12",
            "message1",
            "message12"
        )
    }

    @Test
    fun testCollapseAddFilteredEntries() {
        val list = createEntryListWithEntries(
            kDefaultCapacity, kDefaultTrim,
            makeEntry("message1"),
            makeEntry("message1"),
            makeEntry("message1")
        )

        list.setFilterByText("message1")
        assertTrue(list.isFiltering)

        list.setCollapsed(true)
        assertTrue(list.isFiltering)

        listAssertMessages(list, "message1")
        assertEntry(list, 0, "message1", 3)

        list.addEntry(makeEntry("message1"))
        listAssertMessages(list, "message1")
        assertEntry(list, 0, "message1", 4)

        list.addEntry(makeEntry("message12"))
        listAssertMessages(list, "message1", "message12")
        assertEntry(list, 0, "message1", 4)
        assertEntry(list, 1, "message12", 1)

        list.addEntry(makeEntry("message2"))
        listAssertMessages(list, "message1", "message12")
        assertEntry(list, 0, "message1", 4)
        assertEntry(list, 1, "message12", 1)

        list.addEntry(makeEntry("message1"))
        listAssertMessages(list, "message1", "message12")
        assertEntry(list, 0, "message1", 5)
        assertEntry(list, 1, "message12", 1)

        list.addEntry(makeEntry("message12"))
        listAssertMessages(list, "message1", "message12")
        assertEntry(list, 0, "message1", 5)
        assertEntry(list, 1, "message12", 2)

        list.addEntry(makeEntry("message2"))
        listAssertMessages(list, "message1", "message12")
        assertEntry(list, 0, "message1", 5)
        assertEntry(list, 1, "message12", 2)

        list.setCollapsed(false)
        assertTrue(list.isFiltering)

        listAssertMessages(
            list,
            "message1",
            "message1",
            "message1",
            "message1",
            "message12",
            "message1",
            "message12"
        )

        list.setFilterByText("")
        assertFalse(list.isFiltering)

        listAssertMessages(
            list,
            "message1",
            "message1",
            "message1",
            "message1",
            "message12",
            "message2",
            "message1",
            "message12",
            "message2"
        )
    }

    @Test
    fun testCollapseAddFilteredEntriesOverflow() {
        val list = createEntryListWithEntries(
            3, 1,
            makeEntry("message1"),
            makeEntry("message1"),
            makeEntry("message1")
        )

        list.setFilterByText("message1")
        assertTrue(list.isFiltering)

        list.setCollapsed(true)
        assertTrue(list.isFiltering)

        listAssertMessages(list, "message1")
        assertEntry(list, 0, "message1", 3)

        list.addEntry(makeEntry("message1"))
        listAssertMessages(list, "message1")
        assertEntry(list, 0, "message1", 4)

        list.addEntry(makeEntry("message12"))
        listAssertMessages(list, "message1", "message12")
        assertEntry(list, 0, "message1", 4)
        assertEntry(list, 1, "message12", 1)

        list.addEntry(makeEntry("message2"))
        listAssertMessages(list, "message1", "message12")
        assertEntry(list, 0, "message1", 4)
        assertEntry(list, 1, "message12", 1)

        list.addEntry(makeEntry("message1"))
        listAssertMessages(list, "message1", "message12")
        assertEntry(list, 0, "message1", 5)
        assertEntry(list, 1, "message12", 1)

        list.addEntry(makeEntry("message12"))
        listAssertMessages(list, "message1", "message12")
        assertEntry(list, 0, "message1", 5)
        assertEntry(list, 1, "message12", 2)

        list.addEntry(makeEntry("message2"))
        listAssertMessages(list, "message1", "message12")
        assertEntry(list, 0, "message1", 5)
        assertEntry(list, 1, "message12", 2)

        list.setCollapsed(false)
        assertTrue(list.isFiltering)

        listAssertMessages(
            list,
            "message1",
            "message12"
        )

        list.setFilterByText("")
        assertFalse(list.isFiltering)

        listAssertMessages(
            list,
            "message1",
            "message12",
            "message2"
        )
    }

    @Test
    fun testFilterCollapsedEntries() {
        val list = createEntryListWithEntries(
            kDefaultCapacity, kDefaultTrim,
            makeEntry("message1"),
            makeEntry("message12"),
            makeEntry("message2"),
            makeEntry("message1"),
            makeEntry("message12"),
            makeEntry("message2"),
            makeEntry("message1"),
            makeEntry("message12"),
            makeEntry("message2")
        )

        list.setCollapsed(true)
        assertTrue(list.isFiltering)

        list.setFilterByText("message1")
        assertTrue(list.isFiltering)

        listAssertMessages(list, "message1", "message12")

        assertEntry(list, 0, "message1", 3)
        assertEntry(list, 1, "message12", 3)

        list.setFilterByText("")
        assertTrue(list.isFiltering)

        listAssertMessages(list, "message1", "message12", "message2")

        assertEntry(list, 0, "message1", 3)
        assertEntry(list, 1, "message12", 3)
        assertEntry(list, 2, "message2", 3)

        list.setCollapsed(false)
        assertFalse(list.isFiltering)

        listAssertMessages(
            list,
            "message1",
            "message12",
            "message2",
            "message1",
            "message12",
            "message2",
            "message1",
            "message12",
            "message2"
        )
    }

    @Test
    fun testFilterCollapsedEntriesAndAddEntries() {
        val list = createEntryListWithEntries(
            kDefaultCapacity, kDefaultTrim,
            makeEntry("message1"),
            makeEntry("message1"),
            makeEntry("message1")
        )

        list.setCollapsed(true)
        assertTrue(list.isFiltering)

        list.setFilterByText("message1")
        assertTrue(list.isFiltering)

        listAssertMessages(list, "message1")
        assertEntry(list, 0, "message1", 3)

        list.addEntry(makeEntry("message1"))
        listAssertMessages(list, "message1")
        assertEntry(list, 0, "message1", 4)

        list.addEntry(makeEntry("message12"))
        listAssertMessages(list, "message1", "message12")
        assertEntry(list, 0, "message1", 4)
        assertEntry(list, 1, "message12", 1)

        list.addEntry(makeEntry("message2"))
        listAssertMessages(list, "message1", "message12")
        assertEntry(list, 0, "message1", 4)
        assertEntry(list, 1, "message12", 1)

        list.addEntry(makeEntry("message1"))
        listAssertMessages(list, "message1", "message12")
        assertEntry(list, 0, "message1", 5)
        assertEntry(list, 1, "message12", 1)

        list.addEntry(makeEntry("message12"))
        listAssertMessages(list, "message1", "message12")
        assertEntry(list, 0, "message1", 5)
        assertEntry(list, 1, "message12", 2)

        list.addEntry(makeEntry("message2"))
        listAssertMessages(list, "message1", "message12")
        assertEntry(list, 0, "message1", 5)
        assertEntry(list, 1, "message12", 2)

        list.setFilterByText("")
        assertTrue(list.isFiltering)

        listAssertMessages(list, "message1", "message12", "message2")
        assertEntry(list, 0, "message1", 5)
        assertEntry(list, 1, "message12", 2)
        assertEntry(list, 2, "message2", 2)

        list.setCollapsed(false)
        assertFalse(list.isFiltering)

        listAssertMessages(
            list,
            "message1",
            "message1",
            "message1",
            "message1",
            "message12",
            "message2",
            "message1",
            "message12",
            "message2"
        )
    }


    @Test
    fun testFilterCollapsedEntriesAndAddEntriesOverflow() {
        val list = createEntryListWithEntries(
            3, 1,
            makeEntry("message1"),
            makeEntry("message1"),
            makeEntry("message1")
        )

        list.setCollapsed(true)
        assertTrue(list.isFiltering)

        list.setFilterByText("message1")
        assertTrue(list.isFiltering)

        listAssertMessages(list, "message1")
        assertEntry(list, 0, "message1", 3)

        list.addEntry(makeEntry("message1"))
        listAssertMessages(list, "message1")
        assertEntry(list, 0, "message1", 4)

        list.addEntry(makeEntry("message12"))
        listAssertMessages(list, "message1", "message12")
        assertEntry(list, 0, "message1", 4)
        assertEntry(list, 1, "message12", 1)

        list.addEntry(makeEntry("message2"))
        listAssertMessages(list, "message1", "message12")
        assertEntry(list, 0, "message1", 4)
        assertEntry(list, 1, "message12", 1)

        list.addEntry(makeEntry("message1"))
        listAssertMessages(list, "message1", "message12")
        assertEntry(list, 0, "message1", 5)
        assertEntry(list, 1, "message12", 1)

        list.addEntry(makeEntry("message12"))
        listAssertMessages(list, "message1", "message12")
        assertEntry(list, 0, "message1", 5)
        assertEntry(list, 1, "message12", 2)

        list.addEntry(makeEntry("message2"))
        listAssertMessages(list, "message1", "message12")
        assertEntry(list, 0, "message1", 5)
        assertEntry(list, 1, "message12", 2)

        list.setFilterByText("")
        assertTrue(list.isFiltering)

        listAssertMessages(list, "message1", "message12", "message2")
        assertEntry(list, 0, "message1", 1)
        assertEntry(list, 1, "message12", 1)
        assertEntry(list, 1, "message12", 1)

        list.setCollapsed(false)
        assertFalse(list.isFiltering)

        listAssertMessages(list, "message1", "message12", "message2")
    }

    //region Helpers

    private fun listAssertMessages(list: ConsoleLogEntryList, vararg expected: String) {
        assertEquals(
            "\nExpected:${list.entries.joinToString { it.message }}\n  Actual:${expected.joinToString()}",
            expected.size,
            list.count()
        )
        for (i in expected.indices) {
            assertEquals(expected[i], list[i].message)
        }
    }

    private fun createEntryListWithMessages(vararg messages: String): ConsoleLogEntryList {
        val list = ConsoleLogEntryList(100, 1)
        for (message in messages) {
            list.addEntry(makeEntry(LOG, message))
        }
        return list
    }

    private fun createEntryListWithEntries(vararg entries: LogEntry): ConsoleLogEntryList {
        return createEntryListWithEntries(kDefaultCapacity, kDefaultTrim, *entries)
    }

    private fun createEntryListWithEntries(
        capacity: Int,
        trimSize: Int,
        vararg entries: LogEntry
    ): ConsoleLogEntryList {
        val list = ConsoleLogEntryList(capacity, trimSize)
        for (entry in entries) {
            list.addEntry(entry)
        }
        return list
    }

    private fun assertEntry(list: ConsoleLogEntryList, index: Int, message: String, count: Int) {
        assertEntry(list, index, message, count, index)
    }

    private fun assertEntry(
        list: ConsoleLogEntryList,
        index: Int,
        expectedMessage: String,
        expectedCount: Int,
        expectedIndex: Int
    ) {
        val entry = list.getCollapsedEntry(index) ?: throw AssertionError("Missing collapsed entry")
        assertEquals(expectedMessage, entry.message)
        assertEquals(expectedCount, entry.count)
        assertEquals(expectedIndex, entry.index)
    }

    //endregion

    //region Companion

    companion object {
        private const val kDefaultCapacity = 100
        private const val kDefaultTrim = 1

        private fun makeEntry(type: LogEntryType, message: String) = LogEntry(0, type, message)
        private fun makeEntry(message: String) = makeEntry(LOG, message)
    }

    //endregion
}
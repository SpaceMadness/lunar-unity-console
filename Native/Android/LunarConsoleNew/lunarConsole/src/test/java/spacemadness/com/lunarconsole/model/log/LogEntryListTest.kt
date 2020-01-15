package spacemadness.com.lunarconsole.model.log

import org.junit.Assert.*
import org.junit.Test
import spacemadness.com.lunarconsole.model.log.LogEntryType.*
import java.lang.AssertionError

class LogEntryListTest {
    @Test
    fun testFilteringByText() {
        val list = createEntryListWithMessages(
            "line1",
            "line11",
            "line111",
            "line1111",
            "foo"
        )

        assertTrue(!list.isFiltering)

        assertTrue(list.setFilterByText("l"))
        listAssertMessages(list, "line1", "line11", "line111", "line1111")
        assertTrue(list.isFiltering)

        assertTrue(!list.setFilterByText("l"))
        listAssertMessages(list, "line1", "line11", "line111", "line1111")
        assertTrue(list.isFiltering)

        assertTrue(list.setFilterByText("li"))
        listAssertMessages(list, "line1", "line11", "line111", "line1111")
        assertTrue(list.isFiltering)

        assertTrue(list.setFilterByText("lin"))
        listAssertMessages(list, "line1", "line11", "line111", "line1111")
        assertTrue(list.isFiltering)

        assertTrue(list.setFilterByText("line"))
        listAssertMessages(list, "line1", "line11", "line111", "line1111")
        assertTrue(list.isFiltering)

        assertTrue(list.setFilterByText("line1"))
        listAssertMessages(list, "line1", "line11", "line111", "line1111")
        assertTrue(list.isFiltering)

        assertTrue(list.setFilterByText("line11"))
        listAssertMessages(list, "line11", "line111", "line1111")
        assertTrue(list.isFiltering)

        assertTrue(list.setFilterByText("line111"))
        listAssertMessages(list, "line111", "line1111")
        assertTrue(list.isFiltering)

        assertTrue(list.setFilterByText("line1111"))
        listAssertMessages(list, "line1111")
        assertTrue(list.isFiltering)

        assertTrue(list.setFilterByText("line11111"))
        listAssertMessages(list)
        assertTrue(list.isFiltering)

        assertTrue(list.setFilterByText("line1111"))
        listAssertMessages(list, "line1111")
        assertTrue(list.isFiltering)

        assertTrue(list.setFilterByText("line111"))
        listAssertMessages(list, "line111", "line1111")
        assertTrue(list.isFiltering)

        assertTrue(list.setFilterByText("line11"))
        listAssertMessages(list, "line11", "line111", "line1111")
        assertTrue(list.isFiltering)

        assertTrue(list.setFilterByText("line1"))
        listAssertMessages(list, "line1", "line11", "line111", "line1111")
        assertTrue(list.isFiltering)

        assertTrue(list.setFilterByText("line"))
        listAssertMessages(list, "line1", "line11", "line111", "line1111")
        assertTrue(list.isFiltering)

        assertTrue(list.setFilterByText("lin"))
        listAssertMessages(list, "line1", "line11", "line111", "line1111")
        assertTrue(list.isFiltering)

        assertTrue(list.setFilterByText("li"))
        listAssertMessages(list, "line1", "line11", "line111", "line1111")
        assertTrue(list.isFiltering)

        assertTrue(list.setFilterByText("l"))
        listAssertMessages(list, "line1", "line11", "line111", "line1111")
        assertTrue(list.isFiltering)

        assertTrue(list.setFilterByText(""))
        listAssertMessages(list, "line1", "line11", "line111", "line1111", "foo")
        assertTrue(!list.isFiltering)
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

        assertTrue(!list.isFiltering)

        assertTrue(list.setFilterByLogType(ERROR, true))
        listAssertMessages(
            list,
            "assert1", "assert2",
            "warning1", "warning2",
            "log1", "log2",
            "exception1", "exception2"
        )
        assertTrue(list.isFiltering)

        assertTrue(!list.setFilterByLogType(ERROR, true))
        listAssertMessages(
            list,
            "assert1", "assert2",
            "warning1", "warning2",
            "log1", "log2",
            "exception1", "exception2"
        )
        assertTrue(list.isFiltering)

        assertTrue(list.setFilterByLogType(ASSERT, true))
        listAssertMessages(
            list,
            "warning1", "warning2",
            "log1", "log2",
            "exception1", "exception2"
        )
        assertTrue(list.isFiltering)

        assertTrue(list.setFilterByLogType(WARNING, true))
        listAssertMessages(
            list,
            "log1", "log2",
            "exception1", "exception2"
        )
        assertTrue(list.isFiltering)

        assertTrue(list.setFilterByLogType(LOG, true))
        listAssertMessages(
            list,
            "exception1", "exception2"
        )
        assertTrue(list.isFiltering)

        assertTrue(list.setFilterByLogType(EXCEPTION, true))
        listAssertMessages(list)
        assertTrue(list.isFiltering)

        assertTrue(list.setFilterByLogType(EXCEPTION, false))
        listAssertMessages(
            list,
            "exception1", "exception2"
        )
        assertTrue(list.isFiltering)

        assertTrue(list.setFilterByLogType(LOG, false))
        listAssertMessages(
            list,
            "log1", "log2",
            "exception1", "exception2"
        )
        assertTrue(list.isFiltering)

        assertTrue(list.setFilterByLogType(WARNING, false))
        listAssertMessages(
            list,
            "warning1", "warning2",
            "log1", "log2",
            "exception1", "exception2"
        )
        assertTrue(list.isFiltering)

        assertTrue(list.setFilterByLogType(ASSERT, false))
        listAssertMessages(
            list,
            "assert1", "assert2",
            "warning1", "warning2",
            "log1", "log2",
            "exception1", "exception2"
        )
        assertTrue(list.isFiltering)

        assertTrue(!list.setFilterByLogType(ASSERT, false))
        listAssertMessages(
            list,
            "assert1", "assert2",
            "warning1", "warning2",
            "log1", "log2",
            "exception1", "exception2"
        )
        assertTrue(list.isFiltering)

        assertTrue(list.setFilterByLogType(ERROR, false))
        listAssertMessages(
            list,
            "error1", "error2",
            "assert1", "assert2",
            "warning1", "warning2",
            "log1", "log2",
            "exception1", "exception2"
        )
        assertTrue(!list.isFiltering)
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

        assertTrue(!list.isFiltering)

        var mask = getMask(ERROR) or
                getMask(EXCEPTION) or
                getMask(ASSERT)

        assertTrue(list.setFilterByLogTypeMask(mask, true))
        listAssertMessages(
            list,
            "warning1", "warning2",
            "log1", "log2"
        )
        assertTrue(list.isFiltering)

        mask = getMask(ERROR) or getMask(ASSERT)

        assertTrue(list.setFilterByLogTypeMask(mask, false))
        listAssertMessages(
            list,
            "error1", "error2",
            "assert1", "assert2",
            "warning1", "warning2",
            "log1", "log2"
        )
        assertTrue(list.isFiltering)

        assertTrue(!list.setFilterByLogTypeMask(mask, false))
        listAssertMessages(
            list,
            "error1", "error2",
            "assert1", "assert2",
            "warning1", "warning2",
            "log1", "log2"
        )
        assertTrue(list.isFiltering)

        mask = getMask(ERROR) or
                getMask(EXCEPTION) or
                getMask(ASSERT)

        assertTrue(list.setFilterByLogTypeMask(mask, false))
        listAssertMessages(
            list,
            "error1", "error2",
            "assert1", "assert2",
            "warning1", "warning2",
            "log1", "log2",
            "exception1", "exception2"
        )
        assertTrue(!list.isFiltering)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Collapse items

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
        assertTrue(!list.isFiltering)

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
        assertTrue(!list.isFiltering)

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
        assertTrue(!list.isFiltering)

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
        assertTrue(!list.isFiltering)

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
        assertTrue(!list.isFiltering)

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
        assertTrue(!list.isFiltering)

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
        assertTrue(!list.isFiltering)

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
        assertTrue(!list.isFiltering)

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
        assertTrue(!list.isFiltering)

        listAssertMessages(list, "message1", "message12", "message2")
    }

    //region Helpers

    private fun listAssertMessages(list: ConsoleLogEntryList, vararg expected: String) {
        assertEquals(
            "Expected:${list.entries.joinToString { it.message }}\nActual:${expected.joinToString()}",
            expected.size,
            list.count()
        )
        for (i in expected.indices) {
            val entry = list.getEntry(i)
            assertEquals(expected[i], entry.message)
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
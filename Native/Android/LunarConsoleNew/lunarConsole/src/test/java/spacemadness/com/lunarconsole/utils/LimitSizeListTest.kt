package spacemadness.com.lunarconsole.utils

import org.junit.Assert.*
import org.junit.Test

class LimitSizeListTest {
    @Test
    fun testAddElements() {
        val list = testList(10)
        list.addObject("1")
        list.addObject("2")
        list.addObject("3")

        listAssertObjects(list, "1", "2", "3")
    }

    @Test
    fun testTrimElements() {
        val list = testList(3, 2)
        assertEquals(0, list.count().toLong())
        assertEquals(0, list.totalCount().toLong())
        assertEquals(0, list.trimmedCount().toLong())
        assertFalse(list.isTrimmed)

        list.addObject("1")
        assertEquals(1, list.count().toLong())
        assertEquals(1, list.totalCount().toLong())
        assertEquals(0, list.trimmedCount().toLong())
        assertFalse(list.isTrimmed)

        list.addObject("2")
        assertEquals(2, list.count().toLong())
        assertEquals(2, list.totalCount().toLong())
        assertEquals(0, list.trimmedCount().toLong())
        assertFalse(list.isTrimmed)

        list.addObject("3")
        assertEquals(3, list.count().toLong())
        assertEquals(3, list.totalCount().toLong())
        assertEquals(0, list.trimmedCount().toLong())
        assertFalse(list.isTrimmed)

        list.addObject("4")
        assertEquals(2, list.count().toLong())
        assertEquals(4, list.totalCount().toLong())
        assertEquals(2, list.trimmedCount().toLong())
        assertTrue(list.isTrimmed)

        listAssertObjects(list, "3", "4")

        list.addObject("5")
        assertEquals(3, list.count().toLong())
        assertEquals(5, list.totalCount().toLong())
        assertEquals(2, list.trimmedCount().toLong())
        assertTrue(list.isTrimmed)

        listAssertObjects(list, "3", "4", "5")

        list.addObject("6")
        assertEquals(2, list.count().toLong())
        assertEquals(6, list.totalCount().toLong())
        assertEquals(4, list.trimmedCount().toLong())
        assertTrue(list.isTrimmed)

        listAssertObjects(list, "5", "6")

        list.addObject("7")
        assertEquals(3, list.count().toLong())
        assertEquals(7, list.totalCount().toLong())
        assertEquals(4, list.trimmedCount().toLong())
        assertTrue(list.isTrimmed)

        listAssertObjects(list, "5", "6", "7")
    }

    @Test
    fun testTrimElementsAndClear() {
        val list = testList(3, 2)
        list.addObject("1")
        list.addObject("2")
        list.addObject("3")
        list.addObject("4")

        list.clear()
        assertEquals(0, list.count().toLong())
        assertEquals(0, list.totalCount().toLong())
        assertEquals(0, list.trimmedCount().toLong())
        assertFalse(list.isTrimmed)

        list.addObject("5")
        assertEquals(1, list.count().toLong())
        assertEquals(1, list.totalCount().toLong())
        assertEquals(0, list.trimmedCount().toLong())
        assertFalse(list.isTrimmed)

        listAssertObjects(list, "5")

        list.addObject("6")
        assertEquals(2, list.count().toLong())
        assertEquals(2, list.totalCount().toLong())
        assertEquals(0, list.trimmedCount().toLong())
        assertFalse(list.isTrimmed)

        listAssertObjects(list, "5", "6")

        list.addObject("7")
        assertEquals(3, list.count().toLong())
        assertEquals(3, list.totalCount().toLong())
        assertEquals(0, list.trimmedCount().toLong())
        assertFalse(list.isTrimmed)

        listAssertObjects(list, "5", "6", "7")

        list.addObject("8")
        assertEquals(2, list.count().toLong())
        assertEquals(4, list.totalCount().toLong())
        assertEquals(2, list.trimmedCount().toLong())
        assertTrue(list.isTrimmed)

        listAssertObjects(list, "7", "8")
    }

    private fun listAssertObjects(list: LimitSizeList<String>, vararg expected: String) {
        assertEquals(expected.size.toLong(), list.count().toLong())
        for (i in expected.indices) {
            assertEquals(expected[i], list.objectAtIndex(i))
        }
    }
}

private fun testList(capacity: Int, trimSize: Int = 1) = LimitSizeList<String>(capacity, trimSize)
package spacemadness.com.lunarconsole.utils

import org.junit.Assert.*
import org.junit.Test

class LimitSizeListTest {
    @Test
    fun testAddElements() {
        val list = testList(capacity = 10)
        list.addObject(1)
        list.addObject(2)
        list.addObject(3)

        assertList(list, 1, 2, 3)
    }

    @Test
    fun testTrimElements() {
        val list = testList(capacity = 3, trimSize = 2)
        assertEquals(0, list.count())
        assertEquals(0, list.totalCount())
        assertEquals(0, list.trimmedCount())
        assertFalse(list.isTrimmed)

        assertEquals(0, list.addObject(1))
        assertEquals(1, list.count())
        assertEquals(1, list.totalCount())
        assertEquals(0, list.trimmedCount())
        assertFalse(list.isTrimmed)

        assertEquals(0, list.addObject(2))
        assertEquals(2, list.count())
        assertEquals(2, list.totalCount())
        assertEquals(0, list.trimmedCount())
        assertFalse(list.isTrimmed)

        assertEquals(0, list.addObject(3))
        assertEquals(3, list.count())
        assertEquals(3, list.totalCount())
        assertEquals(0, list.trimmedCount())
        assertFalse(list.isTrimmed)

        assertEquals(2, list.addObject(4))
        assertEquals(2, list.count())
        assertEquals(4, list.totalCount())
        assertEquals(2, list.trimmedCount())
        assertTrue(list.isTrimmed)

        assertList(list, 3, 4)

        assertEquals(0, list.addObject(5))
        assertEquals(3, list.count())
        assertEquals(5, list.totalCount())
        assertEquals(2, list.trimmedCount())
        assertTrue(list.isTrimmed)

        assertList(list, 3, 4, 5)

        assertEquals(2, list.addObject(6))
        assertEquals(2, list.count())
        assertEquals(6, list.totalCount())
        assertEquals(4, list.trimmedCount())
        assertTrue(list.isTrimmed)

        assertList(list, 5, 6)

        assertEquals(0, list.addObject(7))
        assertEquals(3, list.count())
        assertEquals(7, list.totalCount())
        assertEquals(4, list.trimmedCount())
        assertTrue(list.isTrimmed)

        assertList(list, 5, 6, 7)
    }

    @Test
    fun testTrimMultipleElements() {
        val list = testList(capacity = 9, trimSize = 3)
        assertEquals(0, list.count())
        assertEquals(0, list.totalCount())
        assertEquals(0, list.trimmedCount())
        assertFalse(list.isTrimmed)
        assertList(list)

        assertEquals(0, list.addAll(rangeList(1, 6)))
        assertEquals(6, list.count())
        assertEquals(6, list.totalCount())
        assertEquals(0, list.trimmedCount())
        assertFalse(list.isTrimmed)
        assertList(list, 1, 2, 3, 4, 5, 6)

        assertEquals(3, list.addAll(rangeList(7, 12)))
        assertEquals(9, list.count())
        assertEquals(12, list.totalCount())
        assertEquals(3, list.trimmedCount())
        assertTrue(list.isTrimmed)
        assertList(list, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    }

    @Test
    fun testTrimElementsAndClear() {
        val list = testList(3, 2)
        list.addObject(1)
        list.addObject(2)
        list.addObject(3)
        list.addObject(4)

        list.clear()
        assertEquals(0, list.count())
        assertEquals(0, list.totalCount())
        assertEquals(0, list.trimmedCount())
        assertFalse(list.isTrimmed)

        list.addObject(5)
        assertEquals(1, list.count())
        assertEquals(1, list.totalCount())
        assertEquals(0, list.trimmedCount())
        assertFalse(list.isTrimmed)

        assertList(list, 5)

        list.addObject(6)
        assertEquals(2, list.count())
        assertEquals(2, list.totalCount())
        assertEquals(0, list.trimmedCount())
        assertFalse(list.isTrimmed)

        assertList(list, 5, 6)

        list.addObject(7)
        assertEquals(3, list.count())
        assertEquals(3, list.totalCount())
        assertEquals(0, list.trimmedCount())
        assertFalse(list.isTrimmed)

        assertList(list, 5, 6, 7)

        list.addObject(8)
        assertEquals(2, list.count())
        assertEquals(4, list.totalCount())
        assertEquals(2, list.trimmedCount())
        assertTrue(list.isTrimmed)

        assertList(list, 7, 8)
    }

    private fun assertList(list: LimitSizeList<Int>, vararg expected: Int) {
        assertEquals(expected.size, list.count())
        for (i in expected.indices) {
            assertEquals(expected[i], list[i])
        }
    }

    private fun testList(capacity: Int, trimSize: Int = 1) = LimitSizeList<Int>(capacity, trimSize)
}
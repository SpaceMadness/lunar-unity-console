package spacemadness.com.lunarconsole.utils

import org.junit.Assert.*
import org.junit.Test

class CycleArrayTest {
    //region Single item

    @Test
    fun testAddSingleElementAndClear() {
        val array = CycleArray<Int>(3)
        assertEquals(0, array.add(1))

        assertEquals(1, array.length())
        assertEquals(1, array.realLength())
        assertArray(array, 1)

        assertEquals(0, array.add(2))

        assertEquals(2, array.length())
        assertEquals(2, array.realLength())
        assertArray(array, 1, 2)

        assertEquals(0, array.add(3))

        assertEquals(3, array.length())
        assertEquals(3, array.realLength())
        assertArray(array, 1, 2, 3)

        assertEquals(1, array.add(4))

        assertEquals(4, array.length())
        assertEquals(3, array.realLength())
        assertArray(array, 2, 3, 4)

        assertEquals(1, array.add(5))

        assertEquals(5, array.length())
        assertEquals(3, array.realLength())
        assertArray(array, 3, 4, 5)

        assertEquals(1, array.add(6))

        assertEquals(6, array.length())
        assertEquals(3, array.realLength())
        assertArray(array, 4, 5, 6)

        assertEquals(1, array.add(7))

        assertEquals(7, array.length())
        assertEquals(3, array.realLength())
        assertArray(array, 5, 6, 7)

        array.clear()

        assertEquals(0, array.add(8))
        assertEquals(1, array.length())
        assertEquals(1, array.realLength())
        assertArray(array, 8)
    }

    @Test
    fun testTrimHeadIndex() {
        val array = CycleArray<Int>(5)
        assertEquals(0, array.add(1))
        assertEquals(0, array.add(2))
        assertEquals(0, array.add(3))
        assertEquals(0, array.add(4))
        assertEquals(0, array.add(5))

        array.trimToHeadIndex(2)

        assertArray(array, 3, 4, 5)
        assertEquals(2, array.headIndex)
        assertEquals(5, array.length())
        assertEquals(3, array.realLength())

        assertEquals(0, array.add(6))
        assertEquals(0, array.add(7))

        assertArray(array, 3, 4, 5, 6, 7)
        assertEquals(2, array.headIndex)
        assertEquals(7, array.length())
        assertEquals(5, array.realLength())

        assertEquals(1, array.add(8))
        assertEquals(1, array.add(9))

        assertArray(array, 5, 6, 7, 8, 9)
        assertEquals(4, array.headIndex)
        assertEquals(9, array.length())
        assertEquals(5, array.realLength())

        array.trimToHeadIndex(7)

        assertArray(array, 8, 9)
        assertEquals(7, array.headIndex)
        assertEquals(9, array.length())
        assertEquals(2, array.realLength())

        assertEquals(0, array.add(10))
        assertEquals(0, array.add(11))

        assertArray(array, 8, 9, 10, 11)
        assertEquals(7, array.headIndex)
        assertEquals(11, array.length())
        assertEquals(4, array.realLength())

        array.trimToHeadIndex(11)

        assertArray(array)
        assertEquals(11, array.headIndex)
        assertEquals(11, array.length())
        assertEquals(0, array.realLength())

        assertEquals(0, array.add(12))
        assertEquals(0, array.add(13))
        assertEquals(0, array.add(14))
        assertEquals(0, array.add(15))
        assertEquals(0, array.add(16))
        assertEquals(1, array.add(17))
        assertEquals(1, array.add(18))

        assertArray(array, 14, 15, 16, 17, 18)
        assertEquals(13, array.headIndex)
        assertEquals(18, array.length())
        assertEquals(5, array.realLength())
    }

    @Test
    fun testContains() {
        val array = CycleArray<Int>(3)
        array.add(1)
        array.add(2)
        array.add(3)

        assertTrue(array.contains(1))
        assertTrue(array.contains(2))
        assertTrue(array.contains(3))

        array.add(4)
        assertFalse(array.contains(1))
        assertTrue(array.contains(2))
        assertTrue(array.contains(3))
        assertTrue(array.contains(4))

        array.add(5)
        assertFalse(array.contains(1))
        assertFalse(array.contains(2))
        assertTrue(array.contains(3))
        assertTrue(array.contains(4))
        assertTrue(array.contains(5))

        array.add(6)
        assertFalse(array.contains(1))
        assertFalse(array.contains(2))
        assertFalse(array.contains(3))
        assertTrue(array.contains(4))
        assertTrue(array.contains(5))
        assertTrue(array.contains(6))

        array.add(7)
        assertFalse(array.contains(1))
        assertFalse(array.contains(2))
        assertFalse(array.contains(3))
        assertFalse(array.contains(4))
        assertTrue(array.contains(5))
        assertTrue(array.contains(6))
        assertTrue(array.contains(7))
    }

    //endregion

    //region Multiple Elements

    @Test
    fun testAddMultipleElements1() {
        val array = CycleArray<Int>(9)
        assertEquals(0, array.addAll(rangeList(1, 3)))
        assertEquals(3, array.length())
        assertEquals(3, array.realLength())
        assertArray(array, 1, 2, 3)

        assertEquals(0, array.addAll(rangeList(4, 6)))
        assertEquals(6, array.length())
        assertEquals(6, array.realLength())
        assertArray(array, 1, 2, 3, 4, 5, 6)

        assertEquals(0, array.addAll(rangeList(7, 9)))
        assertEquals(9, array.length())
        assertEquals(9, array.realLength())
        assertArray(array, 1, 2, 3, 4, 5, 6, 7, 8, 9)

        assertEquals(3, array.addAll(rangeList(10, 12)))
        assertEquals(12, array.length())
        assertEquals(9, array.realLength())
        assertArray(array, 4, 5, 6, 7, 8, 9, 10, 11, 12)


        assertEquals(7, array.addAll(rangeList(13, 19)))
        assertEquals(19, array.length())
        assertEquals(9, array.realLength())
        assertArray(array, 11, 12, 13, 14, 15, 16, 17, 18, 19)


        assertEquals(21, array.addAll(rangeList(20, 40)))
        assertEquals(40, array.length())
        assertEquals(9, array.realLength())
        assertArray(array, 32, 33, 34, 35, 36, 37, 38, 39, 40)

        assertEquals(20, array.addAll(rangeList(41, 60)))
        assertEquals(60, array.length())
        assertEquals(9, array.realLength())
        assertArray(array, 52, 53, 54, 55, 56, 57, 58, 59, 60)
    }

    @Test
    fun testAddMultipleElements2() {
        val array = CycleArray<Int>(9)
        assertEquals(0, array.addAll(rangeList(1, 6)))
        assertEquals(6, array.length())
        assertEquals(6, array.realLength())
        assertArray(array, 1, 2, 3, 4, 5, 6)

        assertEquals(3, array.addAll(rangeList(7, 12)))
        assertEquals(12, array.length())
        assertEquals(9, array.realLength())
        assertArray(array, 4, 5, 6, 7, 8, 9, 10, 11, 12)

        assertEquals(7, array.addAll(rangeList(13, 19)))
        assertEquals(19, array.length())
        assertEquals(9, array.realLength())
        assertArray(array, 11, 12, 13, 14, 15, 16, 17, 18, 19)


        assertEquals(21, array.addAll(rangeList(20, 40)))
        assertEquals(40, array.length())
        assertEquals(9, array.realLength())
        assertArray(array, 32, 33, 34, 35, 36, 37, 38, 39, 40)

        assertEquals(20, array.addAll(rangeList(41, 60)))
        assertEquals(60, array.length())
        assertEquals(9, array.realLength())
        assertArray(array, 52, 53, 54, 55, 56, 57, 58, 59, 60)
    }

    //endregion

    //region Iterator

    @Test
    fun testIterator() {
        val array = CycleArray<Int>(3)

        array.add(1)
        array.add(2)
        array.add(3)
        assertArrayIterator(array, 1, 2, 3)

        array.add(4)
        assertArrayIterator(array, 2, 3, 4)

        array.add(5)
        assertArrayIterator(array, 3, 4, 5)

        array.add(6)
        assertArrayIterator(array, 4, 5, 6)

        array.add(7)
        assertArrayIterator(array, 5, 6, 7)
    }

    //endregion

    //region Helpers

    private fun <T> assertArray(actual: CycleArray<T>, vararg expected: T) {
        assertEquals(expected.size, actual.realLength())
        var i = 0
        var j = actual.headIndex
        while (i < expected.size) {
            assertEquals(expected[i++], actual[j++])
        }
    }

    private fun <T> assertArrayIterator(actual: CycleArray<T>, vararg expected: T) {
        var index = 0
        for (e in actual) {
            assertEquals(expected[index++], e)
        }
        assertEquals(expected.size, index)
    }
    //endregion
}
package spacemadness.com.lunarconsole.utils

import org.junit.Assert.*
import org.junit.Test

class CycleArrayTest {
    @Test
    fun testAddElements() {
        val array = CycleArray<String>(3)
        array.add("1")

        assertEquals(3, array.capacity)
        assertEquals(1, array.length())
        assertEquals(1, array.realLength())
        assertArray(array, "1")
    }

    @Test
    fun testAddElements2() {
        val array = CycleArray<String>(3)
        array.add("1")
        array.add("2")

        assertEquals(3, array.capacity)
        assertEquals(2, array.length())
        assertEquals(2, array.realLength())
        assertArray(array, "1", "2")
    }

    @Test
    fun testAddElements3() {
        val array = CycleArray<String>(3)
        array.add("1")
        array.add("2")
        array.add("3")

        assertEquals(3, array.capacity)
        assertEquals(3, array.length())
        assertEquals(3, array.realLength())
        assertArray(array, "1", "2", "3")
    }

    @Test
    fun testAddElements4() {
        val array = CycleArray<String>(3)
        array.add("1")
        array.add("2")
        array.add("3")
        array.add("4")

        assertEquals(3, array.capacity)
        assertEquals(4, array.length())
        assertEquals(3, array.realLength())
        assertArray(array, "2", "3", "4")
    }

    @Test
    fun testAddElements5() {
        val array = CycleArray<String>(3)
        array.add("1")
        array.add("2")
        array.add("3")
        array.add("4")
        array.add("5")

        assertEquals(3, array.capacity)
        assertEquals(5, array.length())
        assertEquals(3, array.realLength())
        assertArray(array, "3", "4", "5")
    }

    @Test
    fun testAddElements6() {
        val array = CycleArray<String>(3)
        array.add("1")
        array.add("2")
        array.add("3")
        array.add("4")
        array.add("5")
        array.add("6")

        assertEquals(3, array.capacity)
        assertEquals(6, array.length())
        assertEquals(3, array.realLength())
        assertArray(array, "4", "5", "6")
    }

    @Test
    fun testAddElements7() {
        val array = CycleArray<String>(3)
        array.add("1")
        array.add("2")
        array.add("3")
        array.add("4")
        array.add("5")
        array.add("6")
        array.add("7")

        assertEquals(3, array.capacity)
        assertEquals(7, array.length())
        assertEquals(3, array.realLength())
        assertArray(array, "5", "6", "7")
    }

    @Test
    fun testAddElements8() {
        val array = CycleArray<String>(3)
        array.add("1")
        array.add("2")
        array.add("3")
        array.add("4")
        array.add("5")
        array.add("6")
        array.add("7")
        array.add("8")

        assertEquals(3, array.capacity)
        assertEquals(8, array.length())
        assertEquals(3, array.realLength())
        assertArray(array, "6", "7", "8")
    }

    @Test
    fun testAddElements9() {
        val array = CycleArray<String>(3)
        array.add("1")
        array.add("2")
        array.add("3")
        array.add("4")
        array.add("5")
        array.add("6")
        array.add("7")
        array.add("8")
        array.add("9")

        assertEquals(3, array.capacity)
        assertEquals(9, array.length())
        assertEquals(3, array.realLength())
        assertArray(array, "7", "8", "9")
    }

    @Test
    fun testTrimHeadIndex() {
        val array = CycleArray<String>(5)
        array.add("1")
        array.add("2")
        array.add("3")
        array.add("4")
        array.add("5")

        array.trimToHeadIndex(2)

        assertArray(array, "3", "4", "5")
        assertEquals(2, array.headIndex)
        assertEquals(5, array.length())
        assertEquals(3, array.realLength())

        array.add("6")
        array.add("7")

        assertArray(array, "3", "4", "5", "6", "7")
        assertEquals(2, array.headIndex)
        assertEquals(7, array.length())
        assertEquals(5, array.realLength())

        array.add("8")
        array.add("9")

        assertArray(array, "5", "6", "7", "8", "9")
        assertEquals(4, array.headIndex)
        assertEquals(9, array.length())
        assertEquals(5, array.realLength())

        array.trimToHeadIndex(7)

        assertArray(array, "8", "9")
        assertEquals(7, array.headIndex)
        assertEquals(9, array.length())
        assertEquals(2, array.realLength())

        array.add("10")
        array.add("11")

        assertArray(array, "8", "9", "10", "11")
        assertEquals(7, array.headIndex)
        assertEquals(11, array.length())
        assertEquals(4, array.realLength())

        array.trimToHeadIndex(11)

        assertArray(array)
        assertEquals(11, array.headIndex)
        assertEquals(11, array.length())
        assertEquals(0, array.realLength())

        array.add("12")
        array.add("13")
        array.add("14")
        array.add("15")
        array.add("16")
        array.add("17")
        array.add("18")

        assertArray(array, "14", "15", "16", "17", "18")
        assertEquals(13, array.headIndex)
        assertEquals(18, array.length())
        assertEquals(5, array.realLength())
    }

    @Test
    fun testContains() {
        val array = CycleArray<String>(3)
        array.add("1")
        array.add("2")
        array.add("3")

        assertTrue(array.contains("1"))
        assertTrue(array.contains("2"))
        assertTrue(array.contains("3"))

        array.add("4")
        assertFalse(array.contains("1"))
        assertTrue(array.contains("2"))
        assertTrue(array.contains("3"))
        assertTrue(array.contains("4"))

        array.add("5")
        assertFalse(array.contains("1"))
        assertFalse(array.contains("2"))
        assertTrue(array.contains("3"))
        assertTrue(array.contains("4"))
        assertTrue(array.contains("5"))

        array.add("6")
        assertFalse(array.contains("1"))
        assertFalse(array.contains("2"))
        assertFalse(array.contains("3"))
        assertTrue(array.contains("4"))
        assertTrue(array.contains("5"))
        assertTrue(array.contains("6"))

        array.add("7")
        assertFalse(array.contains("1"))
        assertFalse(array.contains("2"))
        assertFalse(array.contains("3"))
        assertFalse(array.contains("4"))
        assertTrue(array.contains("5"))
        assertTrue(array.contains("6"))
        assertTrue(array.contains("7"))
    }

    @Test
    fun testRemovedItem() {
        val array = CycleArray<String>(3)

        assertNull(array.add("1"))
        assertNull(array.add("2"))
        assertNull(array.add("3"))

        assertEquals("1", array.add("4"))
        assertEquals("2", array.add("5"))
        assertEquals("3", array.add("6"))

        assertEquals("4", array.add("7"))
        assertEquals("5", array.add("8"))
        assertEquals("6", array.add("9"))
    }

    @Test
    fun testIterator() {
        val array = CycleArray<String>(3)

        array.add("1")
        array.add("2")
        array.add("3")
        assertArrayIterator(array, "1", "2", "3")

        array.add("4")
        assertArrayIterator(array, "2", "3", "4")

        array.add("5")
        assertArrayIterator(array, "3", "4", "5")

        array.add("6")
        assertArrayIterator(array, "4", "5", "6")

        array.add("7")
        assertArrayIterator(array, "5", "6", "7")
    }

    private fun <T> assertArray(actual: CycleArray<T>, vararg expected: T) {
        assertEquals(expected.size, actual.realLength())
        var i = 0
        var j = actual.headIndex
        while (i < expected.size) {
            assertEquals(expected[i], actual[j])
            ++i
            ++j
        }
    }

    private fun <T> assertArrayIterator(actual: CycleArray<T>, vararg expected: T) {
        var index = 0
        for (e in actual) {
            assertEquals(expected[index++], e)
        }
        assertEquals(expected.size, index)
    }
}
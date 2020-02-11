package spacemadness.com.lunarconsole.utils

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SortedListTest {
    var nextIndex = 0

    @Before
    fun setup() {
        nextIndex = 0
    }

    @Test
    fun testSortedAdd() {
        val list = SortedList<String>()
        list.add("3")
        list.add("2")
        list.add("1")
        list.add("4")
        assertList(list, "1", "2", "3", "4")
    }

    @Test
    fun testSortedAddDuplicates() {
        val list = SortedList<Element>()
        val a0 = Element(0, "4")
        val a1 = Element(1, "3")
        val a2 = Element(2, "3")
        val a3 = Element(3, "2")
        val a4 = Element(4, "2")
        val a5 = Element(5, "1")

        list.add(a0)
        list.add(a1)
        list.add(a2)
        list.add(a3)
        list.add(a4)
        list.add(a5)
        assertList(list, a5, a3, a4, a1, a2, a0)
    }

    @Test
    fun testRemoveItems() {
        val list = SortedList<String>()
        list.add("3")
        list.add("2")
        list.add("1")
        list.add("4")
        list.remove("2")
        assertList(list, "1", "3", "4")
    }

    @Test
    fun testIndexOfItem() {
        val list = SortedList<String>()
        list.add("3")
        list.add("2")
        list.add("1")
        list.add("4")
        assertEquals(0, list.indexOf("1"))
        assertEquals(1, list.indexOf("2"))
        assertEquals(2, list.indexOf("3"))
        assertEquals(3, list.indexOf("4"))
    }

    private fun <T : Comparable<T>> assertList(list: SortedList<T>, vararg expected: T) {
        val message = "\nExpected: ${expected.contentToString()}\nActual: ${list.contentToString()}"
        assertEquals(message, expected.size, list.count())
        for (i in expected.indices) {
            assertEquals(message, expected[i], list[i])
        }
    }
}

private fun <T> List<T>.contentToString(): String {
    return joinToString(", ", prefix = "[", postfix = "]")
}

private data class Element(val index: Int, val value: String) : Comparable<Element> {
    override fun compareTo(other: Element) = value.compareTo(other.value)
}
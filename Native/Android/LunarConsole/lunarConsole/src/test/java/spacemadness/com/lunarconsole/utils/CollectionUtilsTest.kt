package spacemadness.com.lunarconsole.utils

import org.junit.Assert.*
import org.junit.Test
import spacemadness.com.lunarconsole.utils.CollectionUtils.indexOf

class CollectionUtilsTest {
    @Test
    fun indexOf() {
        val array = Array(3) { it * 2 } // 0 2 4
        assertEquals(0, indexOf(array, 0))
        assertEquals(-1, indexOf(array, 1))
        assertEquals(1, indexOf(array, 2))
        assertEquals(-1, indexOf(array, 3))
        assertEquals(2, indexOf(array, 4))
        assertEquals(-1, indexOf(array, 5))
    }

    @Test
    fun mapArray() {
        val array = Array(3) { it }
        val expected = arrayOf("0", "1", "2")
        val actual = CollectionUtils.map(array) { value -> value.toString() }
        assertArrayEquals(expected, actual)
    }

    @Test
    fun mapList() {
        val list = listOf(0, 1, 2)
        val expected = listOf("0", "1", "2")
        val actual = CollectionUtils.map(list) { value -> value.toString() }
        assertEquals(expected, actual)
    }

    @Test
    fun merge() {
        val list1 = mutableListOf(0, 1, 2)
        val list2 = mutableListOf<Int>()
        val list3 = mutableListOf(3, 4)
        val list4 = mutableListOf(5)
        val list5 = mutableListOf<Int>()

        val expected = listOf(0, 1, 2, 3, 4, 5)
        val list: MutableList<MutableList<Int>> = mutableListOf(list1, list2, list3, list4, list5)
        val actual = CollectionUtils.merge(list)
        assertEquals(expected, actual)
    }

    @Test
    fun listOf() {
        val expected = listOf(1, 1, 1)
        val actual = CollectionUtils.listOf(1, 3)
    }
}
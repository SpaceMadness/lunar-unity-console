package spacemadness.com.lunarconsole.utils

import org.junit.Assert.*
import org.junit.Test

import spacemadness.com.lunarconsole.TestCase

class ConcurrentModificationListTest : TestCase() {
    @Test
    fun forEach() {
        val a = Callback("a")
        val b = Callback("b")
        val c = Callback("c")

        val list = concurrentListOf(a, b, c)
        assertEquals(3, list.size)
        assertFalse(list.isEmpty)

        list.forEach { index, element ->
            addResult("$index:${element.doStuff()}")
        }

        assertResults("0:a", "1:b", "2:c")
    }

    @Test
    fun add() {
        val list = concurrentListOf<Callback>()
        assertEquals(0, list.size)
        assertTrue(list.isEmpty)

        list.add(Callback("a"))
        assertEquals(1, list.size)
        assertFalse(list.isEmpty)

        list.forEach { index, element ->
            addResult("$index:${element.doStuff()}")
        }
        assertResults("0:a")

        list.add(Callback("b"))
        assertEquals(2, list.size)
        assertFalse(list.isEmpty)
        list.forEach { index, element ->
            addResult("$index:${element.doStuff()}")
        }
        assertResults("0:a", "1:b")

        list.add(Callback("c"))
        assertEquals(3, list.size)
        assertFalse(list.isEmpty)
        list.forEach { index, element ->
            addResult("$index:${element.doStuff()}")
        }

        assertResults("0:a", "1:b", "2:c")
    }

    @Test
    fun remove() {
        val a = Callback("a")
        val b = Callback("b")
        val c = Callback("c")

        val list = concurrentListOf(a, b, c)

        list.remove(b)
        assertEquals(2, list.size)
        assertFalse(list.isEmpty)

        list.forEach { index, element ->
            addResult("$index:${element.doStuff()}")
        }
        assertResults("0:a", "1:c")

        list.remove(a)
        assertEquals(1, list.size)
        assertFalse(list.isEmpty)

        list.forEach { index, element ->
            addResult("$index:${element.doStuff()}")
        }
        assertResults("0:c")

        list.remove(a)
        assertEquals(1, list.size)
        assertFalse(list.isEmpty)

        list.forEach { index, element ->
            addResult("$index:${element.doStuff()}")
        }
        assertResults("0:c")

        list.remove(c)
        assertEquals(0, list.size)
        assertTrue(list.isEmpty)

        list.forEach { index, element ->
            addResult("$index:${element.doStuff()}")
        }
        assertResults()
    }

    @Test
    fun removeForEach() {
        val list = concurrentListOf<Callback>()

        val a = Callback("a")
        val c = Callback("c")
        val b = Callback("b") {
            list.remove(a)
            list.remove(c)
        }

        list.add(a)
        list.add(b)
        list.add(c)

        list.forEach { index, element ->
            addResult("$index:${element.doStuff()}")
        }
        assertEquals(1, list.size)
        assertFalse(list.isEmpty)

        assertResults("0:a", "1:b")

        list.forEach { index, element ->
            addResult("$index:${element.doStuff()}")
        }
        assertEquals(1, list.size)
        assertFalse(list.isEmpty)
        assertResults("0:b")

        list.forEach { index, element ->
            addResult("$index:${element.doStuff()}")
        }
        assertEquals(1, list.size)
        assertFalse(list.isEmpty)
        assertResults("0:b")
    }

    @Test
    fun clearForEach() {
        val list = concurrentListOf<Callback>()

        val a = Callback("a")
        val c = Callback("c")
        val b = Callback("b") {
            list.clear()
        }

        list.add(a)
        list.add(b)
        list.add(c)

        list.forEach { index, element ->
            addResult("$index:${element.doStuff()}")
        }
        assertEquals(0, list.size)
        assertTrue(list.isEmpty)

        assertResults("0:a", "1:b")

        list.forEach { index, element ->
            addResult("$index:${element.doStuff()}")
        }
        assertEquals(0, list.size)
        assertTrue(list.isEmpty)

        assertResults()
    }
}

private data class Callback(
    private val name: String,
    private val callback: ((String) -> Unit)? = null
) {
    fun doStuff(): String {
        callback?.invoke(name)
        return name
    }

    override fun toString() = name
}
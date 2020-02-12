package spacemadness.com.lunarconsole.actions

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun nameCompare() {
        val a1 = Action(id = 0, name = "a3")
        val a2 = Action(id = 1, name = "a1")
        val a3 = Action(id = 2, name = "a2")

        val list = listOf(a1, a2, a3)
        val actual = list.sorted()
        val expected = listOf(a2, a3, a1)
        assertEquals(expected, actual)
    }

    @Test
    fun nameAndGroupCompare() {
        val a1 = Action(id = 0, name = "a3", group = "g1")
        val a2 = Action(id = 1, name = "a1", group = "g2")
        val a3 = Action(id = 2, name = "a2", group = "g1")

        val list = listOf(a1, a2, a3)
        val actual = list.sorted()
        val expected = listOf(a3, a1, a2)
        assertEquals(expected, actual)
    }

    @Test
    fun nameOrGroupCompare() {
        val a1 = Action(id = 1, name = "a1", group = "g2")
        val a2 = Action(id = 0, name = "a3")
        val a3 = Action(id = 2, name = "a2", group = "g1")

        val list = listOf(a1, a2, a3)
        val actual = list.sorted()
        val expected = listOf(a2, a3, a1)
        assertEquals(expected, actual)
    }
}
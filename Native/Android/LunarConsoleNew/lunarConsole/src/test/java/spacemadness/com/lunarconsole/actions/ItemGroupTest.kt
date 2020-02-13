package spacemadness.com.lunarconsole.actions

import org.junit.Test

import org.junit.Assert.*

class ItemGroupTest {
    @Test
    fun testGroup() {
        val group = ItemGroup<Action>(id = 0, name = "Group", isCollapsed = false)
        assertTrue(group.isEmpty)
        assertEquals(0, group.size)

        val collapsed = group.collapse()
        assertFalse(group.isCollapsed)
        assertTrue(collapsed.isCollapsed)

        val expanded = collapsed.expand()
        assertTrue(collapsed.isCollapsed)
        assertFalse(expanded.isCollapsed)
    }
}
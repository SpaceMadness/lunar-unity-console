package spacemadness.com.lunarconsole.reflection

import org.junit.Assert.assertEquals
import org.junit.Test
import spacemadness.com.lunarconsole.TestCase

class PropertyHelperTest : TestCase() {
    @Test
    fun testGetProperty() {
        val parent = Parent()
        parent.child.intValue = 42
        parent.child.boolValue = true
        parent.child.floatValue = 3.14f
        parent.child.stringValue = "value"

        val intProperty = PropertyHelper.getProperty(parent, "child.intValue")
        val boolProperty = PropertyHelper.getProperty(parent, "child.boolValue")
        val floatProperty = PropertyHelper.getProperty(parent, "child.floatValue")
        val stringProperty = PropertyHelper.getProperty(parent, "child.stringValue")

        assertEquals(42, intProperty.value as Int)
        assertEquals(true, boolProperty.value as Boolean)
        assertEquals(3.14f, floatProperty.value as Float)
        assertEquals("value", stringProperty.value as String)

        intProperty.value = 20
        boolProperty.value = false
        floatProperty.value = 6.28f
        stringProperty.value = "new value"

        assertEquals(20, parent.child.intValue)
        assertEquals(false, parent.child.boolValue)
        assertEquals(6.28f, parent.child.floatValue)
        assertEquals("new value", parent.child.stringValue)
    }
}
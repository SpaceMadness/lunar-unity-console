package spacemadness.com.lunarconsole.reflection

import org.junit.Assert.assertEquals
import org.junit.Test
import spacemadness.com.lunarconsole.TestCase

class PropertyHelperTest : TestCase() {
    @Test
    fun testListProperties() {
        val myClass = MyClass()
        myClass.childField = MyClass()

        val properties = PropertyHelper.listProperties(myClass)
        assertEquals(6, properties.size)
        assertEquals(properties[0].toString(), "MyClass.intField")
        assertEquals(properties[1].toString(), "MyClass.floatField")
        assertEquals(properties[2].toString(), "MyClass.boolField")
        assertEquals(properties[3].toString(), "MyClass.stringField")
        assertEquals(properties[4].toString(), "MyClass.enumField")

        val groupProperty = properties[5] as GroupProperty
        assertEquals(5, groupProperty.size())
        assertEquals(groupProperty.children[0].toString(), "MyClass.intField")
        assertEquals(groupProperty.children[1].toString(), "MyClass.floatField")
        assertEquals(groupProperty.children[2].toString(), "MyClass.boolField")
        assertEquals(groupProperty.children[3].toString(), "MyClass.stringField")
        assertEquals(groupProperty.children[4].toString(), "MyClass.enumField")
    }
}
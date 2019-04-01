package spacemadness.com.lunarconsole.reflection

import junit.framework.TestCase
import org.junit.Test

import spacemadness.com.lunarconsole.utils.ClassUtils.getField

class FieldPropertyTest : TestCase() {
    @Test
    fun testSetValue() {
        val myClass = MyClass()
        val intProperty = FieldProperty(myClass, getField(myClass, "intField"))
        intProperty.setValue(1)
        TestCase.assertEquals(1, myClass.intField)

        val floatProperty = FieldProperty(myClass, getField(myClass, "floatField"))
        floatProperty.setValue(1.0f)
        TestCase.assertEquals(1.0f, myClass.floatField)

        val boolProperty = FieldProperty(myClass, getField(myClass, "boolField"))
        boolProperty.setValue(true)
        TestCase.assertTrue(myClass.boolField)

        val stringProperty = FieldProperty(myClass, getField(myClass, "stringField"))
        stringProperty.setValue("value")
        TestCase.assertEquals("value", myClass.stringField)

        val enumProperty = FieldProperty(myClass, getField(myClass, "enumField"))
        enumProperty.setValue(MyEnum.One)
        TestCase.assertEquals(MyEnum.One, myClass.enumField)

        val myChild = MyClass()
        val childProperty = FieldProperty(myClass, getField(myClass, "childField"))
        childProperty.setValue(myChild)
        TestCase.assertSame(myChild, myClass.childField)
    }
}

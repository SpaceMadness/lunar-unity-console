package spacemadness.com.lunarconsole.reflection;

import junit.framework.TestCase;

import static spacemadness.com.lunarconsole.utils.ClassUtils.getField;

public class FieldPropertyTest extends TestCase {
	public void testSetValue() {
		MyClass myClass = new MyClass();
		FieldProperty intProperty = new FieldProperty(myClass, getField(myClass, "intField"));
		intProperty.setValue(1);
		assertEquals(1, myClass.getIntField());

		FieldProperty floatProperty = new FieldProperty(myClass, getField(myClass, "floatField"));
		floatProperty.setValue(1.0f);
		assertEquals(1.0f, myClass.getFloatField());

		FieldProperty boolProperty = new FieldProperty(myClass, getField(myClass, "boolField"));
		boolProperty.setValue(true);
		assertTrue(myClass.getBoolField());

		FieldProperty stringProperty = new FieldProperty(myClass, getField(myClass, "stringField"));
		stringProperty.setValue("value");
		assertEquals("value", myClass.getStringField());

		FieldProperty enumProperty = new FieldProperty(myClass, getField(myClass, "enumField"));
		enumProperty.setValue(MyEnum.One);
		assertEquals(MyEnum.One, myClass.getEnumField());

		MyClass myChild = new MyClass();
		FieldProperty childProperty = new FieldProperty(myClass, getField(myClass, "childField"));
		childProperty.setValue(myChild);
		assertSame(myChild, myClass.getChildField());
	}
}

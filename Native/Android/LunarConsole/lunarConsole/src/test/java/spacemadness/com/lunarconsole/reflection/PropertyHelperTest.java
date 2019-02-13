package spacemadness.com.lunarconsole.reflection;

import junit.framework.TestCase;

import java.util.List;

public class PropertyHelperTest extends TestCase {

	public void testListProperties() {
		MyClass myClass = new MyClass();
		myClass.setChildField(new MyClass());

		List<Property> properties = PropertyHelper.listProperties(myClass);
		assertEquals(6, properties.size());
		assertEquals(properties.get(0).toString(), "MyClass.intField");
		assertEquals(properties.get(1).toString(), "MyClass.floatField");
		assertEquals(properties.get(2).toString(), "MyClass.boolField");
		assertEquals(properties.get(3).toString(), "MyClass.stringField");
		assertEquals(properties.get(4).toString(), "MyClass.enumField");

		GroupProperty groupProperty = (GroupProperty) properties.get(5);
		assertEquals(5, groupProperty.size());
		assertEquals(groupProperty.getChildren().get(0).toString(), "MyClass.intField");
		assertEquals(groupProperty.getChildren().get(1).toString(), "MyClass.floatField");
		assertEquals(groupProperty.getChildren().get(2).toString(), "MyClass.boolField");
		assertEquals(groupProperty.getChildren().get(3).toString(), "MyClass.stringField");
		assertEquals(groupProperty.getChildren().get(4).toString(), "MyClass.enumField");
	}
}
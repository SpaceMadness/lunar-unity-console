package spacemadness.com.lunarconsole.reflection;

import junit.framework.TestCase;

import java.util.List;

public class PropertyHelperTest extends TestCase {

	public void testListProperties() {
		MyClass myClass = new MyClass();

		List<Property> properties = PropertyHelper.listProperties(myClass);
	}
}
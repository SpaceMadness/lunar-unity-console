//
//  JsonDecoderTest.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2015-2020 Alex Lementuev, SpaceMadness.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//


package spacemadness.com.lunarconsole.json;

import java.util.Arrays;

import spacemadness.com.lunarconsole.InstrumentationTestCase;

import static spacemadness.com.lunarconsole.json.Child.createChild;
import static spacemadness.com.lunarconsole.json.Parent.createParent;

public class JsonDecoderTest extends InstrumentationTestCase {
	public void testEncode() {
		Parent object = createParent(
			1,
			1.1f,
			true,
			"parent",
			createChild(
				2,
				2.2f,
				false,
				"child",
				MyEnum.Two
			),
			new Child[]{
				createChild(
					3,
					3.3f,
					true,
					"child-1",
					MyEnum.One
				),
				createChild(
					4,
					4.4f,
					false,
					"child-2",
					MyEnum.Three
				),
			}
		);
		String expected = readTextAsset("json-tests/parent.json");
		String actual = JsonDecoder.encode(object);
		assertEquals(expected, actual);
	}

	public void testDecode() {
		Parent actual = JsonDecoder.decode(readTextAsset("json-tests/parent.json"), Parent.class);
		Parent expected = createParent(
			1,
			1.1f,
			true,
			"parent",
			createChild(
				2,
				2.2f,
				false,
				"child",
				MyEnum.Two
			),
			new Child[]{
				createChild(
					3,
					3.3f,
					true,
					"child-1",
					MyEnum.One
				),
				createChild(
					4,
					4.4f,
					false,
					"child-2",
					MyEnum.Three
				),
			}
		);
		assertEquals(expected, actual);
	}

	public void testMissingProperty() {
		Parent actual = JsonDecoder.decode(readTextAsset("json-tests/parent-missing-property.json"), Parent.class);
		Parent expected = createParent(
			0,
			0.0f,
			false,
			null,
			createChild(
				0,
				0.0f,
				true,
				null,
				null),
			null
		);
		assertEquals(expected, actual);
	}

	public void testMissingRequiredProperty() {
		try {
			JsonDecoder.decode(readTextAsset("json-tests/parent-missing-required-property.json"), Parent.class);
			fail("Should be throwing exception");
		} catch (JsonDecoderException ignored) {
		}
	}
}

class Parent {
	private int intField;
	private float floatField;
	private boolean boolField;
	private String stringField;
	private @Required Child child;
	private Child[] children;

	static Parent createParent(int intField, float floatField, boolean boolField, String stringField, Child child, Child[] children) {
		Parent parent = new Parent();
		parent.intField = intField;
		parent.floatField = floatField;
		parent.boolField = boolField;
		parent.stringField = stringField;
		parent.child = child;
		parent.children = children;
		return parent;
	}

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Parent parent = (Parent) o;

		if (intField != parent.intField) return false;
		if (Float.compare(parent.floatField, floatField) != 0) return false;
		if (boolField != parent.boolField) return false;
		if (stringField != null ? !stringField.equals(parent.stringField) : parent.stringField != null)
			return false;
		if (child != null ? !child.equals(parent.child) : parent.child != null) return false;
		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		return Arrays.equals(children, parent.children);
	}

	@Override public int hashCode() {
		int result = intField;
		result = 31 * result + (floatField != +0.0f ? Float.floatToIntBits(floatField) : 0);
		result = 31 * result + (boolField ? 1 : 0);
		result = 31 * result + (stringField != null ? stringField.hashCode() : 0);
		result = 31 * result + (child != null ? child.hashCode() : 0);
		result = 31 * result + Arrays.hashCode(children);
		return result;
	}
}

enum MyEnum {
	One, Two, Three
}

class Child {
	private @Rename("int") int intField;
	private @Rename("float") float floatField;
	private @Rename("bool") boolean boolField;
	private @Rename("string") String stringField;
	private @Rename("enum") MyEnum enumField;

	static Child createChild(int intField, float floatField, boolean boolField, String stringField, MyEnum enumField) {
		Child child = new Child();
		child.intField = intField;
		child.floatField = floatField;
		child.boolField = boolField;
		child.stringField = stringField;
		child.enumField = enumField;
		return child;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Child child = (Child) o;

		if (intField != child.intField) return false;
		if (Float.compare(child.floatField, floatField) != 0) return false;
		if (boolField != child.boolField) return false;
		if (stringField != null ? !stringField.equals(child.stringField) : child.stringField != null)
			return false;
		return enumField == child.enumField;
	}

	@Override
	public int hashCode() {
		int result = intField;
		result = 31 * result + (floatField != +0.0f ? Float.floatToIntBits(floatField) : 0);
		result = 31 * result + (boolField ? 1 : 0);
		result = 31 * result + (stringField != null ? stringField.hashCode() : 0);
		result = 31 * result + (enumField != null ? enumField.hashCode() : 0);
		return result;
	}
}
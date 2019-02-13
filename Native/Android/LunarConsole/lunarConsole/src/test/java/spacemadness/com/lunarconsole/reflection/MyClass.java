package spacemadness.com.lunarconsole.reflection;

class MyClass {
	private int intField;
	private float floatField;
	private boolean boolField;
	private String stringField;
	private MyEnum enumField;
	private MyClass childField;

	int getIntField() {
		return intField;
	}

	float getFloatField() {
		return floatField;
	}

	boolean getBoolField() {
		return boolField;
	}

	String getStringField() {
		return stringField;
	}

	MyEnum getEnumField() {
		return enumField;
	}

	MyClass getChildField() {
		return childField;
	}

	void setChildField(MyClass childField) {
		this.childField = childField;
	}
}
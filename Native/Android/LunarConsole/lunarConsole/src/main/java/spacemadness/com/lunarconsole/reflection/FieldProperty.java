package spacemadness.com.lunarconsole.reflection;

import java.lang.reflect.Field;

import spacemadness.com.lunarconsole.utils.ClassUtils;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.checkNotNull;

public class FieldProperty extends Property {
	private final Field field;
	private final Object target;

	public FieldProperty(Object target, Field field) {
		super(checkNotNull(field, "field").getName());

		this.field = checkNotNull(field, "field");
		this.target = checkNotNull(target, "target");
	}

	public void setValue(Object value) {
		ClassUtils.setFieldValue(field, target, value);
	}
}

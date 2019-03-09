package spacemadness.com.lunarconsole.reflection;

import java.lang.reflect.Field;

import spacemadness.com.lunarconsole.utils.ClassUtils;

public class PropertyHelper {
	public static FieldProperty getProperty(Object target, String path) {
		String[] components = path.split("\\.");

		Field field = null;
		for (int i = 0; i < components.length; ++i) {
			String component = components[i];

			field = ClassUtils.getField(target, component);
			if (field == null) {
				throw new IllegalArgumentException("Can't find field: " + target.getClass().getName() + "." + component);
			}

			if (i < components.length - 1) {
				target = ClassUtils.getFieldValue(target, field);
			}
		}

		return new FieldProperty(target, field);
	}
}

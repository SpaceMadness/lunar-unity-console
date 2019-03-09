package spacemadness.com.lunarconsole.reflection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import spacemadness.com.lunarconsole.utils.ClassUtils;
import spacemadness.com.lunarconsole.utils.NotImplementedException;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.checkNotNull;

public class PropertyHelper {
	public static FieldProperty getProperty(Object target, String path) {
		String[] components = path.split("/");

		Field field = null;
		for (int i = 0; i < components.length; ++i) {
			String component = components[i];

			field = ClassUtils.getField(target, component);
			if (field == null) {
				throw new IllegalArgumentException("Can't find field: " + target.getClass().getName() + "." + component);
			}
			target = ClassUtils.getFieldValue(target, field);
		}

		return new FieldProperty(target, field);
	}

	public static List<Property> listProperties(Object target) {
		return listProperties(target, NULL_FILTER);
	}

	public static List<Property> listProperties(Object target, ClassUtils.FieldFilter filter) {
		List<Property> result = new ArrayList<>();
		listProperties(checkNotNull(target, "target"), result, filter);
		return result;
	}

	private static void listProperties(Object target, List<Property> result, ClassUtils.FieldFilter filter) {
		List<Field> fields = ClassUtils.listFields(target.getClass(), PROPERTY_FILTER);
		for (Field field : fields) {
			if (!filter.accept(field)) {
				continue;
			}

			Property property = createProperty(target, field);
			if (property != null) {
				result.add(property);
			}
		}
	}

	private static Property createProperty(Object target, Field field) {
		Class<?> fieldType = field.getType();

		if (fieldType.isArray()) {
			throw new NotImplementedException("Array properties are not yet supported: " + field.getName());
		}

		if (isFieldProperty(fieldType)) {
			return new FieldProperty(target, field);
		}

		Object childTarget = ClassUtils.getFieldValue(target, field);
		if (childTarget == null) {
			return null;
		}

		List<Property> children = listProperties(childTarget);
		return new GroupProperty(field.getName(), children);
	}

	private static boolean isFieldProperty(Class<?> fieldType) {
		return fieldType.isPrimitive() || fieldType == String.class || fieldType.isEnum();
	}

	private static final ClassUtils.FieldFilter NULL_FILTER = new ClassUtils.FieldFilter() {
		@Override public boolean accept(Field field) {
			return true;
		}
	};

	private static final ClassUtils.FieldFilter PROPERTY_FILTER = new ClassUtils.FieldFilter() {
		@Override public boolean accept(Field field) {
			return !ClassUtils.isFinal(field) && !ClassUtils.isStatic(field);
		}
	};
}

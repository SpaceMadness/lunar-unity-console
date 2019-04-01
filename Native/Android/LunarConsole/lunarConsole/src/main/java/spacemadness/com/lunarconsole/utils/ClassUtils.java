//
//  ClassUtils.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2019 Alex Lementuev, SpaceMadness.
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

package spacemadness.com.lunarconsole.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.checkNotNull;
import static spacemadness.com.lunarconsole.utils.ObjectUtils.checkNotNullAndNotEmpty;

public final class ClassUtils {
	private static final Class<?>[] EMPTY_PARAMS_TYPES = new Class[0];
	private static final Object[] EMPTY_ARGS = new Object[0];

	public static <T> T newInstance(Class<T> cls) {
		try {
			Constructor<T> constructor = checkNotNull(cls, "cls").getDeclaredConstructor(EMPTY_PARAMS_TYPES);
			constructor.setAccessible(true);
			return constructor.newInstance(EMPTY_ARGS);

		} catch (Exception e) {
			throw new RuntimeException(e); // TODO: custom class
		}
	}

	public static Field[] listFields(Class<?> cls) {
		return cls.getDeclaredFields();
	}

	public static List<Field> listFields(Class<?> cls, FieldFilter filter) {
		final Field[] fields = cls.getDeclaredFields();
		final List<Field> result = new ArrayList<>(fields.length);

		for (Field field : fields) {
			if (filter.accept(field)) {
				result.add(field);
			}
		}

		return result;
	}

	public static Field getField(Object target, String name) {
		Class<?> cls = checkNotNull(target, "target").getClass();
		try {
			return cls.getDeclaredField(checkNotNullAndNotEmpty(name, "name"));
		} catch (NoSuchFieldException e) {
			return null;
		}
	}

	public static Object getFieldValue(Object target, String name) {
		Field field = getField(target, name);
		if (field == null) {
			throw new IllegalArgumentException("Field not found: " + name);
		}
		return getFieldValue(target, field);
	}

	public static Object getFieldValue(Object target, Field field) {
		field.setAccessible(true);
		try {
			return field.get(target);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Unable to get field value: " + field);
		}
	}

	public static Field setFieldValue(Object target, Field field, Object value) {
		field.setAccessible(true);
		try {
			field.set(target, value);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Unable to set field value: " + field);
		}
		return field;
	}

	public static Object getEnumValue(Class<?> cls, int ordinal) {
		try {
			Field valuesField = cls.getDeclaredField("$VALUES");
			valuesField.setAccessible(true);
			Object values = valuesField.get(null);
			return Array.get(values, ordinal);
		} catch (Exception e) {
			throw new RuntimeException("Unable to get enum values", e);
		}
	}

	public static boolean isStatic(Field field) {
		return Modifier.isStatic(field.getModifiers());
	}

	public static boolean isFinal(Field field) {
		return Modifier.isFinal(field.getModifiers());
	}

	public interface FieldFilter {
		boolean accept(Field field);
	}
}

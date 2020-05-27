//
//  JsonDecoder.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2020 Alex Lementuev, SpaceMadness.
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import spacemadness.com.lunarconsole.utils.ClassUtils;
import spacemadness.com.lunarconsole.utils.NotImplementedException;
import spacemadness.com.lunarconsole.utils.ObjectUtils;

public class JsonDecoder {
	/**
	 * Special marker-object to indicate that field should retain its default value.
	 */
	private static final Object DEFAULT = new Object();

	public static String encode(Object object) {
		try {
			return encodeJsonObject(object).toString(2);
		} catch (Exception e) {
			throw new JsonDecoderException(e);
		}
	}

	private static JSONObject encodeJsonObject(Object object) throws JSONException {
		JSONObject jsonObject = new JSONObject();

		// decode fields
		List<Field> fields = ClassUtils.listFields(object.getClass(), FIELD_FILTER);
		for (Field field : fields) {
			final String name = getName(field);
			final Object value = encodeObject(ClassUtils.getFieldValue(object, field));

			jsonObject.put(name, value);
		}

		return jsonObject;
	}

	private static Object encodeObject(Object value) throws JSONException {
		if (value == null) {
			return null;
		}

		if (value instanceof String ||
		    value instanceof Number ||
		    value instanceof Boolean ||
				value instanceof Character) {
			return value;
		}

		Class<?> targetType = value.getClass();
		if (targetType.isPrimitive()) {
			return value;
		}

		if (targetType.isArray()) {
			int length = Array.getLength(value);
			List<Object> encoded = new ArrayList<>(length);
			for (int i = 0; i < length; ++i) {
				encoded.add(encodeObject(Array.get(value, i)));
			}

			return new JSONArray(encoded);
		}

		if (targetType.isEnum()) {
			return value.toString();
		}

		return encodeJsonObject(value);
	}

	public static <T> T decode(String json, Class<T> cls) throws JsonDecoderException {
		try {
			//noinspection unchecked
			return (T) decode(new JSONObject(json), cls);
		} catch (JSONException e) {
			throw new JsonDecoderException(e);
		}
	}

	private static Object decode(JSONObject json, Class<?> cls) {
		// create instance
		Object instance = ClassUtils.newInstance(cls);

		// decode fields
		List<Field> fields = ClassUtils.listFields(cls, FIELD_FILTER);
		for (Field field : fields) {
			final String name = getName(field);
			final Object value = json.opt(name);

			// check required
			if (value == null && isRequired(field)) {
				throw new JsonDecoderException("Missing required field: " + name);
			}

			// decode value
			Object fieldValue = decode(value, field.getType());
			if (fieldValue != DEFAULT) {
				ClassUtils.setFieldValue(instance, field, fieldValue);
			}
		}
		return instance;
	}

	private static Object decode(JSONArray json, Class<?> targetType) {
		Class<?> targetComponentType = targetType.getComponentType();

		int length = json.length();
		Object array = Array.newInstance(targetComponentType, length);
		for (int i = 0; i < length; ++i) {
			try {
				Array.set(array, i, decode(json.get(i), targetComponentType));
			} catch (JSONException e) {
				throw new JsonDecoderException(e);
			}
		}

		return array;
	}

	/**
	 * Transforms value from a decoded json into a target type.
	 */
	private static Object decode(Object jsonValue, Class<?> targetType) {
		if (targetType == String.class) {
			return jsonValue;
		}

		if (targetType.isPrimitive()) {
			// if value is missing - use default
			if (jsonValue == null) {
				return DEFAULT;
			}

			// we need to do explicit down casts here
			if (targetType == float.class && jsonValue.getClass() == Double.class) {
				return ((Double) jsonValue).floatValue();
			}

			return jsonValue;
		}

		if (jsonValue == null) {
			return null;
		}

		if (targetType.isArray()) {
			// json should also contain the array
			JSONArray jsonArray = ObjectUtils.as(jsonValue, JSONArray.class);
			if (jsonArray == null) {
				throw new JsonDecoderException("Expected array but was: " + jsonValue.getClass());
			}

			return decode(jsonArray, targetType);
		}

		if (jsonValue instanceof JSONObject) {
			return decode((JSONObject) jsonValue, targetType);
		}

		if (targetType.isEnum()) {
			if (jsonValue instanceof String) {
				//noinspection unchecked
				return Enum.valueOf((Class<? extends Enum>) targetType, (String) jsonValue);
			}

			if (jsonValue instanceof Integer) {
				int ordinal = (Integer) jsonValue;
				return ClassUtils.getEnumValue(targetType, ordinal);
			}

			throw new NotImplementedException("Invalid enum value: " + jsonValue);
		}

		throw new NotImplementedException("Type not supported: " + targetType);
	}

	private static String getName(Field field) {
		Rename rename = field.getAnnotation(Rename.class);
		return rename != null ? rename.value() : field.getName();
	}

	private static boolean isRequired(Field field) {
		return field.getAnnotation(Required.class) != null;
	}

	private static final ClassUtils.FieldFilter FIELD_FILTER = new ClassUtils.FieldFilter() {
		@Override public boolean accept(Field field) {
			return !ClassUtils.isStatic(field) && !ClassUtils.isFinal(field);
		}
	};
}

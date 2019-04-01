//
//  FieldProperty.java
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

	public Class<?> getType() {
		return field.getType();
	}

	public Object getValue() {
		return ClassUtils.getFieldValue(target, field);
	}

	public void setValue(Object value) {
		ClassUtils.setFieldValue(target, field, value);
	}

	@Override public String toString() {
		return target.getClass().getSimpleName() + "." + field.getName();
	}
}

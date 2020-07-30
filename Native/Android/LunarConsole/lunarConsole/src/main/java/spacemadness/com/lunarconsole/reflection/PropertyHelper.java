//
//  PropertyHelper.java
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

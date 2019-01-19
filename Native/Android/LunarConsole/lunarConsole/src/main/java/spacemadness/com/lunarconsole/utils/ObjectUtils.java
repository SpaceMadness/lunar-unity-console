//
//  ObjectUtils.java
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

public class ObjectUtils
{
    public static boolean areEqual(Object o1, Object o2)
    {
        return o1 != null && o1.equals(o2) || o1 == null && o2 == null;
    }

    public static <T> T notNullOrDefault(T obj, T defaultObj)
    {
        if (defaultObj == null)
        {
            throw new NullPointerException("Default object is null");
        }

        return obj != null ? obj : defaultObj;
    }

    public static String toString(Object obj)
    {
        return obj != null ? obj.toString() : null;
    }

    public static <T> T as(Object obj, Class<? extends T> cls)
    {
        return cls.isInstance(obj) ? (T) obj : null;
    }
}

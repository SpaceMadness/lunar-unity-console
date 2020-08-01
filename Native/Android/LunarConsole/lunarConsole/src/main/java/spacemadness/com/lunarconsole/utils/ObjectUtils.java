//
//  ObjectUtils.java
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


package spacemadness.com.lunarconsole.utils;

import spacemadness.com.lunarconsole.core.Check;

/**
 * A collection of useful object utils.
 */
public final class ObjectUtils {
    /* Avoid creating instances */
    private ObjectUtils() {
    }

    /**
     * Throws <code>IllegalArgumentException</code> if the passed reference is <code>null</code>.
     */
    @Deprecated
    public static <T> T checkNotNull(T arg, String name) throws IllegalArgumentException {
        return Check.notNullArg(arg, name);
    }

    /**
     * Throws <code>IllegalArgumentException</code> if the passed string is <code>null</code> or empty.
     */
    @Deprecated
    public static String checkNotNullAndNotEmpty(String arg, String name) throws IllegalArgumentException {
        return Check.notEmptyArg(arg, name);
    }

    /**
     * @return <code>true</code> if objects are equal (<code>null</code>-safe).
     */
    public static boolean areEqual(Object o1, Object o2) {
        return o1 != null && o1.equals(o2) || o1 == null && o2 == null;
    }

    /**
     * @return <code>obj.toString()</code> (<code>null</code>-safe)
     */
    public static String toString(Object obj) {
        return obj != null ? obj.toString() : null;
    }

    /**
     * Safe cast operation.
     *
     * @return <code>null</code> instead of throwing <code>ClassCastException.</code>
     */
    public static <T> T as(Object obj, Class<? extends T> cls) {
        return cls.isInstance(obj) ? (T) obj : null;
    }
}

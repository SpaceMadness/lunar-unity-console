//
//  StringUtils.java
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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StringUtils
{
    private static final NumberFormat FLOATING_POINT_FORMAT = new DecimalFormat("0.#");

    public static boolean isValidInteger(String str)
    {
        try
        {
            Integer.parseInt(str);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    public static boolean isValidFloat(String str)
    {
        try
        {
            Float.parseFloat(str);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    public static int parseInt(String str, int defaultValue)
    {
        try
        {
            return Integer.parseInt(str);
        }
        catch (NumberFormatException e)
        {
            return defaultValue;
        }
    }

    public static float parseFloat(String str, float defaultValue)
    {
        try
        {
            return Float.parseFloat(str);
        }
        catch (NumberFormatException e)
        {
            return defaultValue;
        }
    }

    public static int length(String str)
    {
        return str != null ? str.length() : 0;
    }

    public static boolean contains(String str, CharSequence cs)
    {
        return str != null && cs != null && str.contains(cs);
    }

    public static boolean containsIgnoreCase(String str, String cs)
    {
        return str != null && cs != null &&
               str.length() >= cs.length() &&
               str.toLowerCase().contains(cs.toLowerCase());
    }

    public static boolean hasPrefix(String str, String prefix)
    {
        return str != null && prefix != null && str.startsWith(prefix);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Transformations

    public static String camelCaseToWords(String string)
    {
        if (IsNullOrEmpty(string)) return string;

        StringBuilder result = new StringBuilder(string.length());
        result.append(Character.toUpperCase(string.charAt(0)));

        for (int i = 1; i < string.length(); ++i)
        {
            char chr = string.charAt(i);
            if (Character.isUpperCase(chr))
            {
                result.append(' ');
            }
            result.append(chr);
        }

        return result.toString();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Nullability

    public static boolean IsNullOrEmpty(String str)
    {
        return str == null || str.length() == 0;
    }

    public static String nullOrNonEmpty(String str)
    {
        return IsNullOrEmpty(str) ? null : str;
    }

    public static String NonNullOrEmpty(String str)
    {
        return str != null ? str : "";
    }

    public static String toString(Object value)
    {
        return value != null ? value.toString() : "null";
    }

    public static String toString(float value)
    {
        return FLOATING_POINT_FORMAT.format(value);
    }

    public static String toString(double value)
    {
        return FLOATING_POINT_FORMAT.format(value);
    }

    public static <T> String Join(List<T> list)
    {
        return Join(list, ",");
    }

    public static <T> String Join(List<T> list, String separator)
    {
        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (T e : list)
        {
            builder.append(e);
            if (++i < list.size()) builder.append(separator);
        }
        return builder.toString();
    }

    public static <T> String Join(T[] array)
    {
        return Join(array, ",");
    }

    public static <T> String Join(T[] array, String separator)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; ++i)
        {
            builder.append(array[i]);
            if (i < array.length-1) builder.append(separator);
        }
        return builder.toString();
    }

    public static String Join(boolean[] array)
    {
        return Join(array, ",");
    }

    public static String Join(boolean[] array, String separator)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; ++i)
        {
            builder.append(array[i]);
            if (i < array.length-1) builder.append(separator);
        }
        return builder.toString();
    }

    public static String Join(byte[] array)
    {
        return Join(array, ",");
    }

    public static String Join(byte[] array, String separator)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; ++i)
        {
            builder.append(array[i]);
            if (i < array.length-1) builder.append(separator);
        }
        return builder.toString();
    }

    public static String Join(short[] array)
    {
        return Join(array, ",");
    }

    public static String Join(short[] array, String separator)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; ++i)
        {
            builder.append(array[i]);
            if (i < array.length-1) builder.append(separator);
        }
        return builder.toString();
    }

    public static String Join(char[] array)
    {
        return Join(array, ",");
    }

    public static String Join(char[] array, String separator)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; ++i)
        {
            builder.append(array[i]);
            if (i < array.length-1) builder.append(separator);
        }
        return builder.toString();
    }

    public static String Join(int[] array)
    {
        return Join(array, ",");
    }

    public static String Join(int[] array, String separator)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; ++i)
        {
            builder.append(array[i]);
            if (i < array.length-1) builder.append(separator);
        }
        return builder.toString();
    }

    public static String Join(long[] array)
    {
        return Join(array, ",");
    }

    public static String Join(long[] array, String separator)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; ++i)
        {
            builder.append(array[i]);
            if (i < array.length-1) builder.append(separator);
        }
        return builder.toString();
    }

    public static String Join(float[] array)
    {
        return Join(array, ",");
    }

    public static String Join(float[] array, String separator)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; ++i)
        {
            builder.append(array[i]);
            if (i < array.length-1) builder.append(separator);
        }
        return builder.toString();
    }

    public static String Join(double[] array)
    {
        return Join(array, ",");
    }

    public static String Join(double[] array, String separator)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; ++i)
        {
            builder.append(array[i]);
            if (i < array.length-1) builder.append(separator);
        }
        return builder.toString();
    }

    public static String TryFormat(String format, Object... args) // FIXME: rename
    {
        if (format != null && args != null && args.length > 0)
        {
            try
            {
                return String.format(format, args);
            }
            catch (Exception e)
            {
                android.util.Log.e("Lunar", "Error while formatting String: " + e.getMessage()); // FIXME: better system loggingb
            }
        }

        return format;
    }

    public static String serializeToString(Map<String, ?> data)
    {
        StringBuilder result = new StringBuilder();
        int index = 0;
        for (Map.Entry<String, ?> e : data.entrySet())
        {
            String key = e.getKey();
            String value = toString(e.getValue());
            value = value.replace("\n", "\\n"); // we use new lines as separators
            result.append(key);
            result.append(':');
            result.append(value);

            if (++index < data.size())
            {
                result.append("\n");
            }
        }

        return result.toString();
    }
}

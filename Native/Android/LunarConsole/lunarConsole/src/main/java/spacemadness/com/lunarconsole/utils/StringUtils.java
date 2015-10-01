package spacemadness.com.lunarconsole.utils;

import java.util.List;

public class StringUtils
{
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
}



using System;
using System.Collections.Generic;

public static class Extensions
{
    public static List<R> Map<T, R>(this IList<T> list, Func<T, R> mapper)
    {
        List<R> result = new List<R>(list.Count);
        foreach (T item in list)
        {
            result.Add(mapper(item));
        }
        return result;
    }
}
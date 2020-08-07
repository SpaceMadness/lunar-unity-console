using System.Collections.Generic;
using System.Text;

namespace LunarConsolePluginInternal
{
    public static class Collections
    {
        public static string Join<T>(this IList<T> list, string separator = ",")
        {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < list.Count; ++i)
            {
                builder.Append(list[i]);
                if (i < list.Count - 1) builder.Append(separator);
            }

            return builder.ToString();
        }
    }
}
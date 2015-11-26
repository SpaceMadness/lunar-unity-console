package spacemadness.com.lunarconsole.utils;

import spacemadness.com.lunarconsole.debug.Log;

public class StackTrace
{
    public static final String MARKER_AT = " (at ";
    public static final String MARKER_ASSETS = "/Assets/";

    public static String optimize(String stackTrace)
    {
        try
        {
            if (stackTrace != null && stackTrace.length() > 0)
            {
                String[] lines = stackTrace.split("\n");
                String[] newLines = new String[lines.length];
                for (int i = 0; i < lines.length; ++i)
                {
                    newLines[i] = optimizeLine(lines[i]);
                }

                return StringUtils.Join(newLines, "\n");
            }
        }
        catch (Exception e)
        {
            Log.e(e, "Error while optimizing stacktrace: %s", stackTrace);
        }

        return stackTrace;
    }

    private static String optimizeLine(String line)
    {
        int start = line.indexOf(MARKER_AT);
        if (start == -1) return line;

        int end = line.lastIndexOf(MARKER_ASSETS);
        if (end == -1) return line;

        return line.substring(0, start + MARKER_AT.length()) + line.substring(end + 1);
    }
}

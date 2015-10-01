package spacemadness.com.lunarconsole.debug;

import spacemadness.com.lunarconsole.Config;
import spacemadness.com.lunarconsole.utils.StringUtils;

import static android.util.Log.DEBUG;
import static android.util.Log.ERROR;
import static android.util.Log.INFO;
import static android.util.Log.WARN;

public class Log
{
    public enum LogLevel
    {
        Crit(ERROR),
        Error(ERROR),
        Warn(WARN),
        Info(INFO),
        Debug(DEBUG),
        None(-1);

        private int androidLogPriority;

        LogLevel(int priority)
        {
            androidLogPriority = priority;
        }

        public int getAndroidLogPriority()
        {
            return androidLogPriority;
        }
    }

    private static final String TAG = "LunarConsole";

    private static final LogLevel logLevel = Config.DEBUG ? LogLevel.Debug : LogLevel.Info;

    public static void i(String format, Object... args)
    {
        i(null, format, args);
    }

    public static void i(Tag tag, String format, Object... args)
    {
        log(LogLevel.Info, tag, format, args);
    }

    public static void w(String format, Object... args)
    {
        w(null, format, args);
    }

    public static void w(Tag tag, String format, Object... args)
    {
        log(LogLevel.Warn, tag, format, args);
    }

    public static void d(String format, Object... args)
    {
        d(null, format, args);
    }

    public static void d(Tag tag, String format, Object... args)
    {
        log(LogLevel.Debug, tag, format, args);
    }

    public static void e(Tag tag, String format, Object... args)
    {
        log(LogLevel.Error, tag, format, args);
    }

    public static void e(String format, Object... args)
    {
        e((Tag) null, format, args);
    }

    public static void e(Throwable t, String format, Object... args)
    {
        e(format, args);
        if (t != null)
        {
            t.printStackTrace();
        }
    }

    private static void log(LogLevel level, Tag tag, String format, Object... args)
    {
        if (shouldLogLevel(level) && shouldLogTag(tag))
        {
            if (format != null)
            {
                logHelper(level, format, args);
            }
            else
            {
                logHelper(level, "null");
            }
        }
    }

    private static void logHelper(LogLevel level, String format, Object... args)
    {
        int priority = level.getAndroidLogPriority();

        String message = StringUtils.TryFormat(format, args);
        String threadName = Thread.currentThread().getName();
        String tag = TAG + "/" + threadName;
        android.util.Log.println(priority, tag, message);
    }

    private static boolean shouldLogLevel(LogLevel level)
    {
        return level.ordinal() <= logLevel.ordinal();
    }

    private static boolean shouldLogTag(Tag tag)
    {
        return tag == null || tag.enabled;
    }
}
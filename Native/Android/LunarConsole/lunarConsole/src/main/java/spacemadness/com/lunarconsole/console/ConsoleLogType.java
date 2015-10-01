package spacemadness.com.lunarconsole.console;

public final class ConsoleLogType
{
    public static final byte ERROR = 0;
    public static final byte ASSERT = 1;
    public static final byte WARNING = 2;
    public static final byte LOG = 3;
    public static final byte EXCEPTION = 4;

    public static final byte COUNT = 5;

    public static boolean isErrorType(int type)
    {
        return type == EXCEPTION ||
               type == ERROR ||
               type == ASSERT;
    }

    public static boolean isValidType(int type)
    {
        return type >= 0 && type < COUNT;
    }

    public static int getMask(int type)
    {
        return 1 << type;
    }

    /* The class should not be instantiated */
    private ConsoleLogType()
    {
    }
}

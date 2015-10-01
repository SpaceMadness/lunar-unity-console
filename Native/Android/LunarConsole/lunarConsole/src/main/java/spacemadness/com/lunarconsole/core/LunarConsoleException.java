package spacemadness.com.lunarconsole.core;

public class LunarConsoleException extends RuntimeException
{
    public LunarConsoleException(String detailMessage)
    {
        super(detailMessage);
    }

    public LunarConsoleException(String detailMessage, Throwable throwable)
    {
        super(detailMessage, throwable);
    }
}

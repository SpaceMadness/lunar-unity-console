package spacemadness.com.lunarconsole.console;

/**
 * Base class for any entity which might be displayed on the screen in a
 * table-like fashion: log messages, actions, variables, etc
 */
public abstract class ConsoleEntry
{
    public abstract long getItemId();
}

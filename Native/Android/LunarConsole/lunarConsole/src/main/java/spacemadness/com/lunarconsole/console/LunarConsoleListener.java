package spacemadness.com.lunarconsole.console;

public interface LunarConsoleListener
{
    void onAddEntry(Console console, ConsoleEntry entry, boolean filtered);
    void onRemoveEntries(Console console, int start, int length);
    void onClearEntries(Console console);
}

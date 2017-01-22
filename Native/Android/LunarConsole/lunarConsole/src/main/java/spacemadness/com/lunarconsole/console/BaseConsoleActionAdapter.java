package spacemadness.com.lunarconsole.console;

import spacemadness.com.lunarconsole.console.actions.LUEntry;

public abstract class BaseConsoleActionAdapter<T extends LUEntry> extends BaseConsoleAdapter<T>
{
    public BaseConsoleActionAdapter(DataSource<T> dataSource)
    {
        super(dataSource);
    }
}

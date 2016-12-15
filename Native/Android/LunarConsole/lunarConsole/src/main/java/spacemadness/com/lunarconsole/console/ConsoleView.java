package spacemadness.com.lunarconsole.console;

import android.app.Activity;
import android.widget.HorizontalScrollView;

import spacemadness.com.lunarconsole.core.Destroyable;

public class ConsoleView extends HorizontalScrollView implements Destroyable
{
    private final ConsoleLogView consoleLogView;
    private final ConsoleActionView consoleActionView;

    private Listener listener;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors

    public ConsoleView(Activity activity, ConsolePlugin consolePlugin)
    {
        super(activity);

        if (consolePlugin == null)
        {
            throw new NullPointerException("Console plugin is null");
        }

        consoleLogView = new ConsoleLogView(activity, consolePlugin.getConsole());
        consoleActionView = new ConsoleActionView(activity, consolePlugin.getActionRegistry());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Destroyable

    @Override
    public void destroy()
    {
        consoleLogView.destroy();
        consoleActionView.destroy();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Listener notifications

    void notifyOpen()
    {
        if (listener != null)
        {
            listener.onOpen(this);
        }
    }

    void notifyClose()
    {
        if (listener != null)
        {
            listener.onClose(this);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    public Listener getListener()
    {
        return listener;
    }

    public void setListener(Listener listener)
    {
        this.listener = listener;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Listener

    public interface Listener
    {
        void onOpen(ConsoleView view);
        void onClose(ConsoleView view);
    }
}

package spacemadness.com.lunarconsole.console;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.core.Destroyable;
import spacemadness.com.lunarconsole.ui.ViewPager;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class ConsoleView extends LinearLayout implements Destroyable
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

        View rootView = LayoutInflater.from(activity).inflate(R.layout.lunar_console_layout_console_view, this, false);
        addView(rootView, new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));

        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.lunar_console_view_pager);

        consoleLogView = new ConsoleLogView(activity, consolePlugin.getConsole());
        viewPager.addPageView(consoleLogView);

        consoleActionView = new ConsoleActionView(activity, consolePlugin.getActionRegistry());
        viewPager.addPageView(consoleActionView);
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

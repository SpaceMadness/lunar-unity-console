package spacemadness.com.lunarconsole.console;

import android.app.Activity;
import android.view.View;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.core.Destroyable;

class ConsoleActionView extends AbstractConsoleView implements Destroyable
{
    public ConsoleActionView(Activity activity, ConsolePlugin consolePlugin)
    {
        super(activity, R.layout.lunar_console_layout_console_action_view);

        View contentView = findViewById(R.id.lunar_console_actions_view);
        View warningView = findViewById(R.id.lunar_console_actions_warning_view);

        warningView.setVisibility(VISIBLE);
        contentView.setVisibility(GONE);
    }

    @Override
    public void destroy()
    {
    }
}

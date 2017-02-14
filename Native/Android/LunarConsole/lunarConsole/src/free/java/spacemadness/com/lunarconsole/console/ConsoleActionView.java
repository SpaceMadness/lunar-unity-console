package spacemadness.com.lunarconsole.console;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.core.Destroyable;
import spacemadness.com.lunarconsole.utils.UIUtils;

class ConsoleActionView extends AbstractConsoleView implements Destroyable
{
    public ConsoleActionView(Activity activity, ConsolePlugin consolePlugin)
    {
        super(activity, R.layout.lunar_console_layout_console_action_view);

        View contentView = findViewById(R.id.lunar_console_actions_view);
        View warningView = findViewById(R.id.lunar_console_actions_warning_view);

        warningView.setVisibility(VISIBLE);
        contentView.setVisibility(GONE);

        Button getProButton = (Button) findViewById(R.id.lunar_console_no_actions_button_help);
        getProButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Context context = getContext();
                UIUtils.openURL(context, context.getString(R.string.lunar_console_url_actions_get_pro_version));
            }
        });
    }

    @Override
    public void destroy()
    {
    }
}

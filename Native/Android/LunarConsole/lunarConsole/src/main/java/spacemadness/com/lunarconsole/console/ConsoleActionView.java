package spacemadness.com.lunarconsole.console;

import android.app.Activity;
import android.widget.LinearLayout;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.console.actions.LUAction;
import spacemadness.com.lunarconsole.console.actions.LUActionRegistry;
import spacemadness.com.lunarconsole.console.actions.LUActionRegistryFilter;
import spacemadness.com.lunarconsole.console.actions.LUCVar;
import spacemadness.com.lunarconsole.core.Destroyable;
import spacemadness.com.lunarconsole.utils.NotImplementedException;

public class ConsoleActionView extends AbstractConsoleView implements Destroyable
{
    private final LUActionRegistryFilter _actionRegistryFilter;

    public ConsoleActionView(Activity activity, LUActionRegistry actionRegistry)
    {
        super(activity, R.layout.lunar_console_layout_console_action_view);

        _actionRegistryFilter = new LUActionRegistryFilter(actionRegistry);
        _actionRegistryFilter.setDelegate(new LUActionRegistryFilter.Delegate()
        {
            @Override
            public void actionRegistryFilterDidAddAction(LUActionRegistryFilter registryFilter, LUAction action, int index)
            {
                throw new NotImplementedException();
            }

            @Override
            public void actionRegistryFilterDidRemoveAction(LUActionRegistryFilter registryFilter, LUAction action, int index)
            {
                throw new NotImplementedException();
            }

            @Override
            public void actionRegistryFilterDidRegisterVariable(LUActionRegistryFilter registryFilter, LUCVar variable, int index)
            {
                throw new NotImplementedException();
            }

            @Override
            public void actionRegistryFilterDidChangeVariable(LUActionRegistryFilter registryFilter, LUCVar variable, int index)
            {
                throw new NotImplementedException();
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Destroyable

    @Override
    public void destroy()
    {
        // TODO: destroy stuff
    }
}

package spacemadness.com.lunarconsole.console;

import android.app.Activity;
import android.view.View;

import java.lang.ref.WeakReference;

import spacemadness.com.lunarconsole.core.LunarConsoleException;
import spacemadness.com.lunarconsole.utils.UIUtils;

class DefaultPluginImp implements ConsolePluginImp
{
    private final WeakReference<View> rootViewRef;

    public DefaultPluginImp(Activity activity)
    {
        View rootView = UIUtils.getRootViewGroup(activity);
        if (rootView == null)
        {
            throw new LunarConsoleException("Can't initialize plugin: root view not found");
        }

        rootViewRef = new WeakReference<>(rootView);
    }

    @Override
    public View getTouchRecepientView()
    {
        return rootViewRef.get();
    }
}

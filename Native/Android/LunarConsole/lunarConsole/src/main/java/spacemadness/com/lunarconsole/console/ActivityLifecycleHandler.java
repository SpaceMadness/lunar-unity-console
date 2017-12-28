package spacemadness.com.lunarconsole.console;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;

import java.lang.ref.WeakReference;

import spacemadness.com.lunarconsole.core.Destroyable;
import spacemadness.com.lunarconsole.core.NotificationCenter;

import static spacemadness.com.lunarconsole.console.Notifications.NOTIFICATION_ACTIVITY_STARTED;
import static spacemadness.com.lunarconsole.console.Notifications.NOTIFICATION_ACTIVITY_STOPPED;
import static spacemadness.com.lunarconsole.console.Notifications.NOTIFICATION_KEY_ACTIVITY;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
class ActivityLifecycleHandler implements Application.ActivityLifecycleCallbacks, Destroyable
{
    private final WeakReference<Application> applicationRef;

    public ActivityLifecycleHandler(Application application)
    {
        if (application == null)
        {
            throw new IllegalArgumentException("Application is null");
        }
        applicationRef = new WeakReference<>(application);
        application.registerActivityLifecycleCallbacks(this);
    }

    //region Activity Lifecycle Callbacks

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState)
    {
    }

    @Override
    public void onActivityStarted(Activity activity)
    {
        NotificationCenter.defaultCenter()
                .postNotification(NOTIFICATION_ACTIVITY_STARTED, NOTIFICATION_KEY_ACTIVITY, activity);
    }

    @Override
    public void onActivityResumed(Activity activity)
    {
    }

    @Override
    public void onActivityPaused(Activity activity)
    {
    }

    @Override
    public void onActivityStopped(Activity activity)
    {
        NotificationCenter.defaultCenter()
                .postNotification(NOTIFICATION_ACTIVITY_STOPPED, NOTIFICATION_KEY_ACTIVITY, activity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState)
    {
    }

    @Override
    public void onActivityDestroyed(Activity activity)
    {
    }

    //endregion

    //region Destroyable

    @Override
    public void destroy()
    {
        Application application = getApplication();
        if (application != null)
        {
            application.unregisterActivityLifecycleCallbacks(this);
        }
    }

    //endregion

    //region Helpers

    private Application getApplication()
    {
        return applicationRef.get();
    }

    //endregion
}

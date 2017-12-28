package spacemadness.com.lunarconsole.console;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import java.lang.ref.WeakReference;

import spacemadness.com.lunarconsole.core.Destroyable;
import spacemadness.com.lunarconsole.core.NotificationCenter;
import spacemadness.com.lunarconsole.debug.Log;

import static spacemadness.com.lunarconsole.console.Notifications.NOTIFICATION_APP_ENTER_BACKGROUND;
import static spacemadness.com.lunarconsole.console.Notifications.NOTIFICATION_APP_ENTER_FOREGROUND;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
class ActivityLifecycleHandler implements Application.ActivityLifecycleCallbacks, Destroyable
{
    private static final long CHECK_DELAY = 1000;

    private final Handler handler;
    private final WeakReference<Application> applicationRef;
    private Runnable foregroundCheckRunnable;
    private boolean foregroundFlag = true;
    private int foregroundActivitiesCount = 1;

    public ActivityLifecycleHandler(Application application)
    {
        if (application == null)
        {
            throw new IllegalArgumentException("Application is null");
        }
        applicationRef = new WeakReference<>(application);
        handler = new Handler(Looper.getMainLooper());
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
        boolean oldForegroundFlag = !foregroundFlag;
        foregroundFlag = true;

        if (foregroundCheckRunnable != null)
        {
            handler.removeCallbacks(foregroundCheckRunnable);
            foregroundCheckRunnable = null;
        }

        ++foregroundActivitiesCount;
        if (foregroundActivitiesCount == 1 && oldForegroundFlag)
        {
            notifyAppEnteredForeground();
        }
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
        --foregroundActivitiesCount;
        if (foregroundActivitiesCount < 0)
        {
            Log.e("Invalid foreground activities count: %d", foregroundActivitiesCount);
            foregroundActivitiesCount = 0;
        }

        if (foregroundCheckRunnable != null)
        {
            handler.removeCallbacks(foregroundCheckRunnable);
        }

		/* When one activity transits to another one, there is a brief period during which the former
         * is paused but the latter has not yet resumed. To prevent false negative, check routine is
         * delayed
         */
        handler.postDelayed(foregroundCheckRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                if (foregroundActivitiesCount == 0 && foregroundFlag)
                {
                    foregroundFlag = false;
                    notifyAppEnteredBackground();
                }
            }
        }, CHECK_DELAY);
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

    private void notifyAppEnteredForeground()
    {
        Log.d("Application entered foreground");
        NotificationCenter.defaultCenter().
                postNotification(NOTIFICATION_APP_ENTER_FOREGROUND);
    }

    private void notifyAppEnteredBackground()
    {
        Log.d("Application entered background");
        NotificationCenter.defaultCenter().
                postNotification(NOTIFICATION_APP_ENTER_BACKGROUND);
    }

    private Application getApplication()
    {
        return applicationRef.get();
    }

    //endregion
}

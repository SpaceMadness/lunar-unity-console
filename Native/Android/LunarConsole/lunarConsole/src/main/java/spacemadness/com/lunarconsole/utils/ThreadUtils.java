package spacemadness.com.lunarconsole.utils;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import spacemadness.com.lunarconsole.debug.Log;

public class ThreadUtils
{
    private final Handler handler;

    private ThreadUtils()
    {
        handler = new Handler(Looper.getMainLooper());
    }

    private void postRunnable(Runnable runnable)
    {
        handler.post(runnable);
    }

    public static void runOnUIThread(Runnable runnable)
    {
        Holder.INSTANCE.postRunnable(runnable);
    }

    public static void cancel(Runnable runnable)
    {
        Holder.INSTANCE.cancelRunnable(runnable);
    }

    private void cancelRunnable(Runnable runnable)
    {
        handler.removeCallbacks(runnable);
    }

    public static void cancelAll()
    {
        Holder.INSTANCE.cancelRunnables();
    }

    private void cancelRunnables()
    {
        handler.removeCallbacks(null);
    }

    public static boolean isRunningOnMainThread()
    {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    private static class Holder
    {
        private static final ThreadUtils INSTANCE = new ThreadUtils();
    }
}

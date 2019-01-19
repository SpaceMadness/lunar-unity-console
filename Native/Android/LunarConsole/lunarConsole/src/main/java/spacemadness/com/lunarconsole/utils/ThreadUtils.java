//
//  ThreadUtils.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2019 Alex Lementuev, SpaceMadness.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

package spacemadness.com.lunarconsole.utils;

import android.os.Handler;
import android.os.Looper;

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

    private void postRunnable(Runnable runnable, long delay)
    {
        handler.postDelayed(runnable, delay);
    }

    public static void runOnUIThread(Runnable runnable)
    {
        Holder.INSTANCE.postRunnable(runnable);
    }

    public static void runOnUIThread(Runnable runnable, long delay)
    {
        Holder.INSTANCE.postRunnable(runnable, delay);
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

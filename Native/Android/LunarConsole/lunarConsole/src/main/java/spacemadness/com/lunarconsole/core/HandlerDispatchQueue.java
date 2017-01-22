package spacemadness.com.lunarconsole.core;

import android.os.Handler;
import android.os.Looper;

public class HandlerDispatchQueue extends DispatchQueue
{
    private final Handler handler;

    public HandlerDispatchQueue(Looper looper)
    {
        handler = new Handler(looper);
    }

    @Override
    public void dispatchAsync(Runnable runnable)
    {
        handler.post(runnable);
    }
}
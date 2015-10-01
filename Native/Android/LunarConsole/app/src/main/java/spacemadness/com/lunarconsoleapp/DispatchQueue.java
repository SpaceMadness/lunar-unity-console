package spacemadness.com.lunarconsoleapp;

import android.os.Handler;
import android.os.Looper;

public class DispatchQueue
{
    private final Handler handler;

    public DispatchQueue(Looper looper)
    {
        this(new Handler(looper));
    }

    public DispatchQueue(Handler handler)
    {
        if (handler == null)
        {
            throw new NullPointerException("Handler is null");
        }
        this.handler = handler;
    }

    public void dispatch(Runnable r)
    {
        handler.post(r);
    }
}

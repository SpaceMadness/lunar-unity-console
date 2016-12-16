package spacemadness.com.lunarconsole.core;

import android.os.Looper;

public abstract class DispatchQueue implements Destroyable
{
    public abstract void dispatchAsync(Runnable runnable);

    @Override
    public void destroy()
    {
    }

    public static DispatchQueue mainQueue()
    {
        return Holder.INSTANCE;
    }

    private static class Holder
    {
        private static final DispatchQueue INSTANCE = new HandlerDispatchQueue(Looper.getMainLooper());
    }
}

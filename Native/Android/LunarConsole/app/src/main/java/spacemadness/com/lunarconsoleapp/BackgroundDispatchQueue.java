package spacemadness.com.lunarconsoleapp;

import android.os.HandlerThread;

public class BackgroundDispatchQueue extends DispatchQueue
{
    private final HandlerThread handlerThread;

    protected BackgroundDispatchQueue(HandlerThread handlerThread)
    {
        super(handlerThread.getLooper());
        this.handlerThread = handlerThread;
    }

    public static BackgroundDispatchQueue create(String name)
    {
        HandlerThread t = new HandlerThread(name);
        t.start();
        return new BackgroundDispatchQueue(t);
    }
}

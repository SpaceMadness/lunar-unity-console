package spacemadness.com.lunarconsoleapp.helpers;

import spacemadness.com.lunarconsole.core.DispatchQueue;

/**
 * Created by alementuev on 1/26/17.
 */

public class SyncDispatchQueue extends DispatchQueue
{
    @Override
    public void dispatchAsync(Runnable runnable)
    {
        runnable.run();
    }
}

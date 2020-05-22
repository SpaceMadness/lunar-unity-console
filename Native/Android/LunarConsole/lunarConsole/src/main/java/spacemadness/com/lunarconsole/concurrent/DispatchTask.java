package spacemadness.com.lunarconsole.concurrent;

import spacemadness.com.lunarconsole.debug.Log;

/**
 * A basic class for any dispatch runnable task. Tracks its "schedule" state
 */
public abstract class DispatchTask implements Runnable {

    /**
     * Optional name of the task
     */
    private final String name;

    /**
     * True if task is already on the queue and would be executed soon.
     */
    private boolean scheduled;

    /**
     * True if task is cancelled and should not be executed.
     */
    private boolean cancelled;

    public DispatchTask() {
        this(null);
    }

    public DispatchTask(String name) {
        this.name = name;
    }

    /**
     * Task entry point method
     */
    protected abstract void execute();

    @Override
    public void run() {
        try {
            setScheduled(false);

            if (!isCancelled()) {
                execute();
            }
        } catch (Exception e) {
            Log.e(e, name != null ? "Exception while executing task: " + name : "Exception while executing task");
        } finally {
            setCancelled(false);
        }
    }

    synchronized void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }

    public synchronized boolean isScheduled() {
        return scheduled;
    }

    private synchronized void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public synchronized boolean isCancelled() {
        return cancelled;
    }

    public synchronized void cancel() {
        this.cancelled = true;
    }
}
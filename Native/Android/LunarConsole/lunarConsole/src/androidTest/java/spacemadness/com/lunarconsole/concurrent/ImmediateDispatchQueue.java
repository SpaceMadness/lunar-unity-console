package spacemadness.com.lunarconsole.concurrent;

public class ImmediateDispatchQueue extends DispatchQueue {
    public ImmediateDispatchQueue() {
        super("immediate");
    }

    @Override
    protected void schedule(DispatchTask task, long delay) {
        task.execute();
    }

    @Override
    public void stop() {
    }

    @Override
    public boolean isCurrent() {
        return false;
    }
}

package spacemadness.com.lunarconsole.concurrent;

import java.util.ArrayList;
import java.util.List;

public class ImmediateDispatchQueue extends DispatchQueue {
	private final boolean dispatchManually;
	private final List<Runnable> tasks;
	private Thread currentThread;

	public ImmediateDispatchQueue() {
		this(false);
	}

	public ImmediateDispatchQueue(boolean dispatchManually) {
		super("immediate");
		this.tasks = new ArrayList<>();
		this.dispatchManually = dispatchManually;
	}

	@Override
	public void dispatch(Runnable r, long delay) {
		if (dispatchManually) {
			tasks.add(r);
		} else {
			runTask(r);
		}
	}

	@Override
	public void stop() {
	}

	@Override
	public boolean isCurrent() {
		return Thread.currentThread() == currentThread;
	}

	public void dispatchAll() {
		List<Runnable> temp = new ArrayList<>(tasks);
		tasks.clear();
		for (Runnable r : temp) {
			runTask(r);
		}
	}

	private void runTask(Runnable r) {
		currentThread = Thread.currentThread();
		try {
			r.run();
		} finally {
			currentThread = null;
		}
	}
}
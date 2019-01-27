package spacemadness.com.lunarconsole;

import spacemadness.com.lunarconsole.concurrent.DispatchQueue;

public class ImmediateDispatchQueue extends DispatchQueue {
	public ImmediateDispatchQueue() {
		super("immediate");
	}

	@Override
	public void dispatch(Runnable r) {
		r.run();
	}

	@Override
	public void dispatch(Runnable r, long delay) {
		r.run();
	}

	@Override
	public boolean isCurrent() {
		return true;
	}
}

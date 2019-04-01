package spacemadness.com.lunarconsole.dependency;

import spacemadness.com.lunarconsole.concurrent.DispatchQueue;

public interface DispatchQueueProvider extends ProviderDependency {
	DispatchQueue createMainQueue();

	DispatchQueue createSerialQueue(String name);

	boolean isMainQueue();
}

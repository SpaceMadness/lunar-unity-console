package spacemadness.com.lunarconsole.dependency;

import java.util.HashMap;
import java.util.Map;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.checkNotNull;

public class DependencyProvider {
	private static Map<Class<? extends Dependency>, Dependency> dependencyLookup = new HashMap<>();

	public static <T extends Dependency> void register(Class<T> cls, T dependency) {
		dependencyLookup.put(checkNotNull(cls, "cls"), checkNotNull(dependency, "dependency"));
	}

	static void clear() {
		dependencyLookup.clear();
	}

	public static <T extends Dependency> T of(Class<T> cls) {
		Dependency dependency = dependencyLookup.get(cls);
		if (dependency == null) {
			throw new IllegalStateException("Dependency not registered: " + cls);
		}
		//noinspection unchecked
		return (T) dependency;
	}
}

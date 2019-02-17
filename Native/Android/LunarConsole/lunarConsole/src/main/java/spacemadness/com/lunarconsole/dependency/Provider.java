package spacemadness.com.lunarconsole.dependency;

import java.util.HashMap;
import java.util.Map;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.checkNotNull;

public class Provider {
	private static Map<Class<? extends ProviderDependency>, ProviderDependency> dependencyLookup = new HashMap<>();

	public static <T extends ProviderDependency> void register(Class<T> cls, T dependency) {
		dependencyLookup.put(checkNotNull(cls, "cls"), checkNotNull(dependency, "dependency"));
	}

	static void clear() {
		dependencyLookup.clear();
	}

	public static <T extends ProviderDependency> T of(Class<T> cls) {
		ProviderDependency dependency = dependencyLookup.get(cls);
		if (dependency == null) {
			throw new IllegalArgumentException("Dependency class not registered: " + cls);
		}
		//noinspection unchecked
		return (T) dependency;
	}
}

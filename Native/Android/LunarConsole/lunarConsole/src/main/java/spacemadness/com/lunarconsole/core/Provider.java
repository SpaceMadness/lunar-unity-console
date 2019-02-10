package spacemadness.com.lunarconsole.core;

import java.util.HashMap;
import java.util.Map;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.checkNotNull;

public class Provider {
	private static final Map<Class<? extends Providable>, Providable> lookup = new HashMap<>();

	public static <T extends Providable> T of(Class<? extends Providable> cls) throws MissingProvidableException {
		Providable providable = lookup.get(checkNotNull(cls, "cls"));
		if (providable == null) {
			throw new MissingProvidableException(cls);
		}

		//noinspection unchecked
		return (T) providable;
	}

	public static <T extends Providable> T register(Class<? extends Providable> cls, T providable) {
		lookup.put(checkNotNull(cls, "cls"), checkNotNull(providable, "providable"));
		return providable;
	}

	public static void clear() {
		lookup.clear();
	}
}

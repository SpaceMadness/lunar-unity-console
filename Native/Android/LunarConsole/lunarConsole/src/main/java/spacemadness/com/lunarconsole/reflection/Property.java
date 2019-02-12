package spacemadness.com.lunarconsole.reflection;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.checkNotNullAndNotEmpty;

public abstract class Property {
	public final String name;

	public Property(String name) {
		this.name = checkNotNullAndNotEmpty(name, "name");
	}
}

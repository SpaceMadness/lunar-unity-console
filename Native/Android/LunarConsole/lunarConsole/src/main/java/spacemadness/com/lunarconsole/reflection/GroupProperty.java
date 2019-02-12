package spacemadness.com.lunarconsole.reflection;

import java.util.List;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.checkNotNull;

public class GroupProperty extends Property {
	private final List<Property> children;

	public GroupProperty(String name, List<Property> children) {
		super(name);
		this.children = checkNotNull(children, "children");
	}

	public List<Property> getChildren() {
		return children;
	}
}

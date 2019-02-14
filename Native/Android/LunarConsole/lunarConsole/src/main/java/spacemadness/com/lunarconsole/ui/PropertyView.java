package spacemadness.com.lunarconsole.ui;

import android.content.Context;
import android.view.View;

import spacemadness.com.lunarconsole.reflection.FieldProperty;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.checkNotNull;

public class PropertyView extends View {
	private final FieldProperty property;

	public PropertyView(Context context, FieldProperty property) {
		super(context);
		this.property = checkNotNull(property, "property");
	}
}

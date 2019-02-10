package spacemadness.com.lunarconsole.settings;

import spacemadness.com.lunarconsole.json.Required;

public class LogOverlayEntryColors {
	private @Required
	Color foreground;
	private @Required
	Color background;

	//region Getters

	public Color getForeground() {
		return foreground;
	}

	public Color getBackground() {
		return background;
	}

	//endregion
}

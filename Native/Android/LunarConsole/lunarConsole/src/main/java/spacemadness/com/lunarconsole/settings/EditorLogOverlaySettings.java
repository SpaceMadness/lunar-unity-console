package spacemadness.com.lunarconsole.settings;

import spacemadness.com.lunarconsole.json.Required;

/**
 * Log overlay settings.
 */
public final class EditorLogOverlaySettings {
	/**
	 * Indicates if log overlay is enabled.
	 */
	private boolean enabled;

	/**
	 * Max number of simultaneously visible lines.
	 */
	private int maxVisibleLines;

	/**
	 * Delay in seconds before each line disappears (<code>0</code> means never disappear)
	 */
	private float timeout;

	/**
	 * Indicates if the line background should be transparent.
	 */
	private @Required LogOverlayColors colors;

	//region Getters

	public boolean isEnabled() {
		return enabled;
	}

	public int getMaxVisibleLines() {
		return maxVisibleLines;
	}

	public float getTimeout() {
		return timeout;
	}

	public LogOverlayColors getColors() {
		return colors;
	}

	//endregion
}

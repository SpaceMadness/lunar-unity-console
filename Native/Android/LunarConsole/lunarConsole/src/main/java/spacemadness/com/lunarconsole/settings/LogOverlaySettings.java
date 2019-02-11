package spacemadness.com.lunarconsole.settings;

import spacemadness.com.lunarconsole.json.Required;

/**
 * Log overlay settings.
 */
public final class LogOverlaySettings {
	/**
	 * Indicates if log overlay is enabled.
	 */
	public boolean enabled;

	/**
	 * Max number of simultaneously visible lines.
	 */
	public int maxVisibleLines;

	/**
	 * Delay in seconds before each line disappears (<code>0</code> means never disappear)
	 */
	public float timeout;

	/**
	 * Indicates if the line background should be transparent.
	 */
	public @Required LogOverlayColors colors;

	//region Equality

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		LogOverlaySettings that = (LogOverlaySettings) o;

		if (enabled != that.enabled) return false;
		if (maxVisibleLines != that.maxVisibleLines) return false;
		if (Float.compare(that.timeout, timeout) != 0) return false;
		return colors != null ? colors.equals(that.colors) : that.colors == null;
	}

	@Override public int hashCode() {
		int result = (enabled ? 1 : 0);
		result = 31 * result + maxVisibleLines;
		result = 31 * result + (timeout != +0.0f ? Float.floatToIntBits(timeout) : 0);
		result = 31 * result + (colors != null ? colors.hashCode() : 0);
		return result;
	}

	//endregion
}

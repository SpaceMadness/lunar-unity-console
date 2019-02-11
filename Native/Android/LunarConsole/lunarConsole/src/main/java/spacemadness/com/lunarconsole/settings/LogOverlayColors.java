package spacemadness.com.lunarconsole.settings;

import spacemadness.com.lunarconsole.json.Required;

public class LogOverlayColors {
	public @Required LogOverlayEntryColors exception;
	public @Required LogOverlayEntryColors error;
	public @Required LogOverlayEntryColors warning;
	public @Required LogOverlayEntryColors debug;

	//region Equality

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		LogOverlayColors that = (LogOverlayColors) o;

		if (exception != null ? !exception.equals(that.exception) : that.exception != null)
			return false;
		if (error != null ? !error.equals(that.error) : that.error != null) return false;
		if (warning != null ? !warning.equals(that.warning) : that.warning != null) return false;
		return debug != null ? debug.equals(that.debug) : that.debug == null;
	}

	@Override public int hashCode() {
		int result = exception != null ? exception.hashCode() : 0;
		result = 31 * result + (error != null ? error.hashCode() : 0);
		result = 31 * result + (warning != null ? warning.hashCode() : 0);
		result = 31 * result + (debug != null ? debug.hashCode() : 0);
		return result;
	}

	//endregion
}

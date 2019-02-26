package spacemadness.com.lunarconsole.settings;

import spacemadness.com.lunarconsole.json.Required;
import spacemadness.com.lunarconsole.utils.StringUtils;

public class LogEntryColors {
	public @Required Color foreground;
	public @Required Color background;

	//region Equality

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		LogEntryColors that = (LogEntryColors) o;

		if (foreground != null ? !foreground.equals(that.foreground) : that.foreground != null)
			return false;
		return background != null ? background.equals(that.background) : that.background == null;
	}

	@Override public int hashCode() {
		int result = foreground != null ? foreground.hashCode() : 0;
		result = 31 * result + (background != null ? background.hashCode() : 0);
		return result;
	}

	//endregion

	@Override public String toString() {
		return StringUtils.format("foreground=%s background=%s", foreground, background);
	}
}

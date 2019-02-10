package spacemadness.com.lunarconsole.settings;

import spacemadness.com.lunarconsole.json.Required;

public class LogOverlayColors {
	private @Required LogOverlayEntryColors exception;
	private @Required LogOverlayEntryColors error;
	private @Required LogOverlayEntryColors warning;
	private @Required LogOverlayEntryColors debug;

	//region Getters

	public LogOverlayEntryColors getException() {
		return exception;
	}

	public LogOverlayEntryColors getError() {
		return error;
	}

	public LogOverlayEntryColors getWarning() {
		return warning;
	}

	public LogOverlayEntryColors getDebug() {
		return debug;
	}

	//endregion
}

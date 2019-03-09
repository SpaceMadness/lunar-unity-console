package spacemadness.com.lunarconsole.settings;

import spacemadness.com.lunarconsole.json.Required;

/**
 * Exception warning settings from Unity editor.
 */
public final class ExceptionWarningSettings {
	public enum DisplayMode {
		/**
		 * Don't display anything.
		 */
		NONE,

		/**
		 * Display only errors.
		 */
		ERRORS,

		/**
		 * Display only exceptions.
		 */
		EXCEPTIONS,

		/**
		 * Display everything.
		 */
		ALL
	}

	/**
	 * Content display mode.
	 */
	public @Required DisplayMode displayMode;

	//region Equality

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ExceptionWarningSettings that = (ExceptionWarningSettings) o;

		return displayMode == that.displayMode;
	}

	@Override public int hashCode() {
		return displayMode != null ? displayMode.hashCode() : 0;
	}

	//endregion
}

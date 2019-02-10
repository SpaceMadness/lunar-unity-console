package spacemadness.com.lunarconsole.settings;

import spacemadness.com.lunarconsole.json.Required;

/**
 * Exception warning settings from Unity editor.
 */
public final class EditorExceptionWarningSettings {
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
		ALL;

		/**
		 * Parses the value of <code>DisplayMode</code> (case insensitive)
		 */
		public static DisplayMode parse(String value) {
			if (value == null) {
				throw new IllegalArgumentException("Value is null");
			}
			return DisplayMode.valueOf(value.toUpperCase());
		}
	}

	/**
	 * Content display mode.
	 */
	private @Required DisplayMode displayMode;

	//region Getters

	public DisplayMode getDisplayMode() {
		return displayMode;
	}

	//endregion
}

package spacemadness.com.lunarconsole.settings;

import org.json.JSONException;
import org.json.JSONObject;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.checkNotNull;

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
	public final DisplayMode displayMode;

	/**
	 * Creates an <code>EditorExceptionWarningSettings</code> instance from a json string.
	 */
	public EditorExceptionWarningSettings(DisplayMode displayMode) {
		this.displayMode = checkNotNull(displayMode, "displayMode");
	}

	/**
	 * Creates an <code>EditorExceptionWarningSettings</code> instance from a json string.
	 */
	public static EditorExceptionWarningSettings fromJson(String json) {
		try {
			return fromJson(new JSONObject(json));
		} catch (JSONException e) {
			throw new IllegalArgumentException("Invalid json: " + json);
		}
	}

	/**
	 * Creates an <code>EditorExceptionWarningSettings</code> instance from a json object.
	 */
	public static EditorExceptionWarningSettings fromJson(JSONObject json) throws JSONException {
		DisplayMode displayMode = DisplayMode.parse(json.getString("displayMode"));
		return new EditorExceptionWarningSettings(displayMode);
	}
}

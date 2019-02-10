package spacemadness.com.lunarconsole.settings;

import org.json.JSONException;
import org.json.JSONObject;

import spacemadness.com.lunarconsole.utils.NotImplementedException;

/**
 * Log overlay settings from Unity editor.
 */
public final class EditorLogOverlaySettings {
	/**
	 * Indicates if log overlay is enabled.
	 */
	public final boolean enabled;

	/**
	 * Max number of simultaneously visible lines.
	 */
	public final int maxVisibleLines;

	/**
	 * Delay in seconds before each line disappears (<code>0</code> means never disappear)
	 */
	public final float timeout;

	/**
	 * Indicates if the line background should be transparent.
	 */
	public final boolean transparent;

	public EditorLogOverlaySettings(boolean enabled, int maxVisibleLines, float timeout, boolean transparent) {
		this.enabled = enabled;
		this.maxVisibleLines = maxVisibleLines;
		this.timeout = timeout;
		this.transparent = transparent;
	}

	/**
	 * Creates an <code>EditorLogOverlaySettings</code> instance from a json string.
	 */
	public static EditorLogOverlaySettings fromJson(String json) {
		try {
			return fromJson(new JSONObject(json));
		} catch (JSONException e) {
			throw new IllegalArgumentException("Invalid json: " + json);
		}
	}

	/**
	 * Creates an <code>EditorLogOverlaySettings</code> instance from a json object.
	 */
	public static EditorLogOverlaySettings fromJson(JSONObject json) {
		throw new NotImplementedException();
	}
}

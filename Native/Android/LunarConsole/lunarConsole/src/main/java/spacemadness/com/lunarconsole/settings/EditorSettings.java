//
//  EditorSettings.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2019 Alex Lementuev, SpaceMadness.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

package spacemadness.com.lunarconsole.settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.checkNotNull;

/**
 * Global settings from Unity editor.
 */
public final class EditorSettings {
	/**
	 * Exception warning settings.
	 */
	public final EditorExceptionWarningSettings exceptionWarningSettings;

	/**
	 * Log overlay settings
	 */
	public final EditorLogOverlaySettings logOverlaySettings;

	/**
	 * Indicates if actions should be sorted.
	 */
	public final boolean sortActions;

	/**
	 * Indicates if variables should be sorted.
	 */
	public final boolean sortVariables;

	/**
	 * Optional list of the email recipients for sending a report.
	 */
	public final String[] emails; // TODO: make it an immutable list

	public EditorSettings(EditorExceptionWarningSettings exceptionWarningSettings,
	                      EditorLogOverlaySettings logOverlaySettings,
	                      String[] emails, boolean sortActions,
	                      boolean sortVariables) {
		this.exceptionWarningSettings = checkNotNull(exceptionWarningSettings, "exceptionWarningSettings");
		this.logOverlaySettings = checkNotNull(logOverlaySettings, "logOverlaySettings");
		this.emails = checkNotNull(emails, "emails");
		this.sortActions = sortActions;
		this.sortVariables = sortVariables;
	}

	//region Json Factory

	/**
	 * Creates an <code>EditorSettings</code> instance from a json string.
	 */
	public static EditorSettings fromJson(String json) {
		try {
			return fromJson(new JSONObject(json));
		} catch (JSONException e) {
			throw new IllegalArgumentException("Invalid json: " + json);
		}
	}

	/**
	 * Creates an <code>EditorSettings</code> instance from a json object.
	 */
	private static EditorSettings fromJson(JSONObject json) throws JSONException {
		return new EditorSettings(
			EditorExceptionWarningSettings.fromJson(json.getJSONObject("exceptionWarning")),
			EditorLogOverlaySettings.fromJson(json.getJSONObject("logOverlay")),
			toStringArray(json.getJSONArray("emails")), json.getBoolean("sortActions"),
			json.getBoolean("sortVariables")
		);
	}

	//endregion

	//region Helpers

	/**
	 * @return a non-null array of strings from the passed <code>JSONArray</code>
	 */
	private static String[] toStringArray(JSONArray jsonArray) throws JSONException {
		if (jsonArray == null) {
			return new String[0];
		}

		String[] array = new String[jsonArray.length()];
		for (int i = 0; i < array.length; ++i) {
			array[i] = jsonArray.getString(i);
		}
		return array;
	}

	//endregion
}

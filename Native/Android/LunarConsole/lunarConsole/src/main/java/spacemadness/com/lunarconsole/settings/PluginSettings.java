//
//  PluginSettings.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2020 Alex Lementuev, SpaceMadness.
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

import java.util.Arrays;

import spacemadness.com.lunarconsole.json.Required;

/**
 * Global settings from Unity editor.
 */
public final class PluginSettings {
	/**
	 * Exception warning settings.
	 */
	public @Required ExceptionWarningSettings exceptionWarning;

	/**
	 * Log overlay settings
	 */
	public @Required @ProOnly LogOverlaySettings logOverlay;

	/**
	 * Log output would not grow bigger than this capacity.
	 */
	public @Required @Readonly int capacity;

	/**
	 * Log output will be trimmed this many lines when overflown.
	 */
	public @Required @Readonly int trim;

	/**
	 * Gesture type to open the console.
	 */
	public @Required @Readonly Gesture gesture;

	/**
	 * Indicates if reach text tags should be ignored.
	 */
	public @Readonly boolean removeRichTextTags;

	/**
	 * Indicates if actions should be sorted.
	 */
	public boolean sortActions;

	/**
	 * Indicates if variables should be sorted.
	 */
	public boolean sortVariables;

	/**
	 * Optional list of the email recipients for sending a report.
	 */
	public @Readonly String[] emails;

	//region Equality

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PluginSettings settings = (PluginSettings) o;

		if (capacity != settings.capacity) return false;
		if (trim != settings.trim) return false;
		if (removeRichTextTags != settings.removeRichTextTags) return false;
		if (sortActions != settings.sortActions) return false;
		if (sortVariables != settings.sortVariables) return false;
		if (exceptionWarning != null ? !exceptionWarning.equals(settings.exceptionWarning) : settings.exceptionWarning != null)
			return false;
		if (logOverlay != null ? !logOverlay.equals(settings.logOverlay) : settings.logOverlay != null)
			return false;
		if (gesture != settings.gesture) return false;
		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		return Arrays.equals(emails, settings.emails);
	}

	@Override
	public int hashCode() {
		int result = exceptionWarning != null ? exceptionWarning.hashCode() : 0;
		result = 31 * result + (logOverlay != null ? logOverlay.hashCode() : 0);
		result = 31 * result + capacity;
		result = 31 * result + trim;
		result = 31 * result + (gesture != null ? gesture.hashCode() : 0);
		result = 31 * result + (removeRichTextTags ? 1 : 0);
		result = 31 * result + (sortActions ? 1 : 0);
		result = 31 * result + (sortVariables ? 1 : 0);
		result = 31 * result + Arrays.hashCode(emails);
		return result;
	}

	//endregion
}

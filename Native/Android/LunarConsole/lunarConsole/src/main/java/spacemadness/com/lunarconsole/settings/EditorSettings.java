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

import spacemadness.com.lunarconsole.json.Required;

/**
 * Global settings from Unity editor.
 */
public final class EditorSettings {
	/**
	 * Exception warning settings.
	 */
	private @Required EditorExceptionWarningSettings exceptionWarningSettings;

	/**
	 * Log overlay settings
	 */
	private @Required @ProOnly EditorLogOverlaySettings logOverlaySettings;

	/**
	 * Log output would not grow bigger than this capacity.
	 */
	private @Required @Readonly int capacity;

	/**
	 * Log output will be trimmed this many lines when overflown.
	 */
	private @Required @Readonly int trim;

	/**
	 * Gesture type to open the console.
	 */
	private @Required @Readonly String gesture; // TODO: use enum type

	/**
	 * Indicates if actions should be sorted.
	 */
	private boolean sortActions;

	/**
	 * Indicates if variables should be sorted.
	 */
	private boolean sortVariables;

	/**
	 * Optional list of the email recipients for sending a report.
	 */
	private @Readonly String[] emails;

	//region Getters

	public EditorExceptionWarningSettings getExceptionWarningSettings() {
		return exceptionWarningSettings;
	}

	public EditorLogOverlaySettings getLogOverlaySettings() {
		return logOverlaySettings;
	}

	public int getCapacity() {
		return capacity;
	}

	public int getTrim() {
		return trim;
	}

	public String getGesture() {
		return gesture;
	}

	public boolean isSortActions() {
		return sortActions;
	}

	public boolean isSortVariables() {
		return sortVariables;
	}

	public String[] getEmails() {
		return emails;
	}

	//endregion
}

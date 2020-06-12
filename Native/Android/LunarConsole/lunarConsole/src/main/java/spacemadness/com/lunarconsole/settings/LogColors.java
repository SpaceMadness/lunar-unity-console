//
//  LogColors.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2015-2020 Alex Lementuev, SpaceMadness.
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

public class LogColors {
	public @Required LogEntryColors exception;
	public @Required LogEntryColors error;
	public @Required LogEntryColors warning;
	public @Required LogEntryColors debug;

	//region Equality

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		LogColors that = (LogColors) o;

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

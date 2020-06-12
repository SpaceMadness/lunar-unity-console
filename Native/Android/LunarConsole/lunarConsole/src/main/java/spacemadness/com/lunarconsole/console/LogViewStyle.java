//
//  LogViewStyle.java
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


package spacemadness.com.lunarconsole.console;

import static spacemadness.com.lunarconsole.console.ConsoleLogType.*;

public final class LogViewStyle {
	private final LogEntryStyle[] styles = new LogEntryStyle[COUNT];

	public LogViewStyle(
		LogEntryStyle exception,
		LogEntryStyle error,
		LogEntryStyle warning,
		LogEntryStyle debug
	) {
		styles[ERROR] = error;
		styles[ASSERT] = error;
		styles[WARNING] = warning;
		styles[LOG] = debug;
		styles[EXCEPTION] = exception;
	}

	public LogEntryStyle getEntryStyle(int type) {
		return styles[type];
	}
}

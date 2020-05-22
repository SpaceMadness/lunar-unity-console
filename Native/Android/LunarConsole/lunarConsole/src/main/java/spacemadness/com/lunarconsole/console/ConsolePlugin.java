//
//  ConsolePlugin.java
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

package spacemadness.com.lunarconsole.console;

public interface ConsolePlugin {
	void start();

	void logMessage(ConsoleLogEntry entry);

	void showConsole();

	void hideConsole();

	void showOverlay();

	void hideOverlay();

	void clearConsole();

	void registerAction(int actionId, String actionName);

	void unregisterAction(int actionId);

	void registerVariable(int variableId, String name, String type, String value, String defaultValue, int flags, boolean hasRange, float rangeMin, float rangeMax);

	void updateVariable(int variableId, String value);

	void destroy();
}

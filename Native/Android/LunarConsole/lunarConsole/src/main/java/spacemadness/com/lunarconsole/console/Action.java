//
//  Action.java
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

public class Action extends IdentityEntry {
	private final String groupName;

	public Action(int actionId, String name) {
		super(actionId, getSimpleName(name));
		this.groupName = getGroupName(name);
	}

	//region Helpers

	private static String getSimpleName(String name) {
		int index = name.replace('\\', '/').lastIndexOf('/');
		return index != -1 ? name.substring(index + 1) : name;
	}

	private static String getGroupName(String name) {
		String fixedName = name.replace('\\', '/');
		int index = fixedName.lastIndexOf('/');
		return index != -1 ? fixedName.substring(0, index) : "";
	}

	//endregion

	//region Properties

	@Override
	public EntryType getEntryType() {
		return EntryType.Action;
	}

	public String getGroupName() {
		return groupName;
	}

	//endregion
}

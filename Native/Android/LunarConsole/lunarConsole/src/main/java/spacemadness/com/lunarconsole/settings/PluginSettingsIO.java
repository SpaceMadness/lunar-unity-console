//
//  PluginSettingsIO.java
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

import android.content.Context;
import android.content.SharedPreferences;

import spacemadness.com.lunarconsole.debug.Log;
import spacemadness.com.lunarconsole.json.JsonDecoder;

public class PluginSettingsIO {
	private static final String KEY_SETTINGS = "settings";

	public static PluginSettings load(Context context) {
		String settingJson = getSharedPreferences(context).getString(KEY_SETTINGS, null);
		if (settingJson == null) {
			return null;
		}

		try {
			return JsonDecoder.decode(settingJson, PluginSettings.class);
		} catch (Exception e) {
			Log.e(e, "Unable to load settings");
			return null;
		}
	}

	public static void save(Context context, PluginSettings settings) {
		try {
			String settingsJson = JsonDecoder.encode(settings);
			getSharedPreferences(context).edit().putString(KEY_SETTINGS, settingsJson).apply();
		} catch (Exception e) {
			Log.e(e, "Unable to save settings");
		}
	}

	private static SharedPreferences getSharedPreferences(Context context) {
		String prefsName = PluginSettings.class.getCanonicalName();
		return context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
	}
}

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

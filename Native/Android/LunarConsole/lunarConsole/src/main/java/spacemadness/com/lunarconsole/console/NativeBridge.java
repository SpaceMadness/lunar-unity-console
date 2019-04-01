package spacemadness.com.lunarconsole.console;

import android.app.Activity;

import com.unity3d.player.UnityPlayer;

import spacemadness.com.lunarconsole.debug.Log;
import spacemadness.com.lunarconsole.json.JsonDecoder;
import spacemadness.com.lunarconsole.settings.PluginSettings;

/**
 * Class representing a bridge between native and managed code.
 */
public final class NativeBridge {
	private static ConsolePlugin plugin;

	private NativeBridge() {
	}

	/**
	 * This method is called by a managed code. Do not rename or change params types of order
	 *
	 * @param targetName   name of game object which would receive native callbacks
	 * @param methodName   name of the method of the game object to be called
	 * @param version      plugin version
	 * @param settingsJson JSON settings of the plugin
	 */
	public static void init(String targetName, String methodName, String version, String settingsJson) {
		if (plugin != null) {
			Log.w("Plugin already initialized");
			return;
		}

		try {
			final Activity activity = UnityPlayer.currentActivity;
			final Platform platform = new ManagedPlatform(activity, targetName, methodName);
			final PluginSettings settings = JsonDecoder.decode(settingsJson, PluginSettings.class);
			plugin = new ConsolePluginImpl(activity, platform, version, settings);
			plugin.start();
		} catch (Exception e) {
			Log.e(e, "Exception while initializing plugin");
		}
	}

	public static void logMessage(String message, String stackTrace, int logType) {
		try {
			plugin.logMessage(message, stackTrace, logType);
		} catch (Exception e) {
			Log.e(e, "Exception while logging a message");
		}
	}

	public static void showConsole() {
		try {
			plugin.showConsole();
		} catch (Exception e) {
			Log.e(e, "Exception while showing console");
		}
	}

	public static void hideConsole() {
		try {
			plugin.hideConsole();
		} catch (Exception e) {
			Log.e(e, "Exception while hiding console");
		}
	}

	public static void clearConsole() {
		try {
			plugin.clearConsole();
		} catch (Exception e) {
			Log.e(e, "Exception while clearing console");
		}
	}

	public static void registerAction(int actionId, String actionName) {
		try {
			plugin.registerAction(actionId, actionName);
		} catch (Exception e) {
			Log.e(e, "Exception while registering action");
		}
	}

	public static void unregisterAction(int actionId) {
		try {
			plugin.unregisterAction(actionId);
		} catch (Exception e) {
			Log.e(e, "Exception while un-registering action");
		}
	}

	public static void registerVariable(int variableId, String name, String type, String value, String defaultValue, int flags, boolean hasRange, float rangeMin, float rangeMax) {
		try {
			plugin.registerVariable(variableId, name, type, value, defaultValue, flags, hasRange, rangeMin, rangeMax);
		} catch (Exception e) {
			Log.e(e, "Exception while registering variable");
		}
	}

	public static void updateVariable(final int variableId, final String value) {
		try {
			plugin.updateVariable(variableId, value);
		} catch (Exception e) {
			Log.e(e, "Exception while updating variable");
		}
	}

	public static void destroy() {
		try {
			if (plugin != null) {
				plugin.destroy();
				plugin = null;
			} else {
				Log.w("Plugin already destroyed");
			}
		} catch (Exception e) {
			Log.e(e, "Exception while destroying instance");
		}
	}
}

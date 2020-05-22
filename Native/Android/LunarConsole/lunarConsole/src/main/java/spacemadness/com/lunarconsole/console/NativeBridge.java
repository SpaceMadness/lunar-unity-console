//
//  NativeBridge.java
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

import android.app.Activity;

import com.unity3d.player.UnityPlayer;

import java.util.List;

import spacemadness.com.lunarconsole.concurrent.DispatchQueue;
import spacemadness.com.lunarconsole.concurrent.DispatchTask;
import spacemadness.com.lunarconsole.debug.Log;
import spacemadness.com.lunarconsole.dependency.DefaultDependencies;
import spacemadness.com.lunarconsole.json.JsonDecoder;
import spacemadness.com.lunarconsole.settings.PluginSettings;

/**
 * Class representing a bridge between native and managed code.
 */
public final class NativeBridge {
    private static ConsolePlugin plugin;

    private static final DispatchQueue dispatchQueue;
    private static final ConsoleLogEntryDispatcher entryDispatcher;

    static {
        // register default providers
        DefaultDependencies.register();

        // use main queue as default queue
        dispatchQueue = DispatchQueue.mainQueue();

        // initialize dispatcher
        entryDispatcher = new ConsoleLogEntryDispatcher(dispatchQueue, new ConsoleLogEntryDispatcher.OnDispatchListener() {
            @Override
            public void onDispatchEntries(List<ConsoleLogEntry> entries) {
                logEntries(entries);
            }
        });
    }


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
    public static void init(
            final String targetName,
            final String methodName,
            final String version,
            final String settingsJson
    ) {
        dispatchQueue.dispatch(new DispatchTask("plugin initialization") {
            @Override
            protected void execute() {
                if (plugin != null) {
                    Log.w("Plugin already initialized");
                    return;
                }

                final Activity activity = UnityPlayer.currentActivity;
                final Platform platform = new ManagedPlatform(activity, targetName, methodName);
                final PluginSettings settings = JsonDecoder.decode(settingsJson, PluginSettings.class);
                plugin = new ConsolePluginImpl(activity, platform, version, settings);
                plugin.start();
            }
        });
    }

    public static void logMessage(String message, String stackTrace, int logType) {
        try {
            // unity logs message on its own thread - we batch them and log to the main thread
            entryDispatcher.add(new ConsoleLogEntry((byte) logType, message, stackTrace));
        } catch (Exception e) {
            Log.e(e, "Exception while logging a message");
        }
    }

    public static void showConsole() {
        dispatchQueue.dispatch(new DispatchTask("show console") {
            @Override
            protected void execute() {
                plugin.showConsole();
            }
        });
    }

    public static void hideConsole() {
        dispatchQueue.dispatch(new DispatchTask("hide console") {
            @Override
            protected void execute() {
                plugin.hideConsole();
            }
        });
    }

    public static void clearConsole() {
        dispatchQueue.dispatch(new DispatchTask("clear console") {
            @Override
            protected void execute() {
                plugin.clearConsole();
            }
        });
    }

    public static void registerAction(final int actionId, final String actionName) {
        dispatchQueue.dispatch(new DispatchTask("register action") {
            @Override
            protected void execute() {
                plugin.registerAction(actionId, actionName);
            }
        });
    }

    public static void unregisterAction(final int actionId) {
        dispatchQueue.dispatch(new DispatchTask("unregister action") {
            @Override
            protected void execute() {
                plugin.unregisterAction(actionId);
            }
        });
    }

    public static void registerVariable(final int variableId, final String name, final String type, final String value, final String defaultValue, final int flags, final boolean hasRange, final float rangeMin, final float rangeMax) {
        dispatchQueue.dispatch(new DispatchTask("register variable") {
            @Override
            protected void execute() {
                plugin.registerVariable(variableId, name, type, value, defaultValue, flags, hasRange, rangeMin, rangeMax);
            }
        });
    }

    public static void updateVariable(final int variableId, final String value) {
        dispatchQueue.dispatch(new DispatchTask("update variable") {
            @Override
            protected void execute() {
                plugin.updateVariable(variableId, value);
            }
        });
    }

    public static void destroy() {
        dispatchQueue.dispatch(new DispatchTask("destroy plugin") {
            @Override
            protected void execute() {
                entryDispatcher.cancelAll();

                if (plugin != null) {
                    plugin.destroy();
                    plugin = null;
                } else {
                    Log.w("Plugin already destroyed");
                }
            }
        });
    }

    //region Entry Dispatcher

    private static void logEntries(List<ConsoleLogEntry> entries) {
        for (int i = 0; i < entries.size(); ++i) {
            plugin.logMessage(entries.get(i));
        }
    }

    //endregion
}

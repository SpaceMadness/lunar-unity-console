//
//  ConsolePlugin.java
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

import android.app.Activity;
import android.app.Application;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;
import java.util.Map;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.core.Destroyable;
import spacemadness.com.lunarconsole.core.Notification;
import spacemadness.com.lunarconsole.core.NotificationCenter;
import spacemadness.com.lunarconsole.debug.Assert;
import spacemadness.com.lunarconsole.debug.Log;
import spacemadness.com.lunarconsole.dependency.PluginSettingsEditorProvider;
import spacemadness.com.lunarconsole.dependency.Provider;
import spacemadness.com.lunarconsole.settings.PluginSettings;
import spacemadness.com.lunarconsole.settings.PluginSettingsEditor;
import spacemadness.com.lunarconsole.settings.PluginSettingsIO;
import spacemadness.com.lunarconsole.ui.gestures.GestureRecognizer;
import spacemadness.com.lunarconsole.ui.gestures.GestureRecognizerFactory;
import spacemadness.com.lunarconsole.utils.DictionaryUtils;
import spacemadness.com.lunarconsole.utils.DisplayCutoutHelper;
import spacemadness.com.lunarconsole.utils.Margins;
import spacemadness.com.lunarconsole.utils.NotImplementedException;

import static android.widget.FrameLayout.LayoutParams;
import static spacemadness.com.lunarconsole.console.Console.Options;
import static spacemadness.com.lunarconsole.console.ConsoleLogType.isErrorType;
import static spacemadness.com.lunarconsole.console.Notifications.NOTIFICATION_ACTION_SELECT;
import static spacemadness.com.lunarconsole.console.Notifications.NOTIFICATION_ACTIVITY_STARTED;
import static spacemadness.com.lunarconsole.console.Notifications.NOTIFICATION_ACTIVITY_STOPPED;
import static spacemadness.com.lunarconsole.console.Notifications.NOTIFICATION_KEY_ACTION;
import static spacemadness.com.lunarconsole.console.Notifications.NOTIFICATION_KEY_VARIABLE;
import static spacemadness.com.lunarconsole.console.Notifications.NOTIFICATION_VARIABLE_SET;
import static spacemadness.com.lunarconsole.debug.Tags.CONSOLE;
import static spacemadness.com.lunarconsole.debug.Tags.GESTURES;
import static spacemadness.com.lunarconsole.debug.Tags.OVERLAY_VIEW;
import static spacemadness.com.lunarconsole.debug.Tags.PLUGIN;
import static spacemadness.com.lunarconsole.debug.Tags.WARNING_VIEW;
import static spacemadness.com.lunarconsole.ui.gestures.GestureRecognizer.OnGestureListener;
import static spacemadness.com.lunarconsole.utils.ObjectUtils.checkNotNull;
import static spacemadness.com.lunarconsole.utils.ObjectUtils.checkNotNullAndNotEmpty;

public class ConsolePlugin implements NotificationCenter.OnNotificationListener, Destroyable, PluginSettingsEditorProvider {
    private static final String SCRIPT_MESSAGE_CONSOLE_OPEN = "console_open";
    private static final String SCRIPT_MESSAGE_CONSOLE_CLOSE = "console_close";
    private static final String SCRIPT_MESSAGE_ACTION = "console_action";
    private static final String SCRIPT_MESSAGE_VARIABLE_SET = "console_variable_set";

    private Console console;
    private ActivityLifecycleHandler activityLifecycleHandler;
    private final ActionRegistry actionRegistry;
    private final Platform platform;
    private final String version;
    private PluginSettings settings;

    private final ConsoleViewState consoleViewState;

    // Dialog for holding console related UI (starting Unity 5.6b we can use overlay views)
    private ConsoleView consoleView;
    private LogOverlayView logOverlayView;
    private WarningView warningView;

    private final WeakReference<Activity> activityRef;
    private final GestureRecognizer gestureDetector;
    private final View.OnTouchListener gestureDetectorTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            gestureDetector.onTouchEvent(event);
            return false; // do not block touch events!
        }
    };

    public ConsolePlugin(Activity activity, Platform platform, String version, PluginSettings settings) {
        this.activityRef = new WeakReference<>(checkNotNull(activity, "activity"));

        PluginSettings existingSettings = PluginSettingsIO.load(activity);
        if (existingSettings != null) {
            settings = existingSettings;
        }

        this.platform = checkNotNull(platform, "platform");
        this.settings = checkNotNull(settings, "settings");
        this.version = checkNotNullAndNotEmpty(version, "version");

        Application application = activity.getApplication();
        if (application == null) {
            throw new IllegalStateException("Application is null");
        }
        activityLifecycleHandler = new ActivityLifecycleHandler(application);

        // make settings available as a dependency
        Provider.register(PluginSettingsEditorProvider.class, this);

        consoleViewState = new ConsoleViewState(activity.getApplicationContext());

        Options options = new Options(settings.capacity);
        options.setTrimCount(settings.trim);
        console = new Console(options);
        actionRegistry = new ActionRegistry();
        actionRegistry.setActionSortingEnabled(settings.sortActions);
        actionRegistry.setVariableSortingEnabled(settings.sortVariables);

        gestureDetector = GestureRecognizerFactory.create(activity, settings.gesture);
        gestureDetector.setListener(new OnGestureListener() {
            @Override
            public void onGesture(GestureRecognizer gestureRecognizer) {
                showConsole();
            }
        });

        registerNotifications();
    }

    //region ConsolePlugin

    public void start() {
        enableGestureRecognition();

        if (settings.logOverlay.enabled) {
            showLogOverlayView();
        }
    }

    public void logMessage(ConsoleLogEntry entry) {
        // add to console
        console.logMessage(entry);

        // show warning
        if (shouldShowWarning(entry.type) && !isConsoleShown()) {
            showWarning(entry.message);
        }
    }

    public void showConsole() {
        try {
            showConsoleGuarded();
        } catch (Exception e) {
            Log.e(e, "Exception while showing console");
        }
    }

    public void hideConsole() {
        try {
            hideConsoleGuarded();
        } catch (Exception e) {
            Log.e(e, "Exception while hiding console");
        }
    }

    public void showOverlay() {
        showLogOverlayView();
    }

    public void hideOverlay() {
        removeLogOverlayView();
    }

    public void clearConsole() {
        console.clear();
    }

    public void registerAction(int actionId, String actionName) {
        actionRegistry.registerAction(actionId, actionName);
    }

    public void unregisterAction(int actionId) {
        actionRegistry.unregisterAction(actionId);
    }

    public void registerVariable(int variableId, String name, String typeName, String value, String defaultValue, int flags, boolean hasRange, float rangeMin, float rangeMax) {
        VariableType type = VariableType.parse(typeName);
        if (type == VariableType.Unknown) {
            Log.e("Unexpected variable type: %s", typeName);
            return;
        }

        Variable existing = actionRegistry.findVariable(variableId);
        if (existing != null) {
            Log.e("Attempted to register variable twice: %d", variableId);
            return;
        }

        Variable variable = actionRegistry.registerVariable(variableId, name, type, value, defaultValue);
        variable.setFlags(flags);
        if (hasRange) {
            variable.setRange(rangeMin, rangeMax);
        }
    }

    public void updateVariable(int variableId, String value) {
        actionRegistry.updateVariable(variableId, value);
    }

    //endregion

    //region Destroyable

    @Override
    public void destroy() {
        removeConsoleView();
        removeLogOverlayView();
        hideWarning();

        disableGestureRecognition();
        unregisterNotifications();

        console.destroy();
        if (activityLifecycleHandler != null) {
            activityLifecycleHandler.destroy();
        }

        Log.d(PLUGIN, "Plugin destroyed");
    }

    //endregion

    //region Overlay Dialog

    private void addOverlayView(Activity activity, View overlayView, LayoutParams layoutParams) {
        FrameLayout content = activity.getWindow().findViewById(android.R.id.content);

        // get layout margins
        Margins margins = DisplayCutoutHelper.getSafeMargins(activity);

        // set layout margins
        layoutParams.topMargin = Math.max(margins.top, layoutParams.topMargin);
        layoutParams.bottomMargin = Math.max(margins.bottom, layoutParams.bottomMargin);
        layoutParams.leftMargin = Math.max(margins.left, layoutParams.leftMargin);
        layoutParams.rightMargin = Math.max(margins.right, layoutParams.rightMargin);

        content.addView(overlayView, layoutParams);
    }

    private void removeOverlayView(View overlayView) {
        FrameLayout content = getActivity().getWindow().findViewById(android.R.id.content);
        content.removeView(overlayView);
    }

    //endregion

    //region Console

    private boolean showConsoleGuarded() {
        try {
            if (consoleView == null) {
                Log.d(CONSOLE, "Show console");

                final Activity activity = getActivity();
                if (activity == null) {
                    Log.e("Can't show console: activity reference is lost");
                    return false;
                }

                // create console view
                consoleView = new ConsoleView(activity, ConsolePlugin.this);
                consoleView.setListener(new ConsoleView.Listener() {
                    @Override
                    public void onOpen(ConsoleView view) {
                        sendNativeCallback(SCRIPT_MESSAGE_CONSOLE_OPEN);
                    }

                    @Override
                    public void onClose(ConsoleView view) {
                        hideConsole();
                    }
                });

                // place console log view into console layout
                LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

                // set layout margins
                params.topMargin = consoleViewState.getTopMargin();
                params.bottomMargin = consoleViewState.getBottomMargin();
                params.leftMargin = consoleViewState.getLeftMargin();
                params.rightMargin = consoleViewState.getRightMargin();

                addOverlayView(activity, consoleView, params);

                // show animation
                Animation animation = AnimationUtils.loadAnimation(activity, R.anim.lunar_console_slide_in_top);
                consoleView.startAnimation(animation);

                // notify delegates
                consoleView.notifyOpen();

                // don't handle gestures if console is shown
                disableGestureRecognition();

                // request focus
                consoleView.requestFocus();

                return true;
            } else {
                Log.w("Console is already open");
            }
        } finally {
            removeLogOverlayView();
        }
        return false;
    }

    private boolean hideConsoleGuarded() {
        if (consoleView == null) {
            return false;
        }

        Log.d(CONSOLE, "Hide console");

        Activity activity = getActivity();
        if (activity == null) {
            Log.w("Can't properly hide console: activity reference is lost");
            removeConsoleView();
            return false;
        }

        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.lunar_console_slide_out_top);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                removeConsoleView();

                if (settings.logOverlay.enabled) {
                    showLogOverlayView();
                }

                // start listening for gestures
                enableGestureRecognition();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        consoleView.startAnimation(animation);
        sendNativeCallback(SCRIPT_MESSAGE_CONSOLE_CLOSE);

        return true;
    }

    private void removeConsoleView() {
        if (consoleView != null) {
            removeOverlayView(consoleView);
            consoleView.destroy();
            consoleView = null;
        }
    }

    //endregion

    //region Error warning

    private boolean shouldShowWarning(byte type) {
        if (!isErrorType(type)) {
            return false;
        }

        switch (settings.exceptionWarning.displayMode) {
            case NONE:
                return false;
            case ERRORS:
                return type == ConsoleLogType.ERROR || type == ConsoleLogType.ASSERT;
            case EXCEPTIONS:
                return type == ConsoleLogType.EXCEPTION;
        }

        return true;
    }

    private void showWarning(final String message) {
        try {
            if (warningView == null) {
                Log.d(WARNING_VIEW, "Show warning");

                final Activity activity = getActivity();
                if (activity == null) {
                    Log.e("Can't show warning: activity reference is lost");
                    return;
                }

                warningView = new WarningView(activity);
                warningView.setListener(new WarningView.Listener() {
                    @Override
                    public void onDismissClick(WarningView view) {
                        hideWarning();
                    }

                    @Override
                    public void onDetailsClick(WarningView view) {
                        showConsole();
                        hideWarning();
                    }
                });

                addOverlayView(activity, warningView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            }

            warningView.setMessage(message);
        } catch (Exception e) {
            Log.e(e, "Can't show warning");
        }
    }

    private void hideWarning() {
        if (warningView != null) {
            Log.d(WARNING_VIEW, "Hide warning");

            removeOverlayView(warningView);
            warningView.destroy();
            warningView = null;
        }
    }

    //endregion

    //region Overlay view

    private boolean isOverlayViewShown() {
        return logOverlayView != null;
    }

    private boolean showLogOverlayView() {
        if (LunarConsoleConfig.isFree) {
            return false;
        }

        try {
            if (logOverlayView == null) {
                Log.d(OVERLAY_VIEW, "Show log overlay view");

                final Activity activity = getActivity();
                if (activity == null) {
                    Log.e("Can't show log overlay: activity reference is lost");
                    return false;
                }

                logOverlayView = new LogOverlayView(activity, console, settings.logOverlay);

                LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                addOverlayView(activity, logOverlayView, params);

                return true;
            }
        } catch (Exception e) {
            Log.e(e, "Can't show overlay view");
        }

        return false;
    }

    private boolean removeLogOverlayView() {
        try {
            if (logOverlayView != null) {
                Log.d(CONSOLE, "Hide log overlay view");

                removeOverlayView(logOverlayView);
                logOverlayView = null;

                return true;
            }
        } catch (Exception e) {
            Log.e(e, "Can't hide log overlay view");
        }

        return false;
    }

    //endregion

    //region Gesture recognition

    private void enableGestureRecognition() {
        Log.d(GESTURES, "Enable gesture recognition");

        View view = platform.getTouchRecipientView();
        if (view == null) {
            Log.w("Can't enable gesture recognition: touch view is null");
            return;
        }
        view.setOnTouchListener(gestureDetectorTouchListener);
    }

    private void disableGestureRecognition() {
        Log.d(GESTURES, "Disable gesture recognition");

        View view = platform.getTouchRecipientView();
        if (view != null) {
            view.setOnTouchListener(null);
        } else {
            Log.w("Can't disable gesture recognition: touch view is null");
        }
    }

    //endregion

    //region Native callbacks

    private void sendNativeCallback(String name) {
        sendNativeCallback(name, null);
    }

    private void sendNativeCallback(String name, Map<String, Object> data) {
        platform.sendUnityScriptMessage(name, data);
    }

    //endregion

    //region Notifications

    @Override
    public void onNotification(Notification notification) {
        switch (notification.getName()) {
            case NOTIFICATION_ACTION_SELECT: {
                Action action = notification.getUserData(NOTIFICATION_KEY_ACTION, Action.class);
                Assert.IsNotNull(action);

                if (action != null) {
                    sendNativeCallback(SCRIPT_MESSAGE_ACTION, DictionaryUtils.createMap("id", action.actionId()));
                }
                break;
            }
            case NOTIFICATION_VARIABLE_SET: {
                Variable variable = notification.getUserData(NOTIFICATION_KEY_VARIABLE, Variable.class);
                Assert.IsNotNull(variable);

                if (variable != null) {
                    Map<String, Object> params = DictionaryUtils.createMap(
                            "id", variable.actionId(),
                            "value", variable.value
                    );

                    sendNativeCallback(SCRIPT_MESSAGE_VARIABLE_SET, params);
                }
                break;
            }
        }
    }

    private void registerNotifications() {
        NotificationCenter.defaultCenter()
                .addListener(NOTIFICATION_ACTION_SELECT, this)
                .addListener(NOTIFICATION_VARIABLE_SET, this)
                .addListener(NOTIFICATION_ACTIVITY_STOPPED, this)
                .addListener(NOTIFICATION_ACTIVITY_STARTED, this);
    }

    private void unregisterNotifications() {
        NotificationCenter.defaultCenter().removeListener(this);
    }

    //endregion

    //region PluginSettingsEditorProvider

    @Override
    public PluginSettingsEditor getSettingsEditor() {
        return new PluginSettingsEditor() {
            @Override
            public PluginSettings getSettings() {
                return settings;
            }

            @Override
            public void setSettings(PluginSettings settings) {
                ConsolePlugin.this.settings = settings;
                PluginSettingsIO.save(getActivity(), settings);
            }

            @Override
            public boolean isProVersion() {
                return LunarConsoleConfig.isPro;
            }
        };
    }

    //endregion

    //region Getters/Setters

    Console getConsole() {
        return console;
    }

    ConsoleViewState getConsoleViewState() {
        return consoleViewState;
    }

    ActionRegistry getActionRegistry() {
        return actionRegistry;
    }

    boolean isConsoleShown() {
        return consoleView != null;
    }

    public String getVersion() {
        return version;
    }

    public static void setCapacity(int capacity) {
//		Options options = new Options(capacity);
//		options.setTrimCount(getConsoleInstance().getTrimSize());
//		instance.console = new Console(options);
        throw new NotImplementedException();
    }

    public static void setTrimSize(int trimCount) {
//		Options options = new Options(getConsoleInstance().getCapacity());
//		options.setTrimCount(trimCount);
//		instance.console = new Console(options);
        throw new NotImplementedException();
    }

    private Activity getActivity() {
        return activityRef.get();
    }

    public String[] getEmails() {
        return settings.emails;
    }

    //endregion
}

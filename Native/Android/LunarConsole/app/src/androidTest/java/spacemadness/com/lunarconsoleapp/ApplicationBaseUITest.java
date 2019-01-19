//
//  ApplicationBaseUITest.java
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

package spacemadness.com.lunarconsoleapp;

import android.app.Instrumentation;
import android.content.Context;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import static junit.framework.Assert.*;
import android.widget.SeekBar;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import spacemadness.com.lunarconsole.console.ConsoleCollapsedLogEntry;
import spacemadness.com.lunarconsole.console.ConsoleLogEntry;
import spacemadness.com.lunarconsole.console.ConsolePlugin;
import spacemadness.com.lunarconsole.console.VariableType;
import spacemadness.com.lunarconsole.debug.TestHelper;
import spacemadness.com.lunarconsole.utils.StringUtils;
import spacemadness.com.lunarconsoleapp.helpers.ViewActionsEx;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static org.hamcrest.Matchers.*;

public class ApplicationBaseUITest implements TestHelper.EventListener
{
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(MainActivity.class)
    {
        @Override
        protected void beforeActivityLaunched()
        {
            Instrumentation instrumentation = getInstrumentation();
            Context targetContext = instrumentation.getTargetContext();
            ApplicationBaseUITest.this.beforeActivityLaunched(targetContext);
        }

        private Instrumentation getInstrumentation()
        {
            try
            {
                Field instrumentationField = ActivityTestRule.class.getDeclaredField("mInstrumentation");
                instrumentationField.setAccessible(true);
                return (Instrumentation) instrumentationField.get(this);
            }
            catch (Exception e)
            {
                throw new AssertionError(e.getMessage());
            }
        }
    };

    private final List<String> results = new ArrayList<>();

    protected void beforeActivityLaunched(Context targetContext)
    {
        MainActivity.clearSharedPreferences(targetContext);
        MainActivity.forceSyncCalls = true;
        MainActivity.shutdownPluginWhenDestroyed = false;
        TestHelper.init(this);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // UI-helpers

    protected void pressButton(String title)
    {
        findView(title).perform(click());
    }

    protected void pressButton(int id)
    {
        assertVisible(id);
        findView(id).perform(click());
    }

    protected void pressMenuButton(int id)
    {
        pressButton(getString(id));
    }

    protected void pressBackButton()
    {
        Espresso.pressBack();
    }

    protected void typeText(int id, String text)
    {
        assertVisible(id);
        findView(id).perform(ViewActions.replaceText(text), ViewActions.pressImeActionButton());
    }

    protected void appendText(int id, String text)
    {
        assertVisible(id);
        findView(id).perform(ViewActions.typeText(text), ViewActions.pressImeActionButton());
    }

    protected void deleteLastChar(int id)
    {
        assertVisible(id);
        findView(id).perform(removeLastChar(), ViewActions.closeSoftKeyboard());
    }

    protected void clearText(int id)
    {
        assertVisible(id);
        findView(id).perform(ViewActions.clearText(), ViewActions.closeSoftKeyboard());
    }

    protected void setSeekBarProgress(int viewId, int progress)
    {
        assertVisible(viewId);
        findView(viewId).perform(ViewActionsEx.setSeekBarProgress(progress));
    }

    protected ViewInteraction findView(Class<? extends View> cls)
    {
        return onView(withClassName(is(cls.getName())));
    }

    protected ViewInteraction findView(int id)
    {
        return onView(withId(id));
    }

    protected ViewInteraction findView(String text)
    {
        return onView(withText(text));
    }

    protected ViewInteraction findViewWithTag(String tag)
    {
        return onView(withTagValue(Matchers.<Object>equalTo(tag)));
    }

    protected void assertText(int id, String text)
    {
        findView(id).check(matches(withText(text)));
    }

    protected void assertPreferenceChecked(String title, boolean checked)
    {
        findView(title).check(matches(withCheckBoxPreference(checked)));
    }

    protected void assertSeekProgress(int id, int progress)
    {
        findView(id).check(matches(withSeekBarProgress(progress)));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Visibility

    protected void assertVisible(ViewInteraction view)
    {
        view.check(matches(withEffectiveVisibility(Visibility.VISIBLE)));
    }

    protected void assertHidden(ViewInteraction view)
    {
        view.check(matches(withEffectiveVisibility(Visibility.GONE)));
    }

    protected void assertDoesNotExist(ViewInteraction view)
    {
        view.check(doesNotExist());
    }

    protected void assertVisible(int id)
    {
        assertVisible(findView(id));
    }

    protected void assertHidden(int id)
    {
        assertHidden(findView(id));
    }

    protected void assertDoesNotExist(int id)
    {
        assertDoesNotExist(findView(id));
    }

    protected void assertVisible(Class<? extends View> cls)
    {
        assertVisible(findView(cls));
    }

    protected void assertHidden(Class<? extends View> cls)
    {
        assertHidden(findView(cls));
    }

    protected void assertDoesNotExist(Class<? extends View> cls)
    {
        assertDoesNotExist(findView(cls));
    }

    protected boolean isVisible(int id)
    {
        try
        {
            assertVisible(id);
            return true;
        }
        catch (Throwable e)
        {
            return false;
        }
    }

    protected boolean isInvisible(int id)
    {
        try
        {
            assertHidden(id);
            return true;
        }
        catch (Throwable e)
        {
            return false;
        }
    }

    protected boolean isNotExist(int id)
    {
        try
        {
            assertDoesNotExist(id);
            return true;
        }
        catch (Throwable e)
        {
            return false;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ListView

    protected Matcher<View> withListViewSize(final int size)
    {
        return new TypeSafeMatcher<View>()
        {
            @Override
            public void describeTo(Description description)
            {
                description.appendText("ListView should have " + size + " items");
            }

            @Override
            protected boolean matchesSafely(View item)
            {
                return ((ListView) item).getChildCount() == size;
            }
        };
    }

    protected Matcher<View> withCheckBoxPreference(final boolean checked)
    {
        return new TypeSafeMatcher<View>()
        {
            @Override
            public void describeTo(Description description)
            {
                description.appendText("check box preference should be" + (checked ? "" : " not") + " checked");
            }

            @Override
            protected boolean matchesSafely(View item)
            {
                // FIXME: this is bad
                final LinearLayout layout = (LinearLayout) ((LinearLayout) item.getParent().getParent()).getChildAt(2);

                final Checkable checkable = findCheckable(layout);
                return checkable != null && checkable.isChecked() == checked;
            }

            private Checkable findCheckable(LinearLayout layout)
            {
                int childCount = layout.getChildCount();
                for (int i = 0; i < childCount; ++i)
                {
                    final View child = layout.getChildAt(i);
                    if (child instanceof Checkable)
                    {
                        return (Checkable) child;
                    }
                }

                return null;
            }
        };
    }

    protected Matcher<View> withSeekBarProgress(final int progress)
    {
        return new TypeSafeMatcher<View>()
        {
            @Override
            public void describeTo(Description description)
            {
                description.appendText("seek bar progress should be " + progress);
            }

            @Override
            protected boolean matchesSafely(View view)
            {
                return ((SeekBar) view).getProgress() == progress;
            }
        };
    }

    private static ViewAction removeLastChar()
    {
        return actionWithAssertions(new RemoveLastCharTextAction());
    }

    private static class RemoveLastCharTextAction implements ViewAction
    {

        @SuppressWarnings("unchecked")
        @Override
        public Matcher<View> getConstraints()
        {
            return allOf(isDisplayed(), isAssignableFrom(EditText.class));
        }

        @Override
        public void perform(UiController uiController, View view)
        {
            EditText editText = (EditText) view;
            String oldText = editText.getText().toString();
            String newText = oldText.length() > 1 ? oldText.substring(0, oldText.length() - 1) : "";
            editText.setText(newText);
        }

        @Override
        public String getDescription()
        {
            return "remove last character from text";
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Results

    private final Object resultMutex = new Object();

    protected void addResult(String result)
    {
        results.add(result);

        synchronized (resultMutex)
        {
            resultMutex.notifyAll();
        }
    }

    protected void clearResults()
    {
        results.clear();
    }

    protected void sleep(long time)
    {
        try
        {
            Thread.sleep(time);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void waitForResults(int length)
    {
        synchronized (resultMutex)
        {
            if (results.size() < length)
            {
                try
                {
                    resultMutex.wait(5000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void assertResult(String... expected)
    {
        waitForResults(expected.length);
        assertResult(results, expected);
    }

    protected void assertResult(List<String> actual, String... expected)
    {
        assertEquals("\nExpected: " + StringUtils.Join(expected) +
                "\nActual: " + StringUtils.Join(actual), expected.length, actual.size());

        for (int i = 0; i < expected.length; ++i)
        {
            assertEquals("\nExpected: " + StringUtils.Join(expected) +
                            "\nActual: " + StringUtils.Join(actual),
                    expected[i], actual.get(i));
        }
        clearResults();
    }

    protected void assertResult(String[] actual, String... expected)
    {
        assertEquals("\nExpected: " + StringUtils.Join(expected) +
                        "\nActual: " + StringUtils.Join(actual),
                expected.length, actual.length);

        for (int i = 0; i < expected.length; ++i)
        {
            assertEquals("\nExpected: " + StringUtils.Join(expected) +
                            "\nActual: " + StringUtils.Join(actual),
                    expected[i], actual[i]);
        }
        clearResults();
    }

    protected void assertResult(int[] actual, int... expected)
    {
        assertEquals("\nExpected: " + StringUtils.Join(expected) +
                        "\nActual: " + StringUtils.Join(actual),
                expected.length, actual.length);

        for (int i = 0; i < expected.length; ++i)
        {
            assertEquals("\nExpected: " + StringUtils.Join(expected) +
                            "\nActual: " + StringUtils.Join(actual),
                    expected[i], actual[i]);
        }
        clearResults();
    }

    protected void assertResult(float[] actual, float... expected)
    {
        assertEquals("\nExpected: " + StringUtils.Join(expected) +
                "\nActual: " + StringUtils.Join(actual), expected.length, actual.length);

        for (int i = 0; i < expected.length; ++i)
        {
            assertEquals("\nExpected: " + StringUtils.Join(expected) +
                            "\nActual: " + StringUtils.Join(actual),
                    expected[i], actual[i]);
        }
        clearResults();
    }

    protected void assertResult(boolean[] actual, boolean... expected)
    {
        assertEquals("\nExpected: " + StringUtils.Join(expected) +
                "\nActual: " + StringUtils.Join(actual), expected.length, actual.length);

        for (int i = 0; i < expected.length; ++i)
        {
            assertEquals("\nExpected: " + StringUtils.Join(expected) +
                            "\nActual: " + StringUtils.Join(actual),
                    expected[i], actual[i]);
        }
        clearResults();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Log helpers

    protected void logMessage(String message, int logType)
    {
        logMessage(message, null, logType);
    }

    protected void logMessage(String message, String stackTrace, int logType)
    {
        ConsolePlugin.logMessage(message, stackTrace, logType);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Action helpers

    protected void registerAction(int actionId, String name)
    {
        ConsolePlugin.registerAction(actionId, name);
    }

    protected void unregisterActions(int... actionIds)
    {
        for (int actionId : actionIds)
        {
            ConsolePlugin.unregisterAction(actionId);
        }
    }

    protected void registerVariable(int variableId, String name, String value)
    {
        registerVariable(variableId, name, value, value);
    }

    protected void registerVariable(int variableId, String name, String value, String defaultValue)
    {
        registerVariable(variableId, name, VariableType.String, value, defaultValue);
    }

    protected void registerVariable(int variableId, String name, int value)
    {
        registerVariable(variableId, name, VariableType.Integer, Integer.toString(value));
    }

    protected void registerVariable(int variableId, String name, float value)
    {
        registerVariable(variableId, name, VariableType.Float, Float.toString(value));
    }

    protected void registerVariable(int variableId, String name, float value, float min, float max)
    {
        registerVariable(variableId, name, VariableType.Float, Float.toString(value), Float.toString(value), min, max);
    }

    protected void registerVariable(int variableId, String name, boolean value)
    {
        registerVariable(variableId, name, VariableType.Boolean, Boolean.toString(value));
    }

    protected void registerVariable(int variableId, String name, VariableType type, String value)
    {
        registerVariable(variableId, name, type, value, value);
    }

    protected void registerVariable(int variableId, String name, VariableType type, String value, String defaultValue)
    {
        ConsolePlugin.registerVariable(variableId, name, type.toString(), value, defaultValue, 0, false, 0, 0);
    }

    protected void registerVariable(int variableId, String name, VariableType type, String value, String defaultValue, float min, float max)
    {
        ConsolePlugin.registerVariable(variableId, name, type.toString(), value, defaultValue, 0, true, min, max);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Test events

    @Override
    public void onTestEvent(String name, Object data)
    {

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Helpers

    protected void openConsole()
    {
        if (isNotExist(R.id.lunar_console_log_view))
        {
            getActivity().openConsole();
        }
    }

    protected void closeConsole()
    {
        pressButton(R.id.lunar_console_button_close);
    }

    protected void openConsoleMenu()
    {
        openConsole();
        pressButton(R.id.lunar_console_button_more);
    }

    protected void openSettings()
    {
        openConsoleMenu();
        pressMenuButton(R.string.lunar_console_more_menu_settings);
    }

    protected void closeSettings()
    {
        pressBackButton();
    }

    protected void assertExceptionWarningVisible()
    {
        assertVisible(R.id.lunar_console_warning_text_message);
    }

    protected void assertExceptionWarningInvisible()
    {
        findView(R.id.lunar_console_warning_text_message).check(doesNotExist());
    }

    protected void assertExceptionWarning(String expected)
    {
        assertExceptionWarningVisible();
        assertText(R.id.lunar_console_warning_text_message, expected);
    }

    protected void assertLogEntries(String... expected)
    {
        ViewInteraction listView = onView(withParent(withId(R.id.lunar_console_log_view_list_container)));

        // should be visible
        listView.check(matches(isDisplayed()));

        // should contains expected number of children
        listView.check(matches(withListViewSize(expected.length)));

        String[] messages = new String[expected.length];
        int[] messageCount = new int[expected.length];
        for (int i = 0; i < expected.length; ++i)
        {
            String token = expected[i];
            String message = token;
            int count = 0;

            int index = token.indexOf("@");
            if (index != -1)
            {
                message = token.substring(0, index);
                count = Integer.parseInt(token.substring(index + 1));
            }

            messages[i] = message;
            messageCount[i] = count;
        }

        for (int i = 0; i < messages.length; ++i)
        {
            if (messageCount[i] == 0) // 'regular' entries
            {
                // find entry view
                DataInteraction entryView = onData(allOf(is(instanceOf(ConsoleLogEntry.class))))
                        .inAdapterView(withParent(withId(R.id.lunar_console_log_view_list_container)))
                        .atPosition(i);

                // check message
                entryView
                        .onChildView(withId(R.id.lunar_console_log_entry_message))
                        .check(matches(withText(messages[i])));

                entryView
                        .onChildView(withId(R.id.lunar_console_log_collapsed_count))
                        .check(matches(withEffectiveVisibility(Visibility.GONE)));
            }
            else // 'collapsed' entries
            {
                // find entry view
                DataInteraction entryView = onData(allOf(is(instanceOf(ConsoleCollapsedLogEntry.class))))
                        .inAdapterView(withParent(withId(R.id.lunar_console_log_view_list_container)))
                        .atPosition(i);

                // check message
                entryView
                        .onChildView(withId(R.id.lunar_console_log_entry_message))
                        .check(matches(withText(messages[i])));

                DataInteraction countView = entryView
                        .onChildView(withId(R.id.lunar_console_log_collapsed_count));

                countView
                        .check(matches(withEffectiveVisibility(Visibility.VISIBLE)));
                countView
                        .check(matches(withText(String.valueOf(messageCount[i]))));
            }
        }
    }

    protected MainActivity getActivity()
    {
        return mActivityRule.getActivity();
    }

    protected String getString(int id)
    {
        return getActivity().getResources().getString(id);
    }

    //region Variable info

    protected static class var
    {
        final String name;
        final VariableType type;
        final String value;

        var(String name, Object value)
        {
            this.name = name;
            this.value = value.toString();
            type = getType(value);
        }

        private VariableType getType(Object value)
        {
            if (value instanceof String) return VariableType.String;
            if (value instanceof Integer) return VariableType.Integer;
            if (value instanceof Float) return VariableType.Float;
            if (value instanceof Boolean) return VariableType.Boolean;
            throw new IllegalArgumentException("Unsupported type: " + value.getClass());
        }
    }

    //endregion
}

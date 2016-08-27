//
//  ApplicationBaseUITest.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2016 Alex Lementuev, SpaceMadness.
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

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;

import java.lang.reflect.Field;

import spacemadness.com.lunarconsole.console.ConsoleCollapsedEntry;
import spacemadness.com.lunarconsole.console.ConsoleEntry;
import spacemadness.com.lunarconsole.console.ConsoleLogType;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static org.hamcrest.Matchers.*;

public class ApplicationBaseUITest
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

    protected void beforeActivityLaunched(Context targetContext)
    {
        MainActivity.clearSharedPreferences(targetContext);
        MainActivity.forceSyncCalls = true;
        MainActivity.shutdownPluginWhenDestroyed = false;
    }

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
        findView(id).perform(ViewActions.replaceText(text), ViewActions.closeSoftKeyboard());
    }

    protected void appendText(int id, String text)
    {
        assertVisible(id);
        findView(id).perform(ViewActions.typeText(text), ViewActions.closeSoftKeyboard());
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
            assertHidden(id);
            return true;
        }
        catch (Throwable e)
        {
            return false;
        }
    }

    protected boolean isInvisible(int id)
    {
        return !isVisible(id);
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
                description.appendText ("ListView should have " + size + " items");
            }

            @Override
            protected boolean matchesSafely(View item)
            {
                return ((ListView) item).getChildCount () == size;
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
                description.appendText ("check box preference should be" + (checked ? "" : " not") + " checked");
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

    private static ViewAction removeLastChar() {
        return actionWithAssertions(new RemoveLastCharTextAction());
    }

    private static class RemoveLastCharTextAction implements ViewAction {

        @SuppressWarnings("unchecked")
        @Override
        public Matcher<View> getConstraints() {
            return allOf(isDisplayed(), isAssignableFrom(EditText.class));
        }

        @Override
        public void perform(UiController uiController, View view) {
            EditText editText = (EditText) view;
            String oldText = editText.getText().toString();
            String newText = oldText.length() > 1 ? oldText.substring(0, oldText.length() - 1) : "";
            editText.setText(newText);
        }

        @Override
        public String getDescription() {
            return "remove last character from text";
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Helpers

    protected void openConsole()
    {
        if (isInvisible(R.id.lunar_console_layout))
        {
            pressButton(R.id.test_button_show_console);
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

    protected void logMessage(String message, byte logType)
    {
        typeText(R.id.test_edit_message, message);
        pressButton(getButtonId(logType));
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

    protected void assertTable(String... expected)
    {
        ViewInteraction listView = onView(withParent(withId(R.id.lunar_console_list_view_container)));

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
                DataInteraction entryView = onData(allOf(is(instanceOf(ConsoleEntry.class))))
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
                DataInteraction entryView = onData(allOf(is(instanceOf(ConsoleCollapsedEntry.class))))
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

    protected int getButtonId(byte logType)
    {
        switch (logType)
        {
            case ConsoleLogType.LOG: return R.id.test_button_log_debug;
            case ConsoleLogType.WARNING: return R.id.test_button_log_warning;
            case ConsoleLogType.ERROR: return R.id.test_button_log_error;
        }

        throw new IllegalArgumentException("Unexpected log type: " + logType);
    }

    protected MainActivity getActivity()
    {
        return mActivityRule.getActivity();
    }

    protected String getString(int id)
    {
        return getActivity().getResources().getString(id);
    }
}

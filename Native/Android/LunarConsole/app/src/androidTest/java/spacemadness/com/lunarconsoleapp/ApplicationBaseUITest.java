package spacemadness.com.lunarconsoleapp;

import android.app.Instrumentation;
import android.content.Context;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.EditText;
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
            MainActivity.clearSharedPreferences(targetContext);
            MainActivity.forceSyncCalls = true;
            MainActivity.shutdownPluginWhenDestroyed = false;
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

    protected void pressButton(String title)
    {
        findView(title).perform(click());
    }

    protected void pressButton(int id)
    {
        checkVisible(id);
        findView(id).perform(click());
    }

    protected void typeText(int id, String text)
    {
        checkVisible(id);
        findView(id).perform(ViewActions.replaceText(text), ViewActions.closeSoftKeyboard());
    }

    protected void appendText(int id, String text)
    {
        checkVisible(id);
        findView(id).perform(ViewActions.typeText(text), ViewActions.closeSoftKeyboard());
    }

    protected void deleteLastChar(int id)
    {
        checkVisible(id);
        findView(id).perform(removeLastChar(), ViewActions.closeSoftKeyboard());
    }

    protected void clearText(int id)
    {
        checkVisible(id);
        findView(id).perform(ViewActions.clearText(), ViewActions.closeSoftKeyboard());
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

    protected void checkText(int id, String text)
    {
        findView(id).check(matches(withText(text)));
    }

    protected void checkVisible(int id)
    {
        findView(id).check(matches(withEffectiveVisibility(Visibility.VISIBLE)));
    }

    protected void checkInvisible(int id)
    {
        findView(id).check(matches(withEffectiveVisibility(Visibility.GONE)));
    }

    protected Matcher<View> withListSize(final int size)
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

    protected void logMessage(String message, byte logType)
    {
        typeText(R.id.test_edit_message, message);
        pressButton(getButtonId(logType));
    }

    protected void assertTable(String... expected)
    {
        ViewInteraction listView = onView(withParent(withId(R.id.lunar_console_list_view_container)));

        // should be visible
        listView.check(matches(isDisplayed()));

        // should contains expected number of children
        listView.check(matches(withListSize(expected.length)));

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

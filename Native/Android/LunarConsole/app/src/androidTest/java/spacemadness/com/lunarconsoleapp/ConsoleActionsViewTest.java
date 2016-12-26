package spacemadness.com.lunarconsoleapp;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ConsoleActionsViewTest extends ApplicationBaseUITest
{
    @Test
    public void testNoActions()
    {
        openActions();
        assertNoActions();
    }

    @Test
    public void testRegisterActions()
    {
        registerAction(1, "Action-1");
        registerAction(2, "Action-2");
        registerAction(3, "Action-3");

        openActions();
        assertActions("Action-1", "Action-2", "Action-3");
        closeConsole();

        unregisterActions(2);

        openActions();
        assertActions("Action-1", "Action-3");
        closeActions();

        registerAction(4, "Action-4");
        registerAction(5, "Action-2");

        openActions();
        assertActions("Action-1", "Action-2", "Action-3", "Action-4");
        closeActions();

        unregisterActions(1, 3, 4, 5);

        openActions();
        assertNoActions();
    }

    @Test
    public void testRegisterActionsWhileConsoleOpen()
    {
        openActions();
        assertNoActions();

        registerAction(1, "Action-1");
        registerAction(2, "Action-2");
        registerAction(3, "Action-3");

        assertActions("Action-1", "Action-2", "Action-3");

        unregisterActions(2);

        assertActions("Action-1", "Action-3");

        registerAction(4, "Action-4");
        registerAction(5, "Action-2");

        assertActions("Action-1", "Action-2", "Action-3", "Action-4");

        unregisterActions(1, 3, 4, 5);

        assertNoActions();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Helpers

    private void assertNoActions()
    {
        assertVisible(R.id.lunar_console_actions_warning_view);
        assertHidden(R.id.lunar_console_action_view_list_container);
    }

    private void openActions()
    {
        openConsole();
        findView(R.id.lunar_console_view_pager).perform(swipeLeft());
    }

    private void closeActions()
    {
        closeConsole();
    }
}

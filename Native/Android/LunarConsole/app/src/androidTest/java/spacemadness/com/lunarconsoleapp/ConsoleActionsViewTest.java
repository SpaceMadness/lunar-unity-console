//
//  ConsoleActionsViewTest.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2017 Alex Lementuev, SpaceMadness.
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

    @Test
    public void testFilter()
    {
        registerAction(1, "Action-1");
        registerAction(2, "Action-12");
        registerAction(3, "Action-123");
        registerAction(4, "Action-2");
        registerAction(5, "Action-3");
        registerAction(6, "Action-4");

        openActions();
        assertActions("Action-1", "Action-12", "Action-123", "Action-2", "Action-3", "Action-4");

        setFilterText("Action");
        assertActions("Action-1", "Action-12", "Action-123", "Action-2", "Action-3", "Action-4");

        appendFilterText("-");
        assertActions("Action-1", "Action-12", "Action-123", "Action-2", "Action-3", "Action-4");

        appendFilterText("1");
        assertActions("Action-1", "Action-12", "Action-123");

        appendFilterText("2");
        assertActions("Action-12", "Action-123");

        appendFilterText("3");
        assertActions("Action-123");

        appendFilterText("4");
        assertActions();

        deleteLastFilterCharacter();
        assertActions("Action-123");

        deleteLastFilterCharacter();
        assertActions("Action-12", "Action-123");

        deleteLastFilterCharacter();
        assertActions("Action-1", "Action-12", "Action-123");

        deleteLastFilterCharacter();
        assertActions("Action-1", "Action-12", "Action-123", "Action-2", "Action-3", "Action-4");
    }

    @Test
    public void testFilterAndAddRemoveActions()
    {
        registerAction(1, "Action-1");
        registerAction(2, "Action-2");
        registerAction(3, "Action-3");
        registerAction(5, "Foo");

        openActions();
        assertActions("Action-1", "Action-2", "Action-3", "Foo");

        setFilterText("Action-1");
        assertActions("Action-1");

        registerAction(4, "Action-12");
        assertActions("Action-1", "Action-12");

        unregisterActions(1);
        assertActions("Action-12");

        unregisterActions(4);
        assertActions();

        deleteLastFilterCharacter();
        assertActions("Action-2", "Action-3");

        unregisterActions(3);
        assertActions("Action-2");

        unregisterActions(2);
        assertActions();

        unregisterActions(5);
        assertActions();

        setFilterText("");
        assertNoActions();
    }

    @Test
    public void testFilterPersistence()
    {
        registerAction(1, "Action-1");
        registerAction(2, "Action-12");
        registerAction(3, "Action-123");
        registerAction(4, "Action-2");
        registerAction(5, "Action-3");
        registerAction(6, "Action-4");

        openActions();
        setFilterText("Action-1");
        assertActions("Action-1", "Action-12", "Action-123");
        closeActions();

        openActions();
        assertActions("Action-1", "Action-12", "Action-123");
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

    private void setFilterText(String filterText)
    {
        typeText(R.id.lunar_console_action_view_text_edit_filter, filterText);
    }

    private void appendFilterText(String filterText)
    {
        appendText(R.id.lunar_console_action_view_text_edit_filter, filterText);
    }

    private void deleteLastFilterCharacter()
    {
        deleteLastChar(R.id.lunar_console_action_view_text_edit_filter);
    }
}

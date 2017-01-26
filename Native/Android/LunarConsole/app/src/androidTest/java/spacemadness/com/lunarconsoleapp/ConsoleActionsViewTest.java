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
    private static final String[] NO_ACTIONS = {};
    private static final var[] NO_VARIABLES = {};

    @Test
    public void testNoActions()
    {
        openActions();
        assertNoActions();
    }

    //region Actions

    @Test
    public void testRegisterActions()
    {
        registerAction(1, "Action-1");
        registerAction(2, "Action-2");
        registerAction(3, "Action-3");

        openActions();
        assertEntries(new String[]{"Action-1", "Action-2", "Action-3"}, NO_VARIABLES);
        closeConsole();

        unregisterActions(2);

        openActions();
        assertEntries(new String[]{"Action-1", "Action-3"}, NO_VARIABLES);
        closeActions();

        registerAction(4, "Action-4");
        registerAction(5, "Action-2");

        openActions();
        assertEntries(new String[]{"Action-1", "Action-2", "Action-3", "Action-4"}, NO_VARIABLES);
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

        assertEntries(new String[]{"Action-1", "Action-2", "Action-3"}, NO_VARIABLES);

        unregisterActions(2);

        assertEntries(new String[]{"Action-1", "Action-3"}, NO_VARIABLES);

        registerAction(4, "Action-4");
        registerAction(5, "Action-2");

        assertEntries(new String[]{"Action-1", "Action-2", "Action-3", "Action-4"}, NO_VARIABLES);

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
        assertEntries(new String[]{"Action-1", "Action-12", "Action-123", "Action-2", "Action-3", "Action-4"}, NO_VARIABLES);

        setFilterText("Action");
        assertEntries(new String[]{"Action-1", "Action-12", "Action-123", "Action-2", "Action-3", "Action-4"}, NO_VARIABLES);

        appendFilterText("-");
        assertEntries(new String[]{"Action-1", "Action-12", "Action-123", "Action-2", "Action-3", "Action-4"}, NO_VARIABLES);

        appendFilterText("1");
        assertEntries(new String[]{"Action-1", "Action-12", "Action-123"}, NO_VARIABLES);

        appendFilterText("2");
        assertEntries(new String[]{"Action-12", "Action-123"}, NO_VARIABLES);

        appendFilterText("3");
        assertEntries(new String[]{"Action-123"}, NO_VARIABLES);

        appendFilterText("4");
        assertEntries(new String[]{}, NO_VARIABLES);

        deleteLastFilterCharacter();
        assertEntries(new String[]{"Action-123"}, NO_VARIABLES);

        deleteLastFilterCharacter();
        assertEntries(new String[]{"Action-12", "Action-123"}, NO_VARIABLES);

        deleteLastFilterCharacter();
        assertEntries(new String[]{"Action-1", "Action-12", "Action-123"}, NO_VARIABLES);

        deleteLastFilterCharacter();
        assertEntries(new String[]{"Action-1", "Action-12", "Action-123", "Action-2", "Action-3", "Action-4"}, NO_VARIABLES);
    }

    @Test
    public void testFilterAndAddRemoveActions()
    {
        registerAction(1, "Action-1");
        registerAction(2, "Action-2");
        registerAction(3, "Action-3");
        registerAction(5, "Foo");

        openActions();
        assertEntries(new String[]{"Action-1", "Action-2", "Action-3", "Foo"}, NO_VARIABLES);

        setFilterText("Action-1");
        assertEntries(new String[]{"Action-1"}, NO_VARIABLES);

        registerAction(4, "Action-12");
        assertEntries(new String[]{"Action-1", "Action-12"}, NO_VARIABLES);

        unregisterActions(1);
        assertEntries(new String[]{"Action-12"}, NO_VARIABLES);

        unregisterActions(4);
        assertEntries(new String[]{}, NO_VARIABLES);

        deleteLastFilterCharacter();
        assertEntries(new String[]{"Action-2", "Action-3"}, NO_VARIABLES);

        unregisterActions(3);
        assertEntries(new String[]{"Action-2"}, NO_VARIABLES);

        unregisterActions(2);
        assertEntries(new String[]{}, NO_VARIABLES);

        unregisterActions(5);
        assertEntries(new String[]{}, NO_VARIABLES);

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
        assertEntries(new String[]{"Action-1", "Action-12", "Action-123"}, NO_VARIABLES);
        closeActions();

        openActions();
        assertEntries(new String[]{"Action-1", "Action-12", "Action-123"}, NO_VARIABLES);
    }

    //endregion

    //region Variables

    @Test
    public void testRegisterVariables()
    {
        registerVariable(1, "string", "value");
        registerVariable(2, "integer", 10);
        registerVariable(3, "float", 3.14f);
        registerVariable(4, "boolean", false);

        openActions();
        assertEntries(NO_ACTIONS, new var[]{
                new var("boolean", false),
                new var("float", 3.14f),
                new var("integer", 10),
                new var("string", "value")
        });
    }

    @Test
    public void testRegisterVariablesWhileConsoleOpen()
    {
        openActions();
        assertNoActions();

        registerVariable(1, "string", "value");
        registerVariable(2, "integer", 10);

        assertEntries(NO_ACTIONS, new var[]{
                new var("integer", 10),
                new var("string", "value")
        });

        registerVariable(3, "float", 3.14f);
        registerVariable(4, "boolean", false);

        assertEntries(NO_ACTIONS, new var[]{
                new var("boolean", false),
                new var("float", 3.14f),
                new var("integer", 10),
                new var("string", "value")
        });
    }

    @Test
    public void testVariablesFilter()
    {
        registerVariable(1, "Variable-1", "value-1");
        registerVariable(2, "Variable-12", "value-12");
        registerVariable(3, "Variable-123", "value-123");
        registerVariable(4, "Variable-2", "value-2");
        registerVariable(5, "Variable-3", "value-3");
        registerVariable(6, "Variable-4", "value-4");

        openActions();
        assertEntries(NO_ACTIONS, new var[]{
                new var("Variable-1", "value-1"),
                new var("Variable-12", "value-12"),
                new var("Variable-123", "value-123"),
                new var("Variable-2", "value-2"),
                new var("Variable-3", "value-3"),
                new var("Variable-4", "value-4")
        });

        setFilterText("Variable");
        assertEntries(NO_ACTIONS, new var[]{
                new var("Variable-1", "value-1"),
                new var("Variable-12", "value-12"),
                new var("Variable-123", "value-123"),
                new var("Variable-2", "value-2"),
                new var("Variable-3", "value-3"),
                new var("Variable-4", "value-4")
        });

        appendFilterText("-");
        assertEntries(NO_ACTIONS, new var[]{
                new var("Variable-1", "value-1"),
                new var("Variable-12", "value-12"),
                new var("Variable-123", "value-123"),
                new var("Variable-2", "value-2"),
                new var("Variable-3", "value-3"),
                new var("Variable-4", "value-4")
        });

        appendFilterText("1");
        assertEntries(NO_ACTIONS, new var[]{
                new var("Variable-1", "value-1"),
                new var("Variable-12", "value-12"),
                new var("Variable-123", "value-123")
        });

        appendFilterText("2");
        assertEntries(NO_ACTIONS, new var[]{
                new var("Variable-12", "value-12"),
                new var("Variable-123", "value-123")
        });

        appendFilterText("3");
        assertEntries(NO_ACTIONS, new var[]{
                new var("Variable-123", "value-123")
        });

        appendFilterText("4");
        assertEntries(NO_ACTIONS, NO_VARIABLES);

        deleteLastFilterCharacter();
        assertEntries(NO_ACTIONS, new var[]{
                new var("Variable-123", "value-123")
        });

        deleteLastFilterCharacter();
        assertEntries(NO_ACTIONS, new var[]{
                new var("Variable-12", "value-12"),
                new var("Variable-123", "value-123")
        });

        deleteLastFilterCharacter();
        assertEntries(NO_ACTIONS, new var[]{
                new var("Variable-1", "value-1"),
                new var("Variable-12", "value-12"),
                new var("Variable-123", "value-123")
        });

        deleteLastFilterCharacter();
        assertEntries(NO_ACTIONS, new var[]{
                new var("Variable-1", "value-1"),
                new var("Variable-12", "value-12"),
                new var("Variable-123", "value-123"),
                new var("Variable-2", "value-2"),
                new var("Variable-3", "value-3"),
                new var("Variable-4", "value-4")
        });
    }

    @Test
    public void testVariablesFilterPersistence()
    {
        registerVariable(1, "Variable-1", "value-1");
        registerVariable(2, "Variable-12", "value-12");
        registerVariable(3, "Variable-123", "value-123");
        registerVariable(4, "Variable-2", "value-2");
        registerVariable(5, "Variable-3", "value-3");
        registerVariable(6, "Variable-4", "value-4");

        openActions();
        setFilterText("Variable-1");
        assertEntries(NO_ACTIONS, new var[]{
                new var("Variable-1", "value-1"),
                new var("Variable-12", "value-12"),
                new var("Variable-123", "value-123")
        });
        closeActions();

        openActions();
        assertEntries(NO_ACTIONS, new var[]{
                new var("Variable-1", "value-1"),
                new var("Variable-12", "value-12"),
                new var("Variable-123", "value-123")
        });
    }

    //endregion

    //region Mixed

    @Test
    public void testRegisterActionsAndVariables()
    {
        registerAction(1, "Action-1");
        registerAction(2, "Action-2");
        registerAction(3, "Action-3");
        registerVariable(1, "string", "value");
        registerVariable(2, "integer", 10);
        registerVariable(3, "float", 3.14f);
        registerVariable(4, "boolean", false);

        openActions();
        assertEntries(new String[]{"Action-1", "Action-2", "Action-3"}, new var[]{
                new var("boolean", false),
                new var("float", 3.14f),
                new var("integer", 10),
                new var("string", "value")
        });
        closeConsole();

        unregisterActions(2);

        openActions();
        assertEntries(new String[]{"Action-1", "Action-3"}, new var[]{
                new var("boolean", false),
                new var("float", 3.14f),
                new var("integer", 10),
                new var("string", "value")
        });
        closeActions();

        registerAction(4, "Action-4");
        registerAction(5, "Action-2");

        openActions();
        assertEntries(new String[]{"Action-1", "Action-2", "Action-3", "Action-4"}, new var[]{
                new var("boolean", false),
                new var("float", 3.14f),
                new var("integer", 10),
                new var("string", "value")
        });
        closeActions();

        unregisterActions(1, 3, 4, 5);

        openActions();
        assertEntries(NO_ACTIONS, new var[]{
                new var("boolean", false),
                new var("float", 3.14f),
                new var("integer", 10),
                new var("string", "value")
        });
    }

    @Test
    public void testRegisterActionsAndVariablesWhileConsoleOpen()
    {
        openActions();
        assertNoActions();

        registerAction(1, "Action-1");
        registerAction(2, "Action-2");
        registerAction(3, "Action-3");
        registerVariable(1, "string", "value");
        registerVariable(2, "integer", 10);

        assertEntries(new String[]{"Action-1", "Action-2", "Action-3"}, new var[]{
                new var("integer", 10),
                new var("string", "value")
        });

        unregisterActions(2);

        assertEntries(new String[]{"Action-1", "Action-3"}, new var[]{
                new var("integer", 10),
                new var("string", "value")
        });

        registerAction(4, "Action-4");
        registerAction(5, "Action-2");
        registerVariable(3, "float", 3.14f);
        registerVariable(4, "boolean", false);

        assertEntries(new String[]{"Action-1", "Action-2", "Action-3", "Action-4"}, new var[]{
                new var("boolean", false),
                new var("float", 3.14f),
                new var("integer", 10),
                new var("string", "value")
        });

        unregisterActions(1, 3, 4, 5);

        assertEntries(NO_ACTIONS, new var[]{
                new var("boolean", false),
                new var("float", 3.14f),
                new var("integer", 10),
                new var("string", "value")
        });
    }

    @Test
    public void testActionsAndVariablesFilter()
    {
        registerAction(1, "Action-1");
        registerAction(2, "Action-12");
        registerAction(3, "Action-123");
        registerAction(4, "Action-2");
        registerAction(5, "Action-3");
        registerAction(6, "Action-4");
        registerVariable(1, "Variable-1", "value-1");
        registerVariable(2, "Variable-12", "value-12");
        registerVariable(3, "Variable-123", "value-123");
        registerVariable(4, "Variable-2", "value-2");

        openActions();
        assertEntries(new String[]{"Action-1", "Action-12", "Action-123", "Action-2", "Action-3", "Action-4"}, new var[]{
                new var("Variable-1", "value-1"),
                new var("Variable-12", "value-12"),
                new var("Variable-123", "value-123"),
                new var("Variable-2", "value-2")
        });

        appendFilterText("-");
        assertEntries(new String[]{"Action-1", "Action-12", "Action-123", "Action-2", "Action-3", "Action-4"}, new var[]{
                new var("Variable-1", "value-1"),
                new var("Variable-12", "value-12"),
                new var("Variable-123", "value-123"),
                new var("Variable-2", "value-2")
        });

        appendFilterText("1");
        assertEntries(new String[]{"Action-1", "Action-12", "Action-123"}, new var[]{
                new var("Variable-1", "value-1"),
                new var("Variable-12", "value-12"),
                new var("Variable-123", "value-123")
        });

        appendFilterText("2");
        assertEntries(new String[]{"Action-12", "Action-123"}, new var[]{
                new var("Variable-12", "value-12"),
                new var("Variable-123", "value-123")
        });

        appendFilterText("3");
        assertEntries(new String[]{"Action-123"}, new var[]{
                new var("Variable-123", "value-123")
        });

        appendFilterText("4");
        assertEntries(NO_ACTIONS, NO_VARIABLES);

        deleteLastFilterCharacter();
        assertEntries(new String[]{"Action-123"}, new var[]{
                new var("Variable-123", "value-123")
        });

        deleteLastFilterCharacter();
        assertEntries(new String[]{"Action-12", "Action-123"}, new var[]{
                new var("Variable-12", "value-12"),
                new var("Variable-123", "value-123")
        });

        deleteLastFilterCharacter();
        assertEntries(new String[]{"Action-1", "Action-12", "Action-123"}, new var[]{
                new var("Variable-1", "value-1"),
                new var("Variable-12", "value-12"),
                new var("Variable-123", "value-123")
        });

        deleteLastFilterCharacter();
        assertEntries(new String[]{"Action-1", "Action-12", "Action-123", "Action-2", "Action-3", "Action-4"}, new var[]{
                new var("Variable-1", "value-1"),
                new var("Variable-12", "value-12"),
                new var("Variable-123", "value-123"),
                new var("Variable-2", "value-2")
        });
    }

    @Test
    public void testFilterAndAddRemoveActionsWithVariables()
    {
        registerAction(1, "Action-1");
        registerAction(2, "Action-2");
        registerAction(3, "Action-3");
        registerAction(5, "Action5");
        registerVariable(1, "Variable-1", "value-1");
        registerVariable(2, "Variable-2", "value-2");
        registerVariable(3, "Variable-3", "value-3");
        registerVariable(5, "Variable5", "value5");

        openActions();
        assertEntries(new String[]{"Action-1", "Action-2", "Action-3", "Action5"}, new var[]{
                new var("Variable-1", "value-1"),
                new var("Variable-2", "value-2"),
                new var("Variable-3", "value-3"),
                new var("Variable5", "value5"),
        });

        setFilterText("-1");
        assertEntries(new String[]{"Action-1"}, new var[]{
                new var("Variable-1", "value-1")
        });

        registerAction(4, "Action-12");
        assertEntries(new String[]{"Action-1", "Action-12"}, new var[]{
                new var("Variable-1", "value-1")
        });

        unregisterActions(1);
        assertEntries(new String[]{"Action-12"}, new var[]{
                new var("Variable-1", "value-1")
        });

        unregisterActions(4);
        assertEntries(NO_ACTIONS, new var[]{
                new var("Variable-1", "value-1")
        });

        deleteLastFilterCharacter();
        assertEntries(new String[]{"Action-2", "Action-3"}, new var[]{
                new var("Variable-1", "value-1"),
                new var("Variable-2", "value-2"),
                new var("Variable-3", "value-3")
        });

        unregisterActions(3);
        assertEntries(new String[]{"Action-2"}, new var[]{
                new var("Variable-1", "value-1"),
                new var("Variable-2", "value-2"),
                new var("Variable-3", "value-3")
        });

        unregisterActions(2);
        assertEntries(NO_ACTIONS, new var[]{
                new var("Variable-1", "value-1"),
                new var("Variable-2", "value-2"),
                new var("Variable-3", "value-3")
        });

        unregisterActions(5);
        assertEntries(NO_ACTIONS, new var[]{
                new var("Variable-1", "value-1"),
                new var("Variable-2", "value-2"),
                new var("Variable-3", "value-3")
        });

        setFilterText("");
        assertEntries(NO_ACTIONS, new var[]{
                new var("Variable-1", "value-1"),
                new var("Variable-2", "value-2"),
                new var("Variable-3", "value-3"),
                new var("Variable5", "value5")
        });
    }

    @Test
    public void testFilterPersistenceWithActionsAndVariables()
    {
        registerAction(1, "Action-1");
        registerAction(2, "Action-12");
        registerAction(3, "Action-123");
        registerAction(4, "Action-2");
        registerAction(5, "Action-3");
        registerAction(6, "Action-4");
        registerVariable(1, "Variable-1", "value-1");
        registerVariable(2, "Variable-12", "value-12");
        registerVariable(3, "Variable-123", "value-123");
        registerVariable(4, "Variable-2", "value-2");
        registerVariable(5, "Variable-3", "value-3");
        registerVariable(6, "Variable-4", "value-4");

        openActions();
        setFilterText("-1");
        assertEntries(new String[]{"Action-1", "Action-12", "Action-123"}, new var[]{
                new var("Variable-1", "value-1"),
                new var("Variable-12", "value-12"),
                new var("Variable-123", "value-123")
        });
        closeActions();

        openActions();
        assertEntries(new String[]{"Action-1", "Action-12", "Action-123"}, new var[]{
                new var("Variable-1", "value-1"),
                new var("Variable-12", "value-12"),
                new var("Variable-123", "value-123")
        });
    }

    //endregion

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

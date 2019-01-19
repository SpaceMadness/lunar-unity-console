//
//  ConsoleActionsViewTest.java
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

import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.Map;

import spacemadness.com.lunarconsole.console.ConsolePlugin;
import spacemadness.com.lunarconsole.console.IdentityEntry;
import spacemadness.com.lunarconsole.core.NotificationCenter;
import spacemadness.com.lunarconsole.debug.TestHelper;
import spacemadness.com.lunarconsoleapp.helpers.SyncDispatchQueue;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static org.hamcrest.Matchers.*;
import static spacemadness.com.lunarconsole.debug.TestHelper.*;

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

        setFilterText("action");
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

        setFilterText("action-1");
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
        setFilterText("action-1");
        assertEntries(new String[]{"Action-1", "Action-12", "Action-123"}, NO_VARIABLES);
        closeActions();

        openActions();
        assertEntries(new String[]{"Action-1", "Action-12", "Action-123"}, NO_VARIABLES);
    }

    @Test
    public void testTriggeringActions()
    {
        registerAction(1, "Action");

        openActions();
        assertResult("console_open()");

        makeNotificationCenterSync();
        clickAction(0);

        assertResult("console_action({id=1})");
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

        setFilterText("variable");
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
        setFilterText("variable-1");
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

    @Test
    public void testUpdateVariables()
    {
        registerVariable(1, "string", "value", "default value");

        openActions();

        assertText(R.id.lunar_console_variable_entry_value, "value");
        pressButton(R.id.lunar_console_variable_entry_value);

        // ugly hack: sometimes pressing the "edit" button does not work for this test
        int attempts = 10;
        while (!isVisible(R.id.lunar_console_edit_variable_default_value) && attempts > 0)
        {
            pressButton(R.id.lunar_console_variable_entry_value);
            --attempts;
        }

        assertText(R.id.lunar_console_edit_variable_default_value, String.format(getString(R.string.lunar_console_edit_variable_title_default_value), "default value"));
        assertText(R.id.lunar_console_edit_variable_value, "value");
        typeText(R.id.lunar_console_edit_variable_value, "new value");
        pressButton(R.id.lunar_console_edit_variable_button_ok);

        assertText(R.id.lunar_console_variable_entry_value, "new value");
        pressButton(R.id.lunar_console_variable_entry_value);

        assertText(R.id.lunar_console_edit_variable_value, "new value");
        pressButton(R.id.lunar_console_edit_variable_button_reset);

        assertText(R.id.lunar_console_variable_entry_value, "default value");
    }

    @Test
    public void testUpdateVariablesFromThePlugin()
    {
        registerVariable(1, "string", "value", "default value");

        openActions();

        assertText(R.id.lunar_console_variable_entry_value, "value");
        pressButton(R.id.lunar_console_variable_entry_value);

        ConsolePlugin.updateVariable(1, "new value");

        assertText(R.id.lunar_console_variable_entry_value, "new value");
    }

    @Test
    public void testUpdateVariablesFromThePluginWhileEditing()
    {
        registerVariable(1, "string", "value", "default value");

        openActions();

        assertText(R.id.lunar_console_variable_entry_value, "value");
        pressButton(R.id.lunar_console_variable_entry_value);

        // ugly hack: sometimes pressing the "edit" button does not work for this test
        while (!isVisible(R.id.lunar_console_edit_variable_default_value))
        {
            pressButton(R.id.lunar_console_variable_entry_value);
        }

        ConsolePlugin.updateVariable(1, "another value");

        assertText(R.id.lunar_console_edit_variable_default_value, String.format(getString(R.string.lunar_console_edit_variable_title_default_value), "default value"));
        assertText(R.id.lunar_console_edit_variable_value, "value");
        typeText(R.id.lunar_console_edit_variable_value, "new value");
        pressButton(R.id.lunar_console_edit_variable_button_ok);

        assertText(R.id.lunar_console_variable_entry_value, "new value");
        pressButton(R.id.lunar_console_variable_entry_value);

        assertText(R.id.lunar_console_edit_variable_value, "new value");
        pressButton(R.id.lunar_console_edit_variable_button_reset);

        assertText(R.id.lunar_console_variable_entry_value, "default value");
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

    //region Range variables

    @Test
    public void testRangeVariable()
    {
        TestHelper.forceSeekBarChangeDelegate = true;
        makeNotificationCenterSync();

        registerVariable(1, "range", 6.5f, 1.5f, 11.5f);

        openActions();
        assertResult("console_open()");

        assertText(R.id.lunar_console_variable_entry_value, "6.5");

        while (!isVisible(R.id.lunar_console_edit_variable_seek_bar))
        {
            pressButton(R.id.lunar_console_variable_entry_value);
        }

        assertSeekProgress(R.id.lunar_console_edit_variable_seek_bar, 50);
        assertText(R.id.lunar_console_edit_variable_value, "6.5");

        setSeekBarProgress(R.id.lunar_console_edit_variable_seek_bar, 0);
        assertText(R.id.lunar_console_edit_variable_value, "1.5");

        setSeekBarProgress(R.id.lunar_console_edit_variable_seek_bar, 100);
        assertText(R.id.lunar_console_edit_variable_value, "11.5");

        typeText(R.id.lunar_console_edit_variable_value, "10.5");
        assertSeekProgress(R.id.lunar_console_edit_variable_seek_bar, 90);

        pressButton(R.id.lunar_console_edit_variable_button_ok);
        assertResult("console_variable_set({id=1, value=10.5})");

        assertText(R.id.lunar_console_variable_entry_value, "10.5");

        while (!isVisible(R.id.lunar_console_edit_variable_seek_bar))
        {
            pressButton(R.id.lunar_console_variable_entry_value);
        }

        assertText(R.id.lunar_console_edit_variable_value, "10.5");

        pressButton(R.id.lunar_console_edit_variable_button_reset);

        assertText(R.id.lunar_console_variable_entry_value, "6.5");
        assertResult("console_variable_set({id=1, value=6.5})");
    }

    //endregion

    //region Test events

    @Override
    public void onTestEvent(String name, Object data)
    {
        if (name.equals(TEST_EVENT_NATIVE_CALLBACK))
        {
            Map<String, Object> payload = (Map<String, Object>) data;
            String callback = (String) payload.get("name");
            Map<String, Object> args = (Map<String, Object>) payload.get("arguments");
            addResult(String.format("%s(%s)", callback, args != null ? args.toString() : ""));
        }
    }

    //endregion

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Helpers

    private void assertEntries(String[] actions, var[] variables)
    {
        int expectedCount = 0;
        if (actions.length > 0) expectedCount += 1 + actions.length;
        if (variables.length > 0) expectedCount += 1 + variables.length;

        ViewInteraction listView = onView(withParent(withId(R.id.lunar_console_action_view_list_container)));

        // should be visible
        listView.check(matches(isDisplayed()));

        // should contains expected number of children
        listView.check(matches(withListViewSize(expectedCount)));

        if (actions.length > 0)
        {
            // first entry is header
            DataInteraction headerView = onData(allOf(is(instanceOf(IdentityEntry.class))))
                    .inAdapterView(withParent(withId(R.id.lunar_console_action_view_list_container)))
                    .atPosition(0);

            // check title
            headerView
                    .onChildView(withId(R.id.lunar_console_header_entry_name))
                    .check(matches(withText(R.string.lunar_console_header_actions)));

            for (int i = 0; i < actions.length; ++i)
            {
                // find entry view
                DataInteraction entryView = onData(allOf(is(instanceOf(IdentityEntry.class))))
                        .inAdapterView(withParent(withId(R.id.lunar_console_action_view_list_container)))
                        .atPosition(i + 1);

                // check message
                entryView
                        .onChildView(withId(R.id.lunar_console_action_entry_name))
                        .check(matches(withText(actions[i])));
            }
        }

        if (variables.length > 0)
        {
            int offset = actions.length > 0 ? 1 + actions.length : 0;

            // first entry is header
            DataInteraction headerView = onData(allOf(is(instanceOf(IdentityEntry.class))))
                    .inAdapterView(withParent(withId(R.id.lunar_console_action_view_list_container)))
                    .atPosition(offset);

            // check title
            headerView
                    .onChildView(withId(R.id.lunar_console_header_entry_name))
                    .check(matches(withText(R.string.lunar_console_header_variables)));

            for (int i = 0; i < variables.length; ++i)
            {
                // find entry view
                DataInteraction entryView = onData(allOf(is(instanceOf(IdentityEntry.class))))
                        .inAdapterView(withParent(withId(R.id.lunar_console_action_view_list_container)))
                        .atPosition(offset + 1 + i);

                // check name
                entryView
                        .onChildView(withId(R.id.lunar_console_variable_entry_name))
                        .check(matches(withText(variables[i].name)));

                switch (variables[i].type)
                {
                    case String:
                    case Integer:
                    case Float:
                        // check value
                        entryView
                                .onChildView(withId(R.id.lunar_console_variable_entry_value))
                                .check(matches(withText(variables[i].value)));
                        break;
                    case Boolean:
                        // check flag
                        entryView
                                .onChildView(withId(R.id.lunar_console_variable_entry_switch))
                                .check(matches(variables[i].value.equals("0") ? isNotChecked() : isChecked()));
                        break;
                    default:
                        throw new AssertionError("Unexpected type: " + variables[i].type);
                }
            }
        }
    }

    private void clickAction(int index)
    {
        ViewInteraction listView = onView(withParent(withId(R.id.lunar_console_action_view_list_container)));

        // should be visible
        listView.check(matches(isDisplayed()));

        onData(allOf(is(instanceOf(IdentityEntry.class))))
                .inAdapterView(withParent(withId(R.id.lunar_console_action_view_list_container)))
                .atPosition(1 + index).perform(click());
    }

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

    private static void makeNotificationCenterSync()
    {
        try
        {
            Field dispatchQueueField = NotificationCenter.class.getDeclaredField("dispatchQueue");
            dispatchQueueField.setAccessible(true);
            dispatchQueueField.set(NotificationCenter.defaultCenter(), new SyncDispatchQueue());
        }
        catch (Exception e)
        {
            throw new AssertionError(e);
        }
    }
}

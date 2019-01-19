//
//  ConsoleLogViewTest.java
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

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import spacemadness.com.lunarconsole.console.ConsoleLogType;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ConsoleLogViewTest extends ApplicationBaseUITest
{
    @Test
    public void testFilter()
    {
        logMessage("Debug-1", ConsoleLogType.LOG);
        logMessage("Debug-2", ConsoleLogType.LOG);
        logMessage("Warning-1", ConsoleLogType.WARNING);
        logMessage("Warning-2", ConsoleLogType.WARNING);
        logMessage("Error-1", ConsoleLogType.ERROR);
        logMessage("Error-2", ConsoleLogType.ERROR);

        openConsole();

        assertLogEntries("Debug-1", "Debug-2", "Warning-1", "Warning-2", "Error-1", "Error-2");

        pressButton(R.id.lunar_console_log_button);
        assertLogEntries("Warning-1", "Warning-2", "Error-1", "Error-2");

        pressButton(R.id.lunar_console_warning_button);
        assertLogEntries("Error-1", "Error-2");

        pressButton(R.id.lunar_console_error_button);
        assertLogEntries();

        pressButton(R.id.lunar_console_log_button);
        assertLogEntries("Debug-1", "Debug-2");

        pressButton(R.id.lunar_console_warning_button);
        assertLogEntries("Debug-1", "Debug-2", "Warning-1", "Warning-2");

        pressButton(R.id.lunar_console_error_button);
        assertLogEntries("Debug-1", "Debug-2", "Warning-1", "Warning-2", "Error-1", "Error-2");

        clearText(R.id.lunar_console_log_view_text_edit_filter);

        appendText(R.id.lunar_console_log_view_text_edit_filter, "1");
        assertLogEntries("Debug-1", "Warning-1", "Error-1");

        appendText(R.id.lunar_console_log_view_text_edit_filter, "1");
        assertLogEntries();

        deleteLastChar(R.id.lunar_console_log_view_text_edit_filter);
        assertLogEntries("Debug-1", "Warning-1", "Error-1");

        deleteLastChar(R.id.lunar_console_log_view_text_edit_filter);
        assertLogEntries("Debug-1", "Debug-2", "Warning-1", "Warning-2", "Error-1", "Error-2");

        appendText(R.id.lunar_console_log_view_text_edit_filter, "2");
        assertLogEntries("Debug-2", "Warning-2", "Error-2");

        appendText(R.id.lunar_console_log_view_text_edit_filter, "2");
        assertLogEntries();

        clearText(R.id.lunar_console_log_view_text_edit_filter);
        assertLogEntries("Debug-1", "Debug-2", "Warning-1", "Warning-2", "Error-1", "Error-2");
    }

    @Test
    public void testCollapse()
    {
        // add elements to console
        logMessage("Debug", ConsoleLogType.LOG);
        logMessage("Warning", ConsoleLogType.WARNING);
        logMessage("Error", ConsoleLogType.ERROR);
        logMessage("Debug", ConsoleLogType.LOG);
        logMessage("Warning", ConsoleLogType.WARNING);
        logMessage("Error", ConsoleLogType.ERROR);

        // present controller
        openConsole();

        // collapse elements
        openConsoleMenu();
        pressMenuButton(R.string.lunar_console_more_menu_collapse);

        assertLogEntries("Debug@2", "Warning@2", "Error@2");

        // close controller
        pressButton(R.id.lunar_console_button_close);

        // re-open controller
        openConsole();

        assertLogEntries("Debug@2", "Warning@2", "Error@2");

        // expand elements
        openConsoleMenu();
        pressButton(getString(R.string.lunar_console_more_menu_collapse));

        assertLogEntries("Debug", "Warning", "Error", "Debug", "Warning", "Error");
    }

    @Test
    public void testOverflow()
    {
        // set trim
        typeText(R.id.test_edit_text_trim, "3");
        pressButton(R.id.test_button_set_trim);

        // set capacity
        typeText(R.id.test_edit_text_capacity, "5");
        pressButton(R.id.test_button_set_capacity);

        // add elements to console
        logMessage("Debug-1", ConsoleLogType.LOG);
        logMessage("Warning-1", ConsoleLogType.WARNING);
        logMessage("Error-1", ConsoleLogType.ERROR);
        logMessage("Debug-2", ConsoleLogType.LOG);
        logMessage("Warning-2", ConsoleLogType.WARNING);

        // show controller
        openConsole();

        // check table
        assertLogEntries("Debug-1", "Warning-1", "Error-1", "Debug-2", "Warning-2");

        // overflow message should be invisible
        assertHidden(R.id.lunar_console_text_overflow);

        // close controller
        pressButton(R.id.lunar_console_button_close);

        // make console overflow
        logMessage("Error-2", ConsoleLogType.ERROR);

        // show controller
        openConsole();

        // check table
        assertLogEntries("Debug-2", "Warning-2", "Error-2");

        // overflow message should be visible
        assertVisible(R.id.lunar_console_text_overflow);

        // check overflow message
        assertText(R.id.lunar_console_text_overflow, String.format(getString(R.string.lunar_console_overflow_warning_text), 3));
    }
}

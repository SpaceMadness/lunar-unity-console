//
//  ExceptionWarningTest.java
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
import spacemadness.com.lunarconsole.console.ConsolePlugin;
import spacemadness.com.lunarconsole.settings.PluginSettings;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExceptionWarningTest extends ApplicationBaseUITest
{
    @Test
    public void testExceptionWarning()
    {
        assertExceptionWarningInvisible();

        logMessage("Error 1", ConsoleLogType.ERROR);
        assertExceptionWarning("Error 1");

        logMessage("Error 2", ConsoleLogType.ERROR);
        assertExceptionWarning("Error 2");

        logMessage("Error 3", ConsoleLogType.ERROR);
        assertExceptionWarning("Error 3");

        pressButton(R.id.lunar_console_warning_button_dismiss);
        assertExceptionWarningInvisible();
    }

    @Test
    public void testExceptionWarningButton()
    {
        assertExceptionWarningInvisible();

        logMessage("Error 1", ConsoleLogType.ERROR);
        assertExceptionWarning("Error 1");

        pressButton(R.id.lunar_console_warning_button_details);
        assertExceptionWarningInvisible();

        assertLogEntries("Error 1");
    }

    @Test
    public void testDisabledExceptionWarning()
    {
        final PluginSettings settings = ConsolePlugin.pluginSettings();
        settings.setEnableExceptionWarning(false);

        logMessage("Error 1", ConsoleLogType.ERROR);
        assertExceptionWarningInvisible();

        settings.setEnableExceptionWarning(true);
        logMessage("Error 2", ConsoleLogType.ERROR);
        assertExceptionWarning("Error 2");

        pressButton(R.id.lunar_console_warning_button_details);
        assertExceptionWarningInvisible();

        assertLogEntries("Error 1", "Error 2");
    }
}

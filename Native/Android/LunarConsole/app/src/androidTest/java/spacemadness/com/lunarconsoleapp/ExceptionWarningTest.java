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

        assertTable("Error 1");
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

        assertTable("Error 1", "Error 2");
    }
}

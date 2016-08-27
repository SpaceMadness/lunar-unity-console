package spacemadness.com.lunarconsoleapp;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import spacemadness.com.lunarconsole.console.ConsoleLogType;

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
}

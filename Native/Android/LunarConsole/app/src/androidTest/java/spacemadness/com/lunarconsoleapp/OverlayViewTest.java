package spacemadness.com.lunarconsoleapp;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import spacemadness.com.lunarconsole.console.OverlayView;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class OverlayViewTest extends ApplicationBaseUITest
{
    @Test
    public void testOverlayVisibility()
    {
        assertDoesNotExist(OverlayView.class);

        openSettings();
        pressButton("Enable Transparent Log Overlay");
        closeSettings();
        closeConsole();

        assertVisible(OverlayView.class);

        openSettings();
        pressButton("Enable Transparent Log Overlay");
        closeSettings();
        closeConsole();

        assertDoesNotExist(OverlayView.class);
    }
}

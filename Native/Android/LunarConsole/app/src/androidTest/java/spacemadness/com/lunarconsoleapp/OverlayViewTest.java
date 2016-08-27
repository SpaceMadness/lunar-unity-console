package spacemadness.com.lunarconsoleapp;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class OverlayViewTest extends ApplicationBaseUITest
{
    @Test
    public void testOverlayView()
    {
        openSettings();
    }
}

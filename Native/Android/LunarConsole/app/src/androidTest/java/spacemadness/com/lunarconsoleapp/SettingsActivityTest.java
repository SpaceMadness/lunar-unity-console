package spacemadness.com.lunarconsoleapp;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import spacemadness.com.lunarconsole.console.ConsolePlugin;
import spacemadness.com.lunarconsole.settings.PluginSettings;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SettingsActivityTest extends ApplicationBaseUITest
{
    @Test
    public void testSettings()
    {
        PluginSettings settings = ConsolePlugin.pluginSettings();
        settings.setEnableExceptionWarning(true);
        settings.setEnableTransparentLogOverlay(false);
        settings.save();

        openSettings();
        assertPreferenceChecked("Enable Exception Warning", settings.isEnableExceptionWarning());
        assertPreferenceChecked("Enable Transparent Log Overlay", settings.isEnableTransparentLogOverlay());
        pressBackButton();

        settings.setEnableExceptionWarning(false);
        settings.setEnableTransparentLogOverlay(true);
        settings.save();

        openSettings();
        assertPreferenceChecked("Enable Exception Warning", settings.isEnableExceptionWarning());
        assertPreferenceChecked("Enable Transparent Log Overlay", settings.isEnableTransparentLogOverlay());

        boolean expectedEnableExceptionWarning = !settings.isEnableExceptionWarning();
        boolean expectedEnableTransparentLogOverlay = !settings.isEnableTransparentLogOverlay();

        pressButton("Enable Exception Warning");
        pressButton("Enable Transparent Log Overlay");

        Assert.assertEquals(expectedEnableExceptionWarning, settings.isEnableExceptionWarning());
        Assert.assertEquals(expectedEnableTransparentLogOverlay, settings.isEnableTransparentLogOverlay());

        settings = ConsolePlugin.pluginSettings();
        Assert.assertEquals(expectedEnableExceptionWarning, settings.isEnableExceptionWarning());
        Assert.assertEquals(expectedEnableTransparentLogOverlay, settings.isEnableTransparentLogOverlay());
    }
}

//
//  SettingsActivityTest.java
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

//
//  OverlayViewTest.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2016 Alex Lementuev, SpaceMadness.
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

    @Test
    public void testOverlayEntries()
    {
        openSettings();
        pressButton("Enable Transparent Log Overlay");
        closeSettings();
        closeConsole();

        // add elements to console
        logMessage("Debug", ConsoleLogType.LOG);
        logMessage("Warning", ConsoleLogType.WARNING);
        logMessage("Error", ConsoleLogType.ERROR);
    }
}

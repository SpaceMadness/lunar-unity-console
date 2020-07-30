//
//  PluginSettingsTest.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2015-2020 Alex Lementuev, SpaceMadness.
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


package spacemadness.com.lunarconsole.settings;

import spacemadness.com.lunarconsole.InstrumentationTestCase;
import spacemadness.com.lunarconsole.json.JsonDecoder;
import spacemadness.com.lunarconsole.settings.ExceptionWarningSettings.DisplayMode;

public class PluginSettingsTest extends InstrumentationTestCase {
    private static LogEntryColors createOverlayColor(Color foreground, Color background) {
        LogEntryColors colors = new LogEntryColors();
        colors.foreground = foreground;
        colors.background = background;
        return colors;
    }

    public void testParseJson() {
        final String settingJson = readTextAsset("editor-settings.json");
        final PluginSettings actual = JsonDecoder.decode(settingJson, PluginSettings.class);

        // this looks ugly
        final PluginSettings expected = new PluginSettings();
        final ExceptionWarningSettings exceptionWarning = new ExceptionWarningSettings();
        exceptionWarning.displayMode = DisplayMode.ALL;
        expected.exceptionWarning = exceptionWarning;

        final LogColors logOverlayColors = new LogColors();
        logOverlayColors.exception = createOverlayColor(createColor(10, 11, 12, 13), createColor(14, 15, 16, 17));
        logOverlayColors.error = createOverlayColor(createColor(20, 21, 22, 23), createColor(24, 25, 26, 27));
        logOverlayColors.warning = createOverlayColor(createColor(30, 31, 32, 33), createColor(34, 35, 36, 37));
        logOverlayColors.debug = createOverlayColor(createColor(40, 41, 42, 43), createColor(44, 45, 46, 47));
        final LogOverlaySettings logOverlay = new LogOverlaySettings();
        logOverlay.enabled = false;
        logOverlay.maxVisibleLines = 3;
        logOverlay.timeout = 1.0f;
        logOverlay.colors = logOverlayColors;
        expected.logOverlay = logOverlay;

        expected.capacity = 4096;
        expected.trim = 512;
        expected.gesture = Gesture.SWIPE_DOWN;
        expected.richTextTags = false;
        expected.sortActions = true;
        expected.sortVariables = false;
        expected.emails = new String[]{
                "a.lementuev@gmail.com",
                "lunar.plugin@gmail.com"
        };

        assertEquals(expected, actual);
    }

    private Color createColor(int r, int g, int b, int a) {
        Color color = new Color();
        color.r = r;
        color.g = g;
        color.b = b;
        color.a = a;
        return color;
    }
}
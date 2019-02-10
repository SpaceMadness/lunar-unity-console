//
//  PluginSettingsTest.java
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

package spacemadness.com.lunarconsole.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.test.InstrumentationTestCase;

public class PluginSettingsTest extends InstrumentationTestCase
{
    private PluginSettings settings;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        final Context context = getContext();
        final SharedPreferences preferences = PluginSettings.getSharedPreferences(context);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        settings = new PluginSettings(context);
    }

    public void testSaveLoad()
    {
        assertEquals(true, settings.isEnableExceptionWarning());
        assertEquals(false, settings.isEnableTransparentLogOverlay());

        settings.setEnableExceptionWarning(false);
        settings.setEnableTransparentLogOverlay(true);

        boolean saved = settings.save();
        assertTrue(saved);

        settings = new PluginSettings(getContext());
        assertEquals(false, settings.isEnableExceptionWarning());
        assertEquals(true, settings.isEnableTransparentLogOverlay());
    }

    private Context getContext()
    {
        return getInstrumentation().getContext();
    }
}
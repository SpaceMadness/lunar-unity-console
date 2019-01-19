//
//  SettingsActivity.java
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

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;

import java.lang.reflect.Field;
import java.util.List;

import spacemadness.com.lunarconsole.console.ConsolePlugin;
import spacemadness.com.lunarconsole.console.LunarConsoleConfig;
import spacemadness.com.lunarconsole.debug.Log;
import spacemadness.com.lunarconsole.utils.StringUtils;

public class SettingsActivity extends PreferenceActivity
{
    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getPreferenceManager().setSharedPreferencesName(PluginSettings.PREFS_NAME);

        PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(this);

        PreferenceCategory category = new PreferenceCategory(this);
        category.setTitle("Settings");
        screen.addPreference(category);
        try
        {
            createPreferences(category);
        }
        catch (Exception e)
        {
            Log.e(e, "Unable to create preferences activity");
        }
        setPreferenceScreen(screen);
    }

    private void createPreferences(PreferenceCategory category) throws IllegalAccessException
    {
        // FIXME: better solution!
        final PluginSettings settings = ConsolePlugin.pluginSettings();
        if (settings != null)
        {
            final List<Field> fields = settings.listFields();
            for (Field field : fields)
            {
                Preference preference = createPreference(settings, field);
                if (preference != null)
                {
                    category.addPreference(preference);
                }
            }
        }
    }

    private Preference createPreference(final PluginSettings settings, final Field field) throws IllegalAccessException
    {
        final Class<?> type = field.getType();
        field.setAccessible(true);
        final Object value = field.get(settings);

        PluginSettingsEntry annotation = field.getAnnotation(PluginSettingsEntry.class);
        boolean proOnly = annotation != null && annotation.proOnly();

        Preference preference = createPreference(type, value);
        if (preference != null)
        {
            preference.setEnabled(LunarConsoleConfig.isFull || !proOnly);
            preference.setKey(field.getName());
            preference.setTitle(StringUtils.camelCaseToWords(field.getName()));
            preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
            {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue)
                {
                    try
                    {
                        field.set(settings, newValue);
                        return true;
                    }
                    catch (Exception e)
                    {
                        Log.e(e, "Unable to change preference");
                        return false;
                    }
                }
            });
        }

        return preference;
    }

    private Preference createPreference(Class<?> type, Object value)
    {
        if (type == boolean.class)
        {
            CheckBoxPreference checkBoxPreference = new CheckBoxPreference(this);
            checkBoxPreference.setChecked((boolean) value);
            return checkBoxPreference;
        }

        return null;
    }
}

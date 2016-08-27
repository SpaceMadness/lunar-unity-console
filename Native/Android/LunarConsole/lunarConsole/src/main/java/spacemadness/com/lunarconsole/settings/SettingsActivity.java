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
import spacemadness.com.lunarconsole.console.PluginSettings;
import spacemadness.com.lunarconsole.debug.Log;

public class SettingsActivity extends PreferenceActivity
{
    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

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

    private Preference createPreference(PluginSettings settings, Field field) throws IllegalAccessException
    {
        final Class<?> type = field.getType();
        field.setAccessible(true);
        Object value = field.get(settings);

        Preference preference = createPreference(type, value);
        if (preference != null)
        {
            preference.setKey(field.getName());
            preference.setTitle("title");
            preference.setSummary("summary");
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

//
//  PluginSettings.java
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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.List;

import spacemadness.com.lunarconsole.debug.Log;
import spacemadness.com.lunarconsole.utils.ClassUtils;

import static spacemadness.com.lunarconsole.debug.Tags.*;
import static spacemadness.com.lunarconsole.utils.ClassUtils.FieldFilter;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface PluginSettingsEntry
{
    boolean proOnly() default false;
}

public class PluginSettings
{
    static final String PREFS_NAME = "spacemadness.com.lunarconsole.console.PluginSettings";

    private final WeakReference<Context> contextRef;

    private final FieldFilter fieldFilter = new FieldFilter()
    {
        @Override
        public boolean accept(Field field)
        {
            return field.getAnnotation(PluginSettingsEntry.class) != null;
        }
    };

    @PluginSettingsEntry
    private boolean enableExceptionWarning;

    @PluginSettingsEntry(proOnly = true)
    private boolean enableTransparentLogOverlay;

    public PluginSettings(Context context)
    {
        if (context == null)
        {
            throw new NullPointerException("Context is null");
        }
        contextRef = new WeakReference<>(context);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Save/Load

    public boolean load()
    {
        final Context context = getContext();
        if (context == null)
        {
            Log.e(SETTINGS, "Can't load settings: context reference is lost");
            return false;
        }

        try
        {
            final SharedPreferences preferences = getSharedPreferences(context);
            final List<Field> fields = listFields();
            for (Field field : fields)
            {
                field.setAccessible(true);

                final String name = field.getName();
                if (!preferences.contains(name))
                {
                    continue;
                }

                Object value;

                final Class<?> type = field.getType();
                if (type == boolean.class)
                {
                    value = preferences.getBoolean(name, false);
                }
                else if (type == int.class)
                {
                    value = preferences.getInt(name, 0);
                }
                else
                {
                    Log.e("Unexpected setting field type: %s", type);
                    continue;
                }

                field.set(this, value);
            }

            return true;
        }
        catch (Exception e)
        {
            Log.e(e, "Can't load plugin settings");
        }

        return false;
    }

    public boolean save()
    {
        final Context context = getContext();
        if (context == null)
        {
            Log.e(SETTINGS, "Can't save settings: context reference is lost");
            return false;
        }

        try
        {
            final SharedPreferences preferences = getSharedPreferences(context);
            final SharedPreferences.Editor editor = preferences.edit();

            final List<Field> fields = listFields();
            for (Field field : fields)
            {
                field.setAccessible(true);

                final String name = field.getName();
                Object value = field.get(this);

                if (value instanceof Boolean)
                {
                    editor.putBoolean(name, (Boolean) value);
                }
                else if (value instanceof Integer)
                {
                    editor.putInt(name, (Integer) value);
                }
            }
            editor.apply();
            return true;
        }
        catch (Exception e)
        {
            Log.e(e, "Can't save plugin settings");
        }

        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Fields

    public List<Field> listFields()
    {
        return ClassUtils.listFields(getClass(), fieldFilter);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Helpers

    static SharedPreferences getSharedPreferences(Context context)
    {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static void clear(Context context)
    {
        final SharedPreferences sharedPreferences = getSharedPreferences(context);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    public boolean isEnableExceptionWarning()
    {
        return enableExceptionWarning;
    }

    public void setEnableExceptionWarning(boolean enableExceptionWarning)
    {
        this.enableExceptionWarning = enableExceptionWarning;
    }

    public boolean isEnableTransparentLogOverlay()
    {
        return enableTransparentLogOverlay;
    }

    public void setEnableTransparentLogOverlay(boolean enableTransparentLogOverlay)
    {
        this.enableTransparentLogOverlay = enableTransparentLogOverlay;
    }

    private Context getContext()
    {
        return contextRef.get();
    }
}

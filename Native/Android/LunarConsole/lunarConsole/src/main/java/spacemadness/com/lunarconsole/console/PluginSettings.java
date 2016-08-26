package spacemadness.com.lunarconsole.console;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.List;

import spacemadness.com.lunarconsole.debug.Log;
import spacemadness.com.lunarconsole.utils.ClassUtils;

import static spacemadness.com.lunarconsole.debug.Tags.SETTINGS;
import static spacemadness.com.lunarconsole.utils.ClassUtils.FieldFilter;

@Target(ElementType.FIELD)
@interface PluginSettingsEntry
{
    String defaultValue();
}

public class PluginSettings
{
    private static final String PREFS_NAME = "spacemadness.com.lunarconsole.console.PluginSettings";

    private final WeakReference<Context> contextRef;

    private final FieldFilter fieldFilter = new FieldFilter()
    {
        @Override
        public boolean accept(Field field)
        {
            return field.getAnnotation(PluginSettingsEntry.class) != null;
        }
    };

    @PluginSettingsEntry(defaultValue = "true")
    private boolean enableExceptionWarning;

    @PluginSettingsEntry(defaultValue = "false")
    private boolean enableTransparentLogOverlay;

    public PluginSettings(Context context)
    {
        if (context == null)
        {
            throw new NullPointerException("Context is null");
        }
        contextRef = new WeakReference<>(context);
        load();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Save/Load

    private boolean load()
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
                final Class<?> type = field.getType();
                final PluginSettingsEntry annotation = field.getAnnotation(PluginSettingsEntry.class);

                Object value;
                if (type == boolean.class)
                {
                    final boolean defValue = Boolean.parseBoolean(annotation.defaultValue());
                    value = preferences.getBoolean(name, defValue);
                }
                else if (type == boolean.class)
                {
                    final int defValue = Integer.parseInt(annotation.defaultValue());
                    value = preferences.getInt(name, defValue);
                }
                else
                {
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

    SharedPreferences getSharedPreferences(Context context)
    {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
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

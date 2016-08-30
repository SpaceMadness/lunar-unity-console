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
        settings = new PluginSettings(context);
        final SharedPreferences preferences = settings.getSharedPreferences(context);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
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
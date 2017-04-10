package spacemadness.com.lunarconsole.console;

import org.json.JSONObject;

public class EditorSettings
{
    public boolean enableExceptionWarning = true;
    public boolean enableTransparentLogOverlay = false;
    public boolean sortActions = true;
    public boolean sortVariables = true;

    public EditorSettings()
    {
    }

    public static EditorSettings fromJson(String jsonString) throws EditorSettingsException
    {
        try
        {
            JSONObject json = new JSONObject(jsonString);

            EditorSettings settings = new EditorSettings();
            settings.enableExceptionWarning = json.getBoolean("exceptionWarning");
            settings.enableTransparentLogOverlay = json.getBoolean("transparentLogOverlay");
            settings.sortActions = json.getBoolean("sortActions");
            settings.sortVariables = json.getBoolean("sortVariables");
            return settings;
        }
        catch (Exception e)
        {
            throw new EditorSettingsException("Invalid settings json: " + jsonString, e);
        }
    }
}

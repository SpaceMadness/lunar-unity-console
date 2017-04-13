//
//  EditorSettings.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2017 Alex Lementuev, SpaceMadness.
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

//
//  ConsoleViewState.java
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

package spacemadness.com.lunarconsole.console;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.ref.WeakReference;

import spacemadness.com.lunarconsole.debug.Log;

public class ConsoleViewState
{
    private static final String KEY_LEFT_MARGIN         = "leftMargin";
    private static final String KEY_RIGHT_MARGIN        = "rightMargin";
    private static final String KEY_TOP_MARGIN          = "topMargin";
    private static final String KEY_BOTTOM_MARGIN       = "bottomMargin";
    private static final String KEY_ACTION_FILTER_TEXT  = "actionFilterText";

    private final WeakReference<Context> contextRef;

    private int leftMargin;
    private int rightMargin;
    private int topMargin;
    private int bottomMargin;

    private String actionFilterText;

    public ConsoleViewState(Context context)
    {
        contextRef = new WeakReference<>(context);
        load();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    public int getBottomMargin()
    {
        return bottomMargin;
    }

    public int getTopMargin()
    {
        return topMargin;
    }

    public int getRightMargin()
    {
        return rightMargin;
    }

    public int getLeftMargin()
    {
        return leftMargin;
    }

    public void setMargins(int topMargin, int bottomMargin, int leftMargin, int rightMargin)
    {
        this.topMargin = topMargin;
        this.bottomMargin = bottomMargin;
        this.leftMargin = leftMargin;
        this.rightMargin = rightMargin;

        saveMargins();
    }

    public String getActionFilterText()
    {
        return actionFilterText;
    }

    public void setActionFilterText(String actionFilterText)
    {
        this.actionFilterText = actionFilterText;

        saveActionFilterText();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Persistent storage

    private void load()
    {
        try
        {
            SharedPreferences preferences = getSharedPreferences();
            int leftMargin = preferences.getInt(KEY_LEFT_MARGIN, 0);
            int rightMargin = preferences.getInt(KEY_RIGHT_MARGIN, 0);
            int topMargin = preferences.getInt(KEY_TOP_MARGIN, 0);
            int bottomMargin = preferences.getInt(KEY_BOTTOM_MARGIN, 0);
            String actionFilterText = preferences.getString(KEY_ACTION_FILTER_TEXT, null);

            this.leftMargin = leftMargin;
            this.rightMargin = rightMargin;
            this.topMargin = topMargin;
            this.bottomMargin = bottomMargin;
            this.actionFilterText = actionFilterText;
        }
        catch (Exception e)
        {
            Log.e(e, "Exception while loading margins");
        }
    }

    private void saveMargins()
    {
        try
        {
            SharedPreferences preferences = getSharedPreferences();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(KEY_LEFT_MARGIN, leftMargin);
            editor.putInt(KEY_RIGHT_MARGIN, rightMargin);
            editor.putInt(KEY_TOP_MARGIN, topMargin);
            editor.putInt(KEY_BOTTOM_MARGIN, bottomMargin);
            editor.apply();
        }
        catch (Exception e)
        {
            Log.e(e, "Exception while saving margins");
        }
    }

    private void saveActionFilterText()
    {
        try
        {
            SharedPreferences preferences = getSharedPreferences();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(KEY_ACTION_FILTER_TEXT, actionFilterText);
            editor.apply();
        }
        catch (Exception e)
        {
            Log.e(e, "Exception while saving margins");
        }
    }

    private SharedPreferences getSharedPreferences()
    {
        return getSharedPreferences(getContext());
    }

    private static SharedPreferences getSharedPreferences(Context context)
    {
        String prefsName = ConsoleViewState.class.getCanonicalName();
        return context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
    }

    private Context getContext()
    {
        return contextRef.get();
    }

    // for testing
    public static void clear(Context context)
    {
        getSharedPreferences(context).edit().clear().apply();
    }
}
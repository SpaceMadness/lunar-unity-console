//
//  LUEntry.java
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

package spacemadness.com.lunarconsole.console.actions;

import android.content.Context;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.console.ConsoleEntry;

public abstract class LUEntry extends ConsoleEntry implements Comparable<LUEntry> // FIXME: rename
{
    private final int actionId; // FIXME: rename
    private final String name;

    public LUEntry(int actionId, String name)
    {
        this.actionId = actionId;
        this.name = name;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Appearance

    @SuppressWarnings("deprecation")
    public int getBackgroundColor(Context context, int position)
    {
        int colorId = position % 2 == 0 ?
                R.color.lunar_console_color_cell_background_dark :
                R.color.lunar_console_color_cell_background_light;
        return context.getResources().getColor(colorId);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    public int actionId() // FIXME: rename
    {
        return actionId;
    }

    public String name() // FIXME: rename
    {
        return name;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Comparable

    @Override
    public int compareTo(LUEntry another)
    {
        return name.compareTo(another.name);
    }
}

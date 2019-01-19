//
//  IdentityEntry.java
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

import spacemadness.com.lunarconsole.R;

public abstract class IdentityEntry extends BaseEntry implements Comparable<IdentityEntry>
{
    private final int entryId;
    private final String name;

    public IdentityEntry(int entryId, String name)
    {
        this.entryId = entryId;
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

    @Override
    public long getItemId()
    {
        return getEntryType().ordinal();
    }

    public int actionId() // FIXME: rename
    {
        return entryId;
    }

    public String name() // FIXME: rename
    {
        return name;
    }

    public abstract EntryType getEntryType();

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Comparable

    @Override
    public int compareTo(IdentityEntry another)
    {
        return name.compareTo(another.name);
    }
}

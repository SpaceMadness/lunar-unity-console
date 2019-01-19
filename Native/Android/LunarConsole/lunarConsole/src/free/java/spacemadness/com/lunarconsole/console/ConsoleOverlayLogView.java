//
//  ConsoleOverlayLogView.java
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
import android.view.View;

import spacemadness.com.lunarconsole.core.Destroyable;

public class ConsoleOverlayLogView extends View implements Destroyable
{
    public ConsoleOverlayLogView(Context context, Console console, Settings settings)
    {
        super(context);
    }

    @Override
    public void destroy()
    {
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Settings

    public static class Settings
    {
        /** How many entries can be visible at the same time */
        public int maxVisibleEntries;

        /** How much time each row would be displayed on the screen */
        public long entryDisplayTimeMillis;

        public Settings()
        {
            maxVisibleEntries = 3;
            entryDisplayTimeMillis = 1000;
        }
    }
}

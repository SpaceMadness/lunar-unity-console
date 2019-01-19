//
//  ViewActionsEx.java
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

package spacemadness.com.lunarconsoleapp.helpers;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;
import android.widget.SeekBar;

import org.hamcrest.Matcher;

public class ViewActionsEx
{
    public static ViewAction setSeekBarProgress(final int progress)
    {
        return new ViewAction()
        {
            @Override
            public Matcher<View> getConstraints()
            {
                return ViewMatchers.isAssignableFrom(SeekBar.class);
            }

            @Override
            public String getDescription()
            {
                return "seek bar progress is expected to be " + progress;
            }

            @Override
            public void perform(UiController uiController, View view)
            {
                ((SeekBar) view).setProgress(progress);
            }
        };
    }
}

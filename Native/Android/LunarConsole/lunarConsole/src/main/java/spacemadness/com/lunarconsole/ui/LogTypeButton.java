//
//  LogTypeButton.java
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

package spacemadness.com.lunarconsole.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

public class LogTypeButton extends ToggleButton
{
    private static final int MAX_COUNT = 999;

    private int count;
    private float offAlpha;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LogTypeButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public LogTypeButton(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    public LogTypeButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LogTypeButton(Context context)
    {
        super(context);
        init();
    }

    private void init()
    {
        count = Integer.MAX_VALUE;
        offAlpha = 0.25f;
    }

    @Override
    public void setOn(boolean flag)
    {
        super.setOn(flag);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            setAlpha(flag ? 1.0f : offAlpha);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    public void setCount(int count)
    {
        if (this.count != count)
        {
            if (count < MAX_COUNT)
            {
                setText(Integer.toString(count));
            }
            else if (this.count < MAX_COUNT)
            {
                setText(MAX_COUNT + "+");
            }
            this.count = count;
        }
    }

    public void setOffAlpha(float offAlpha)
    {
        this.offAlpha = offAlpha;
    }

    public float getOffAlpha()
    {
        return offAlpha;
    }
}

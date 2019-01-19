//
//  AbstractConsoleView.java
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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import spacemadness.com.lunarconsole.core.Destroyable;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public abstract class AbstractConsoleView extends LinearLayout implements Destroyable
{
    /** Root view from the layout file */
    private final View rootView;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor

    public AbstractConsoleView(Context context, int rootViewId)
    {
        super(context);

        // disable touches
        setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true; // don't let touch events to pass through the view group
            }
        });

        // might not be the most efficient way but we'll keep it for now
        rootView = LayoutInflater.from(context).inflate(rootViewId, this, false);
        addView(rootView, new LayoutParams(MATCH_PARENT, MATCH_PARENT));

        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Destroyable

    @Override
    public void destroy()
    {
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Helpers

    protected <T extends View> T findExistingViewById(int id) throws ClassCastException
    {
        return findExistingViewById(rootView, id);
    }

    protected <T extends View> T findExistingViewById(View parent, int id) throws ClassCastException
    {
        View view = parent.findViewById(id);
        if (view == null)
        {
            throw new IllegalArgumentException("View with id " + id + " not found");
        }

        return (T) view;
    }

    protected void setOnClickListener(int viewId, View.OnClickListener listener)
    {
        View view = findExistingViewById(viewId);
        view.setOnClickListener(listener);
    }
}

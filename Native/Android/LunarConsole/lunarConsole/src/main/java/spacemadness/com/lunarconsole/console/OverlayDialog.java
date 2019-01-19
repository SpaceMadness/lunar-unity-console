//
//  OverlayDialog.java
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

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import static android.widget.FrameLayout.LayoutParams.*;

class OverlayDialog extends Dialog
{
    private final int systemUIVisibilityFlags;
    private final Activity activity;
    private final FrameLayout contentView;
    private BackButtonListener backButtonListener;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public OverlayDialog(Activity activity, int themeResId)
    {
        super(activity, themeResId);

        this.activity = activity;

        setCancelable(false);

        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.requestFeature(Window.FEATURE_NO_TITLE);

        // we need to mimic Unity's navigation bar behaviour
        View decorView = activity.getWindow().getDecorView();
        systemUIVisibilityFlags = decorView.getSystemUiVisibility();
        window.getDecorView().setSystemUiVisibility(systemUIVisibilityFlags);

        // set content view
        contentView = new FrameLayout(activity);
        setContentView(contentView, new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
    }

    public void addView(View view)
    {
        contentView.addView(view);
    }

    public void addView(View view, FrameLayout.LayoutParams params)
    {
        contentView.addView(view, params);
    }

    public void removeView(View view)
    {
        contentView.removeView(view);
    }

    public int getChildCount()
    {
        return contentView.getChildCount();
    }

    @Override
    public void onBackPressed()
    {
        if (backButtonListener != null)
        {
            backButtonListener.onBackPressed();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return activity.onTouchEvent(event); // pass touch events to the host activity
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            getWindow().getDecorView().setSystemUiVisibility(systemUIVisibilityFlags);
        }
    }

    public void setBackButtonListener(BackButtonListener backButtonListener)
    {
        this.backButtonListener = backButtonListener;
    }

    public void setOnTouchListener(View.OnTouchListener onTouchListener)
    {
        getWindow().getDecorView().setOnTouchListener(onTouchListener);
    }
}

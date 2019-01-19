//
//  ViewPager.java
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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class ViewPager extends HorizontalScrollView
{
    private static final int MIN_FLING_VELOCITY = 1000;

    private LinearLayout contentLayout;
    private GestureDetector gestureDetector;

    public ViewPager(Context context)
    {
        super(context);
        setupUI(context);
    }

    public ViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setupUI(context);
    }

    public ViewPager(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        setupUI(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ViewPager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupUI(context);
    }

    private void setupUI(Context context)
    {
        contentLayout = new LinearLayout(context);
        contentLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(contentLayout, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));

        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);

        // TODO: figure out a better solution
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener()
        {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
            {
                if (Math.abs(velocityX) >= MIN_FLING_VELOCITY)
                {
                    if (velocityX > 0)
                    {
                        scrollToPage(0, true);
                    }
                    else
                    {
                        scrollToPage(1, true);
                    }

                    return true;
                }

                scrollToClosestPage(true);
                return true;
            }
        });
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int size = MeasureSpec.getSize(widthMeasureSpec);

        for (int i = 0; i < contentLayout.getChildCount(); i++)
        {
            View child = contentLayout.getChildAt(i);
            if (child.getLayoutParams().width != size)
            {
                child.setLayoutParams(new LinearLayout.LayoutParams(size, LayoutParams.MATCH_PARENT));
            }

        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Touch input

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (gestureDetector.onTouchEvent(event))
        {
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            scrollToClosestPage(true);
            return true;
        }

        return super.onTouchEvent(event);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Appearance

    @Override
    protected float getLeftFadingEdgeStrength()
    {
        return 0.0f;
    }

    @Override
    protected float getRightFadingEdgeStrength()
    {
        return 0.0f;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Pages

    public void addPageView(View child)
    {
        int width = getWidth();
        child.setLayoutParams(new LayoutParams(width, LayoutParams.MATCH_PARENT));
        contentLayout.addView(child);
        contentLayout.requestLayout();
    }

    public void scrollToPage(int pageIndex, boolean smooth)
    {
        int pos = pageIndex * getWidth();
        if (smooth)
        {
            smoothScrollTo(pos, 0);
        }
        else
        {
            scrollTo(pos, 0);
        }
    }

    private void scrollToClosestPage(boolean smooth)
    {
        final int width = getWidth();
        scrollToPage((getScrollX() + width / 2) / width, smooth);
    }
}
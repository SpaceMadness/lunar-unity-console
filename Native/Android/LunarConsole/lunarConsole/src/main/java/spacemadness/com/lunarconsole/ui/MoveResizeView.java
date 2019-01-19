//
//  MoveResizeView.java
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
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.core.Destroyable;
import spacemadness.com.lunarconsole.utils.MathUtils;

public class MoveResizeView extends LinearLayout implements Destroyable
{
    private static final int OPERATION_NONE = 0;
    private static final int OPERATION_MOVE = 1 << 0;
    private static final int OPERATION_RESIZE_TOP = 1 << 1;
    private static final int OPERATION_RESIZE_BOTTOM = 1 << 2;
    private static final int OPERATION_RESIZE_LEFT = 1 << 3;
    private static final int OPERATION_RESIZE_RIGHT = 1 << 4;
    private static final int OPERATION_RESIZE_TOP_LEFT = OPERATION_RESIZE_TOP | OPERATION_RESIZE_LEFT;
    private static final int OPERATION_RESIZE_TOP_RIGHT = OPERATION_RESIZE_TOP | OPERATION_RESIZE_RIGHT;
    private static final int OPERATION_RESIZE_BOTTOM_LEFT = OPERATION_RESIZE_BOTTOM | OPERATION_RESIZE_LEFT;

    private int operation;
    private int minWidth;
    private int minHeight;
    private float lastX;
    private float lastY;
    private OnCloseListener closeListener;

    /** The target layout for resizing */
    private RelativeLayout targetView;

    public MoveResizeView(Context context)
    {
        super(context);
        loadViewFromXml(context);
    }

    public MoveResizeView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        loadViewFromXml(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public MoveResizeView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        loadViewFromXml(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MoveResizeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        loadViewFromXml(context);
    }

    private void loadViewFromXml(Context context)
    {
        minWidth = context.getResources().getDimensionPixelSize(R.dimen.lunar_console_move_resize_min_width);
        minHeight = context.getResources().getDimensionPixelSize(R.dimen.lunar_console_move_resize_min_height);

        LayoutInflater inflater = LayoutInflater.from(context);
        targetView = (RelativeLayout) inflater.inflate(R.layout.lunar_console_layout_move_resize, null, false);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(targetView, layoutParams);
        setupUI();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // UI setup

    private void setupUI()
    {
        // close button
        targetView.findViewById(R.id.lunar_console_resize_button_close)
                .setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (closeListener != null)
                        {
                            closeListener.onClose(MoveResizeView.this);
                        }
                    }
                });

        // resize handles
        final SparseIntArray operationLookup = new SparseIntArray();
        operationLookup.append(R.id.lunar_console_resize_bar_top, OPERATION_RESIZE_TOP);
        operationLookup.append(R.id.lunar_console_resize_bar_bottom, OPERATION_RESIZE_BOTTOM);
        operationLookup.append(R.id.lunar_console_resize_bar_left, OPERATION_RESIZE_LEFT);
        operationLookup.append(R.id.lunar_console_resize_bar_right, OPERATION_RESIZE_RIGHT);
        operationLookup.append(R.id.lunar_console_resize_bar_top_left, OPERATION_RESIZE_TOP_LEFT);
        operationLookup.append(R.id.lunar_console_resize_bar_top_right, OPERATION_RESIZE_TOP_RIGHT);
        operationLookup.append(R.id.lunar_console_resize_bar_bottom_left, OPERATION_RESIZE_BOTTOM_LEFT);

        OnTouchListener resizeHandleTouchListener = new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    operation = operationLookup.get(v.getId());
                }

                return false;
            }
        };

        for (int i = 0; i < operationLookup.size(); ++i)
        {
            int id = operationLookup.keyAt(i);
            targetView.findViewById(id).setOnTouchListener(resizeHandleTouchListener);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Touch events

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                lastY = event.getY();

                if (operation == OPERATION_NONE)
                {
                    operation = OPERATION_MOVE;
                }
                return true;

            case MotionEvent.ACTION_MOVE:
                int dx = (int) (event.getX() - lastX);
                int dy = (int) (event.getY() - lastY);
                lastX = event.getX();
                lastY = event.getY();
                return onPointerMove(dx, dy);

            case MotionEvent.ACTION_UP:
                operation = OPERATION_NONE;
                return true;
        }

        return false;
    }

    protected boolean onPointerMove(int dx, int dy)
    {
        final MarginLayoutParams layoutParams = getMarginLayoutParams();
        final ViewGroup parentGroup = (ViewGroup) targetView.getParent();
        final int width = parentGroup.getWidth();
        final int height = parentGroup.getHeight();

        if (operation == OPERATION_MOVE)
        {
            if (dx > 0 && layoutParams.rightMargin - dx < 0)
            {
                dx = layoutParams.rightMargin;
            }
            else if (dx < 0 && layoutParams.leftMargin + dx < 0)
            {
                dx = -layoutParams.leftMargin;
            }

            if (dy > 0 && layoutParams.bottomMargin - dy < 0)
            {
                dy = layoutParams.bottomMargin;
            }
            else if (dy < 0 && layoutParams.topMargin + dy < 0)
            {
                dy = -layoutParams.topMargin;
            }

            layoutParams.leftMargin += dx;
            layoutParams.rightMargin -= dx;
            layoutParams.topMargin += dy;
            layoutParams.bottomMargin -= dy;
        }
        else
        {
            if ((operation & OPERATION_RESIZE_TOP) != 0)
            {
                layoutParams.topMargin = MathUtils.clamp(layoutParams.topMargin + dy, 0, height - (minHeight + layoutParams.bottomMargin));
            }
            else if ((operation & OPERATION_RESIZE_BOTTOM) != 0)
            {
                layoutParams.bottomMargin = MathUtils.clamp(layoutParams.bottomMargin - dy, 0, height - (minHeight + layoutParams.topMargin));
            }

            if ((operation & OPERATION_RESIZE_LEFT) != 0)
            {
                layoutParams.leftMargin = MathUtils.clamp(layoutParams.leftMargin + dx, 0, width - (minWidth + layoutParams.rightMargin));
            }
            else if ((operation & OPERATION_RESIZE_RIGHT) != 0)
            {
                layoutParams.rightMargin = MathUtils.clamp(layoutParams.rightMargin - dx, 0, width - (minWidth + layoutParams.leftMargin));
            }
        }

        targetView.setLayoutParams(layoutParams);
        targetView.invalidate();

        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Layout margins

    public void setMargins(int left, int top, int right, int bottom)
    {
        final MarginLayoutParams layoutParams = (MarginLayoutParams) targetView.getLayoutParams();
        layoutParams.setMargins(left, top, right, bottom);
        targetView.invalidate();
    }

    public int getTopMargin()
    {
        return getMarginLayoutParams().topMargin;
    }

    public int getBottomMargin()
    {
        return getMarginLayoutParams().bottomMargin;
    }

    public int getLeftMargin()
    {
        return getMarginLayoutParams().leftMargin;
    }

    public int getRightMargin()
    {
        return getMarginLayoutParams().rightMargin;
    }

    private MarginLayoutParams getMarginLayoutParams()
    {
        return (MarginLayoutParams) targetView.getLayoutParams();
    }

    public OnCloseListener getOnCloseListener()
    {
        return closeListener;
    }

    public void setOnCloseListener(OnCloseListener closeListener)
    {
        this.closeListener = closeListener;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    // Destroyable

    @Override
    public void destroy()
    {
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    // Listener

    public interface OnCloseListener
    {
        void onClose(MoveResizeView view);
    }
}

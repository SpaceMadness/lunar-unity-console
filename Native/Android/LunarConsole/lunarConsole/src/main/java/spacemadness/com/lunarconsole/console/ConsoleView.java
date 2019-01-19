//
//  ConsoleView.java
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
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.core.Destroyable;
import spacemadness.com.lunarconsole.debug.Assert;
import spacemadness.com.lunarconsole.ui.MoveResizeView;
import spacemadness.com.lunarconsole.ui.ViewPager;
import spacemadness.com.lunarconsole.utils.ObjectUtils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class ConsoleView extends LinearLayout implements BackButtonListener, Destroyable
{
    private final ConsoleViewState consoleViewState;
    private final ConsoleLogView consoleLogView;
    private final ConsoleActionView consoleActionView;

    /** An overlay layout for move/resize operations */
    private MoveResizeView moveResizeView;

    private Listener listener;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors

    public ConsoleView(Activity activity, ConsolePlugin consolePlugin)
    {
        super(activity);

        if (consolePlugin == null)
        {
            throw new NullPointerException("Console plugin is null");
        }

        consoleViewState = consolePlugin.getConsoleViewState();

        View rootView = LayoutInflater.from(activity).inflate(R.layout.lunar_console_layout_console_view, this, false);
        addView(rootView, new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));

        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.lunar_console_view_pager);

        consoleLogView = new ConsoleLogView(activity, consolePlugin.getConsole());
        consoleLogView.setEmails(consolePlugin.getEmails());
        viewPager.addPageView(consoleLogView);

        consoleActionView = new ConsoleActionView(activity, consolePlugin); // FIXME: these classes know too much about each other
        viewPager.addPageView(consoleActionView);

        consoleLogView.setOnMoveSizeListener(new ConsoleLogView.OnMoveSizeListener()
        {
            @Override
            public void onMoveResize(ConsoleLogView consoleLogView)
            {
                showMoveResizeView(getContext());
            }
        });

        // focus
        setFocusable(true);
        setFocusableInTouchMode(true);

        // setup close button
        ImageButton closeButton = (ImageButton) rootView.findViewById(R.id.lunar_console_button_close);
        closeButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                notifyClose();
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Move/Resize

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void showMoveResizeView(Context context)
    {
        Assert.IsNull(moveResizeView);
        if (moveResizeView == null)
        {
            final FrameLayout parentLayout = ObjectUtils.as(getParent(), FrameLayout.class);
            Assert.IsNotNull(parentLayout);

            if (parentLayout != null)
            {
                moveResizeView = new MoveResizeView(context);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
                parentLayout.addView(moveResizeView, layoutParams);

                final MarginLayoutParams p = (MarginLayoutParams) getLayoutParams();
                moveResizeView.setMargins(p.leftMargin, p.topMargin, p.rightMargin, p.bottomMargin);

                // hide page views
                setPageViewsVisible(false);

                // handle close button
                moveResizeView.setOnCloseListener(new MoveResizeView.OnCloseListener()
                {
                    @Override
                    public void onClose(MoveResizeView view)
                    {
                        hideMoveResizeView();
                    }
                });
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void hideMoveResizeView()
    {
        Assert.IsNotNull(moveResizeView);
        if (moveResizeView != null)
        {
            final MarginLayoutParams parentLayoutParams = (MarginLayoutParams) getLayoutParams();

            parentLayoutParams.topMargin = moveResizeView.getTopMargin();
            parentLayoutParams.bottomMargin = moveResizeView.getBottomMargin();
            parentLayoutParams.leftMargin = moveResizeView.getLeftMargin();
            parentLayoutParams.rightMargin = moveResizeView.getRightMargin();
            invalidate();

            // update state margins
            consoleViewState.setMargins(moveResizeView.getTopMargin(),
                    moveResizeView.getBottomMargin(),
                    moveResizeView.getLeftMargin(),
                    moveResizeView.getRightMargin());

            final ViewGroup parent = (ViewGroup) moveResizeView.getParent();
            parent.removeView(moveResizeView);

            moveResizeView.destroy();
            moveResizeView = null;

            // show page views
            setPageViewsVisible(true);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setPageViewsVisible(boolean visible)
    {
        int visibility = visible ? VISIBLE : GONE;

        consoleLogView.setVisibility(visibility);
        consoleActionView.setVisibility(visibility);
    }

    private boolean isMoveResizeViewVisible()
    {
        return moveResizeView != null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Back button

    @Override
    public void onBackPressed()
    {
        if (isMoveResizeViewVisible())
        {
            hideMoveResizeView();
        }
        else
        {
            notifyClose();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Destroyable

    @Override
    public void destroy()
    {
        consoleLogView.destroy();
        consoleActionView.destroy();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Listener notifications

    void notifyOpen()
    {
        if (listener != null)
        {
            listener.onOpen(this);
        }
    }

    void notifyClose()
    {
        if (listener != null)
        {
            listener.onClose(this);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    public Listener getListener()
    {
        return listener;
    }

    public void setListener(Listener listener)
    {
        this.listener = listener;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Listener

    public interface Listener
    {
        void onOpen(ConsoleView view);
        void onClose(ConsoleView view);
    }
}

//
//  WarningView.java
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
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.core.Destroyable;
import spacemadness.com.lunarconsole.debug.Log;

import static android.widget.FrameLayout.LayoutParams.*;
import static android.view.Gravity.*;
import static spacemadness.com.lunarconsole.debug.Tags.WARNING_VIEW;

public class WarningView extends FrameLayout implements Destroyable
{
    private TextView messageText;
    private Listener listener;

    public WarningView(Context context)
    {
        super(context);
        init(context);
    }

    public WarningView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public WarningView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WarningView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.lunar_console_layout_warning, null, false);

        setupUI(view);

        LayoutParams params = new LayoutParams(MATCH_PARENT, WRAP_CONTENT, CENTER_HORIZONTAL | BOTTOM);
        addView(view, params);
    }

    private void setupUI(View view)
    {
        messageText = (TextView) view.findViewById(R.id.lunar_console_warning_text_message);
        view.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true; // don't let touch events to pass through the view group
            }
        });

        setOnClickListener(view, R.id.lunar_console_warning_button_dismiss, new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                notifyDismiss();
            }
        });

        setOnClickListener(view, R.id.lunar_console_warning_button_details, new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                notifyDetails();
            }
        });
    }

    private void setOnClickListener(View root, int id, View.OnClickListener listener)
    {
        root.findViewById(id).setOnClickListener(listener);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Destroyable

    @Override
    public void destroy()
    {
        Log.d(WARNING_VIEW, "Destroy warning");
        listener = null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Notifications

    private void notifyDismiss()
    {
        if (listener != null)
        {
            try
            {
                listener.onDismissClick(this);
            }
            catch (Exception e)
            {
                Log.e("Error while notifying listener");
            }
        }
    }

    private void notifyDetails()
    {
        if (listener != null)
        {
            try
            {
                listener.onDetailsClick(this);
            }
            catch (Exception e)
            {
                Log.e("Error while notifying listener");
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    public void setMessage(String message)
    {
        messageText.setText(message);
    }

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
        void onDismissClick(WarningView view);
        void onDetailsClick(WarningView view);
    }
}

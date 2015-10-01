package spacemadness.com.lunarconsole.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

public class ToggleImageButton extends ImageButton
{
    private OnStateChangeListener stateChangeListener;
    private boolean on;

    private Drawable onDrawable;
    private Drawable offDrawable;

    public ToggleImageButton(Context context)
    {
        super(context);
        init();
    }

    public ToggleImageButton(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ToggleImageButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public ToggleImageButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    private void init()
    {
        setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setOn(!on);
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Listener notifications

    private void notifyStateChanged()
    {
        if (stateChangeListener != null)
        {
            stateChangeListener.onStateChanged(ToggleImageButton.this);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    public boolean isOn()
    {
        return on;
    }

    public void setOn(boolean flag)
    {
        boolean oldFlag = on;

        on = flag;

        Drawable stateDrawable = on ? onDrawable : offDrawable;
        if (stateDrawable != null)
        {
            setImageDrawable(stateDrawable);
        }

        if (oldFlag != flag)
        {
            notifyStateChanged();
        }
    }

    public OnStateChangeListener getOnStateChangeListener()
    {
        return stateChangeListener;
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener)
    {
        this.stateChangeListener = onStateChangeListener;
    }

    public Drawable getOnDrawable()
    {
        return onDrawable;
    }

    public void setOnDrawable(Drawable onDrawable)
    {
        this.onDrawable = onDrawable;
    }

    public Drawable getOffDrawable()
    {
        return offDrawable;
    }

    public void setOffDrawable(Drawable offDrawable)
    {
        this.offDrawable = offDrawable;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Listener

    public interface OnStateChangeListener
    {
        void onStateChanged(ToggleImageButton button);
    }
}

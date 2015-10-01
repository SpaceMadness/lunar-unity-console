package spacemadness.com.lunarconsole.ui;

import android.view.MotionEvent;

import spacemadness.com.lunarconsole.debug.Log;

public abstract class GestureRecognizer<T extends GestureRecognizer>
{
    private OnGestureListener<T> listener;

    public abstract boolean onTouchEvent(MotionEvent event);

    protected void notifyGestureRecognizer()
    {
        if (listener != null)
        {
            try
            {
                listener.onGesture((T) this);
            }
            catch (Exception e)
            {
                Log.e(e, "Error while notifying gesture listener");
            }
        }
    }

    public OnGestureListener<T> getListener()
    {
        return listener;
    }

    public void setListener(OnGestureListener<T> listener)
    {
        this.listener = listener;
    }

    public interface OnGestureListener<T extends GestureRecognizer>
    {
        void onGesture(T gestureRecognizer);
    }
}

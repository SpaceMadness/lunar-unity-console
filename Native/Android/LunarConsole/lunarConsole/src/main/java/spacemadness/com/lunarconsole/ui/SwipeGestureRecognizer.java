package spacemadness.com.lunarconsole.ui;

import android.view.MotionEvent;

public class SwipeGestureRecognizer extends GestureRecognizer<SwipeGestureRecognizer>
{
    public enum SwipeDirection
    {
        Up,
        Down,
        Left,
        Right
    }

    private final SwipeDirection direction;
    private final float threshold;

    private boolean swiping;

    private float startX;
    private float startY;
    private float endX;
    private float endY;

    public SwipeGestureRecognizer(SwipeDirection direction, float threshold)
    {
        this.direction = direction;
        this.threshold = threshold;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_POINTER_DOWN:
            {
                swiping = true;
                startX = event.getX(0);
                startY = event.getY(0);
                break;
            }

            case MotionEvent.ACTION_POINTER_UP:
            {
                if (swiping)
                {
                    handleSwipe(direction, endX - startX, endY - startY);
                    swiping = false;
                }
                break;
            }

            case MotionEvent.ACTION_MOVE:
            {
                if (swiping)
                {
                    endX = event.getX(0);
                    endY = event.getY(0);
                }
                break;
            }
        }

        return true;
    }

    private void handleSwipe(SwipeDirection direction, float distX, float distY)
    {
        if (direction == SwipeDirection.Down && distY >= threshold ||
            direction == SwipeDirection.Up && -distY >= threshold ||
            direction == SwipeDirection.Right && distX >= threshold ||
            direction == SwipeDirection.Left && -distX >= threshold)
        {
            notifyGestureRecognizer();
        }
    }
}

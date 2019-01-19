//
//  TwoFingerSwipeGestureRecognizer.java
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

package spacemadness.com.lunarconsole.ui.gestures;

import android.view.MotionEvent;

public class TwoFingerSwipeGestureRecognizer extends GestureRecognizer<TwoFingerSwipeGestureRecognizer>
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

    private final TouchMotion firstTouchMotion;
    private final TouchMotion secondTouchMotion;

    public TwoFingerSwipeGestureRecognizer(SwipeDirection direction, float threshold)
    {
        this.direction = direction;
        this.threshold = threshold;

        firstTouchMotion = new TouchMotion();
        secondTouchMotion = new TouchMotion();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int action = event.getActionMasked();
        int index = event.getActionIndex();
        int id = event.getPointerId(index);

        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
            {
                firstTouchMotion.reset();
                secondTouchMotion.reset();

                firstTouchMotion.id = id;
                firstTouchMotion.startX = event.getX(index);
                firstTouchMotion.startY = event.getY(index);

                break;
            }

            case MotionEvent.ACTION_UP:
            {
                if (firstTouchMotion.isActive() && secondTouchMotion.isActive())
                {
                    if (isRightDirection(direction, firstTouchMotion) && isRightDirection(direction, secondTouchMotion))
                    {
                        notifyGestureRecognizer();
                    }
                }

                firstTouchMotion.reset();
                secondTouchMotion.reset();
                break;
            }

            case MotionEvent.ACTION_POINTER_DOWN:
            {
                if (firstTouchMotion.isActive() && !secondTouchMotion.isActive())
                {
                    secondTouchMotion.id = id;
                    secondTouchMotion.startX = event.getX(index);
                    secondTouchMotion.startY = event.getY(index);
                }
                else
                {
                    firstTouchMotion.reset();
                    secondTouchMotion.reset();
                }
                break;
            }

            case MotionEvent.ACTION_POINTER_UP:
            {
                break;
            }

            case MotionEvent.ACTION_MOVE:
            {
                int pointerCount = event.getPointerCount();
                for(int pointerIndex = 0; pointerIndex < pointerCount; ++pointerIndex)
                {
                    int pointerId = event.getPointerId(pointerIndex);
                    if(pointerId == firstTouchMotion.id)
                    {
                        firstTouchMotion.endX = event.getX(pointerIndex);
                        firstTouchMotion.endY = event.getY(pointerIndex);
                    }
                    if(pointerId == secondTouchMotion.id)
                    {
                        secondTouchMotion.endX = event.getX(pointerIndex);
                        secondTouchMotion.endY = event.getY(pointerIndex);
                    }
                }
                break;
            }
        }

        return true;
    }

    private boolean isRightDirection(SwipeDirection direction, TouchMotion touch)
    {
        float distX = touch.distanceX();
        float distY = touch.distanceY();
        return direction == SwipeDirection.Down && distY >= threshold ||
                direction == SwipeDirection.Up && -distY >= threshold ||
                direction == SwipeDirection.Right && distX >= threshold ||
                direction == SwipeDirection.Left && -distX >= threshold;
    }
}

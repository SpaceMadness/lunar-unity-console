//
//  TwoFingerSwipeGestureRecognizer.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2015-2021 Alex Lementuev, SpaceMadness.
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

public class TwoFingerSwipeGestureRecognizer extends GestureRecognizer<TwoFingerSwipeGestureRecognizer> {
    private static final int INVALID_POINTER_ID = -1;
    private static final float DIRECTION_TOLERANCE = 0.5f; // Allow 50% deviation between fingers
    
    private final SwipeDirection direction;
    private final float threshold;
    private final TouchMotion firstTouchMotion;
    private final TouchMotion secondTouchMotion;
    public TwoFingerSwipeGestureRecognizer(SwipeDirection direction, float threshold) {
        this.direction = direction;
        this.threshold = threshold;

        firstTouchMotion = new TouchMotion();
        secondTouchMotion = new TouchMotion();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        int index = event.getActionIndex();
        int id = event.getPointerId(index);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                resetGesture();

                firstTouchMotion.id = id;
                firstTouchMotion.startX = event.getX(index);
                firstTouchMotion.startY = event.getY(index);

                break;
            }

            case MotionEvent.ACTION_UP: {
                checkAndNotifyGesture();
                resetGesture();
                break;
            }

            case MotionEvent.ACTION_POINTER_DOWN: {
                if (firstTouchMotion.isActive() && !secondTouchMotion.isActive()) {
                    secondTouchMotion.id = id;
                    secondTouchMotion.startX = event.getX(index);
                    secondTouchMotion.startY = event.getY(index);
                } else {
                    // More than 2 fingers detected, reset gesture
                    resetGesture();
                }
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                // Check if gesture is complete when one finger is lifted
                if (id == firstTouchMotion.id || id == secondTouchMotion.id) {
                    checkAndNotifyGesture();
                    resetGesture();
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                updateTouchPositions(event);
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                // Gesture interrupted by system, reset state
                resetGesture();
                break;
            }
        }

        return true;
    }

    private void resetGesture() {
        firstTouchMotion.reset();
        secondTouchMotion.reset();
    }

    private void updateTouchPositions(MotionEvent event) {
        int pointerCount = event.getPointerCount();
        for (int pointerIndex = 0; pointerIndex < pointerCount; ++pointerIndex) {
            int pointerId = event.getPointerId(pointerIndex);
            if (pointerId == firstTouchMotion.id) {
                firstTouchMotion.endX = event.getX(pointerIndex);
                firstTouchMotion.endY = event.getY(pointerIndex);
                if (!secondTouchMotion.isActive()) {
                    break; // Early exit if only tracking first finger
                }
            } else if (pointerId == secondTouchMotion.id) {
                secondTouchMotion.endX = event.getX(pointerIndex);
                secondTouchMotion.endY = event.getY(pointerIndex);
                break; // Found second finger, no need to continue
            }
        }
    }

    private void checkAndNotifyGesture() {
        if (firstTouchMotion.isActive() && secondTouchMotion.isActive()) {
            if (isValidTwoFingerSwipe()) {
                notifyGestureRecognizer();
            }
        }
    }

    private boolean isValidTwoFingerSwipe() {
        // Check if both fingers moved in the correct direction
        if (!isRightDirection(direction, firstTouchMotion) || !isRightDirection(direction, secondTouchMotion)) {
            return false;
        }

        // Validate that both fingers moved in roughly the same direction (gesture consistency)
        return areFingersMovingConsistently(firstTouchMotion, secondTouchMotion);
    }

    private boolean isRightDirection(SwipeDirection direction, TouchMotion touch) {
        float distX = touch.distanceX();
        float distY = touch.distanceY();
        
        switch (direction) {
            case Down:
                return distY >= threshold;
            case Up:
                return -distY >= threshold;
            case Right:
                return distX >= threshold;
            case Left:
                return -distX >= threshold;
            default:
                return false;
        }
    }

    private boolean areFingersMovingConsistently(TouchMotion first, TouchMotion second) {
        float firstDistX = first.distanceX();
        float firstDistY = first.distanceY();
        float secondDistX = second.distanceX();
        float secondDistY = second.distanceY();

        // Check if both fingers are moving in the same general direction
        // by comparing the signs and relative magnitudes
        switch (direction) {
            case Down:
            case Up:
                // For vertical swipes, check Y consistency
                if (Math.signum(firstDistY) != Math.signum(secondDistY)) {
                    return false;
                }
                // Allow some deviation but ensure both moved significantly in Y
                float minY = Math.min(Math.abs(firstDistY), Math.abs(secondDistY));
                float maxY = Math.max(Math.abs(firstDistY), Math.abs(secondDistY));
                return minY >= maxY * DIRECTION_TOLERANCE;

            case Left:
            case Right:
                // For horizontal swipes, check X consistency
                if (Math.signum(firstDistX) != Math.signum(secondDistX)) {
                    return false;
                }
                // Allow some deviation but ensure both moved significantly in X
                float minX = Math.min(Math.abs(firstDistX), Math.abs(secondDistX));
                float maxX = Math.max(Math.abs(firstDistX), Math.abs(secondDistX));
                return minX >= maxX * DIRECTION_TOLERANCE;

            default:
                return false;
        }
    }

    public enum SwipeDirection {
        Up,
        Down,
        Left,
        Right
    }
}

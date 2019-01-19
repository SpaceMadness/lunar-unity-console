//
//  GestureRecognizer.java
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

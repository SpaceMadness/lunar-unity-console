//
//  TouchMotion.java
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

public class TouchMotion
{
    public int id;
    public float startX;
    public float startY;
    public float endX;
    public float endY;

    public TouchMotion()
    {
        reset();
    }

    public void reset()
    {
        id = -1;
        startX = startY = endX = endY = 0;
    }

    public float distanceX()
    {
        return endX - startX;
    }

    public float distanceY()
    {
        return endY - startY;
    }

    public boolean isActive()
    {
        return id != -1;
    }

    @Override
    public String toString()
    {
        return String.format("start: (%d, %d) end: (%d, %d) distanceX: %d distanceY: %d",
                (int) startX, (int) startY, (int) endX, (int) endY, (int) distanceX(), (int) distanceY());
    }
}

//
//  BackgroundDispatchQueue.java
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

package spacemadness.com.lunarconsoleapp;

import android.os.HandlerThread;

public class BackgroundDispatchQueue extends DispatchQueue
{
    private final HandlerThread handlerThread;

    protected BackgroundDispatchQueue(HandlerThread handlerThread)
    {
        super(handlerThread.getLooper());
        this.handlerThread = handlerThread;
    }

    public static BackgroundDispatchQueue create(String name)
    {
        HandlerThread t = new HandlerThread(name);
        t.start();
        return new BackgroundDispatchQueue(t);
    }
}

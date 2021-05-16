//
//  SerialDispatchQueue.java
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


package spacemadness.com.lunarconsole.concurrent;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.checkNotNull;

public class SerialDispatchQueue extends DispatchQueue {
    private final Handler handler;
    private final HandlerThread handlerThread;

    public SerialDispatchQueue(Looper looper, String name) {
        super(name);
        handler = new Handler(checkNotNull(looper, "looper"));
        handlerThread = null;
    }

    public SerialDispatchQueue(String name) {
        super(name);
        handlerThread = new HandlerThread(name);
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    @Override
    protected void schedule(DispatchTask task, long delay) {
        if (delay > 0) {
            handler.postDelayed(task, delay);
        } else {
            handler.post(task);
        }
    }

    @Override
    public void stop() {
        if (handlerThread != null) {
            handler.removeCallbacks(null);
            handlerThread.quit();
        }
    }

    @Override
    public boolean isCurrent() {
        return handler.getLooper() == Looper.myLooper();
    }
}
//
//  ConsoleLogEntryDispatcher.java
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

import java.util.ArrayList;
import java.util.List;

import spacemadness.com.lunarconsole.concurrent.DispatchQueue;
import spacemadness.com.lunarconsole.concurrent.DispatchTask;
import spacemadness.com.lunarconsole.debug.Log;

/**
 * Class for handling batches of console entries on UI-thread
 */
class ConsoleLogEntryDispatcher {
    private final OnDispatchListener listener;
    private final List<ConsoleLogEntry> entries;
    private final DispatchQueue dispatchQueue;
    private final DispatchTask dispatchTask;

    public ConsoleLogEntryDispatcher(OnDispatchListener listener) {
        this(DispatchQueue.mainQueue(), listener);
    }

    public ConsoleLogEntryDispatcher(DispatchQueue dispatchQueue, OnDispatchListener listener) {
        if (dispatchQueue == null) {
            throw new NullPointerException("Dispatch queue is null");
        }
        if (listener == null) {
            throw new NullPointerException("Listener is null");
        }

        this.dispatchQueue = dispatchQueue;
        this.listener = listener;
        this.entries = new ArrayList<>();
        this.dispatchTask = createDispatchTask();
    }

    public void add(ConsoleLogEntry entry) {
        synchronized (entries) {
            entries.add(entry);

            if (entries.size() == 1) {
                postEntriesDispatch();
            }
        }
    }

    protected void postEntriesDispatch() {
        dispatchQueue.dispatchOnce(dispatchTask);
    }

    protected void cancelEntriesDispatch() {
        dispatchTask.cancel();
    }

    protected void dispatchEntries() {
        synchronized (entries) {
            try {
                listener.onDispatchEntries(entries);
            } catch (Exception e) {
                Log.e(e, "Can't dispatch entries");
            }
            entries.clear();
        }
    }

    private DispatchTask createDispatchTask() {
        return new DispatchTask() {
            @Override
            protected void execute() {
                dispatchEntries();
            }
        };
    }

    public void cancelAll() {
        cancelEntriesDispatch();

        synchronized (entries) {
            entries.clear();
        }
    }

    public interface OnDispatchListener {
        void onDispatchEntries(List<ConsoleLogEntry> entries);
    }
}

//
//  DispatchTask.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2015-2020 Alex Lementuev, SpaceMadness.
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

import spacemadness.com.lunarconsole.debug.Log;

/**
 * A basic class for any dispatch runnable task. Tracks its "schedule" state
 */
public abstract class DispatchTask implements Runnable {

    /**
     * Optional name of the task
     */
    private final String name;

    /**
     * True if task is already on the queue and would be executed soon.
     */
    private boolean scheduled;

    /**
     * True if task is cancelled and should not be executed.
     */
    private boolean cancelled;

    public DispatchTask() {
        this(null);
    }

    public DispatchTask(String name) {
        this.name = name;
    }

    /**
     * Task entry point method
     */
    protected abstract void execute();

    @Override
    public void run() {
        try {
            setScheduled(false);

            if (!isCancelled()) {
                execute();
            }
        } catch (Exception e) {
            Log.e(e, name != null ? "Exception while executing task: " + name : "Exception while executing task");
        } finally {
            setCancelled(false);
        }
    }

    synchronized void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }

    public synchronized boolean isScheduled() {
        return scheduled;
    }

    private synchronized void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public synchronized boolean isCancelled() {
        return cancelled;
    }

    public synchronized void cancel() {
        if (scheduled) {
            cancelled = true;
        }
    }
}
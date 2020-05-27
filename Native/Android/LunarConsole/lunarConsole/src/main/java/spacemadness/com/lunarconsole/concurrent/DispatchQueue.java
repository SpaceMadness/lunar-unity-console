//
//  DispatchQueue.java
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

import spacemadness.com.lunarconsole.dependency.DispatchQueueProvider;
import spacemadness.com.lunarconsole.dependency.Provider;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.checkNotNull;

public abstract class DispatchQueue implements Dispatcher {
    private final String name;

    public DispatchQueue(String name) {
        this.name = checkNotNull(name, "name");
    }

    public boolean dispatchOnce(DispatchTask task) {
        return dispatchOnce(task, 0L);
    }

    public boolean dispatchOnce(DispatchTask task, long delayMillis) {
        if (!task.isScheduled()) {
            dispatch(task, delayMillis);
            return true;
        }
        return false;
    }

    @Override
    public void dispatch(DispatchTask task) {
        dispatch(task, 0L);
    }

    public void dispatch(DispatchTask task, long delay) {
		task.setScheduled(true);
		schedule(task, delay);
    }

    protected abstract void schedule(DispatchTask task, long delay);

    public abstract void stop();

    public abstract boolean isCurrent();

    public String getName() {
        return name;
    }

    public static DispatchQueue mainQueue() {
        return Holder.MAIN_QUEUE;
    }

    public static DispatchQueue createSerialQueue(String name) {
        return Holder.provider.createSerialQueue(name);
    }

    public static boolean isMainQueue() {
        return Holder.provider.isMainQueue();
    }

    private static final class Holder {
        private static final DispatchQueueProvider provider = Provider.of(DispatchQueueProvider.class);
        private static final DispatchQueue MAIN_QUEUE = provider.createMainQueue();
    }
}

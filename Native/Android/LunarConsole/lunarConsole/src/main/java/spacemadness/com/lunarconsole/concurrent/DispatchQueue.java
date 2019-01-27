//
//  DispatchQueue.java
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

package spacemadness.com.lunarconsole.concurrent;

import android.os.Looper;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.checkNotNull;

public abstract class DispatchQueue {
	private final String name;

	public DispatchQueue(String name) {
		this.name = checkNotNull(name, "name");
	}

	public abstract void dispatch(Runnable r);

	public abstract void dispatch(Runnable r, long delay);

	public abstract boolean isCurrent();

	public String getName() {
		return name;
	}

	public static DispatchQueue mainQueue() {
		return Holder.MAIN_QUEUE;
	}

	private static final class Holder {
		private static final DispatchQueue MAIN_QUEUE = new SerialDispatchQueue(Looper.getMainLooper(), "main");
	}
}

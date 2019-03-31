//
//  ConsoleOverlayLogView.java
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

import android.content.Context;
import android.view.MotionEvent;
import android.widget.ListView;

import spacemadness.com.lunarconsole.core.Destroyable;
import spacemadness.com.lunarconsole.debug.Log;
import spacemadness.com.lunarconsole.settings.LogColors;
import spacemadness.com.lunarconsole.settings.LogEntryColors;
import spacemadness.com.lunarconsole.settings.LogOverlaySettings;
import spacemadness.com.lunarconsole.utils.CycleArray;
import spacemadness.com.lunarconsole.utils.ThreadUtils;

import static spacemadness.com.lunarconsole.console.BaseConsoleLogAdapter.DataSource;
import static spacemadness.com.lunarconsole.debug.Tags.OVERLAY_VIEW;

import static spacemadness.com.lunarconsole.debug.TestHelper.*;

public class LogOverlayView extends ListView implements Destroyable, DataSource, LunarConsoleListener {
	private final Console console;

	private final LogOverlaySettings settings;

	private final LogOverlayAdapter adapter;

	private final CycleArray<ConsoleLogEntry> entries;

	private final Runnable entryRemovalCallback = new Runnable() {
		@Override
		public void run() {
			if (entryRemovalScheduled) {
				entryRemovalScheduled = false;

				// remove first visible row
				if (entries.realLength() > 0) {
					testEvent(TEST_EVENT_OVERLAY_REMOVE_ITEM, entries);

					entries.trimHeadIndex(1);
					reloadData();
				}

				// if more entries are visible - schedule another removal
				if (entries.realLength() > 0) {
					scheduleEntryRemoval();
				}
			}
		}
	};
	private boolean entryRemovalScheduled;

	public LogOverlayView(Context context, Console console, LogOverlaySettings settings) {
		super(context);

		if (console == null) {
			throw new NullPointerException("Console is null");
		}

		if (settings == null) {
			throw new NullPointerException("Settings is null");
		}

		this.console = console;
		this.console.setConsoleListener(this);

		this.settings = settings;

		entries = new CycleArray<>(ConsoleLogEntry.class, settings.maxVisibleLines);
		adapter = new LogOverlayAdapter(this, createLogViewStyle(settings.colors));

		setDivider(null);
		setDividerHeight(0);
		setAdapter(adapter);
		setOverScrollMode(ListView.OVER_SCROLL_NEVER);
		setScrollingCacheEnabled(false);

		reloadData();
	}

	private static LogViewStyle createLogViewStyle(LogColors colors) {
		return new LogViewStyle(
			createLogEntryStyle(colors.exception),
			createLogEntryStyle(colors.error),
			createLogEntryStyle(colors.warning),
			createLogEntryStyle(colors.debug)
		);
	}

	private static LogEntryStyle createLogEntryStyle(LogEntryColors colors) {
		return new LogEntryStyle(colors.foreground.toARGB(), colors.background.toARGB());
	}

	//region Events

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return false; // no touch events
	}

	//endregion

	//region Data

	private void reloadData() {
		adapter.notifyDataSetChanged();
	}

	//endregion

	//region Entry removal

	private void scheduleEntryRemoval() {
		if (!entryRemovalScheduled) {
			entryRemovalScheduled = true;
			ThreadUtils.runOnUIThread(entryRemovalCallback, (long) (settings.timeout * 1000L));

			testEvent(TEST_EVENT_OVERLAY_SCHEDULE_ITEM_REMOVAL);
		}
	}

	private void cancelEntryRemoval() {
		entryRemovalScheduled = false;
		ThreadUtils.cancel(entryRemovalCallback);
	}

	//endregion

	//region Destroyable

	@Override
	public void destroy() {
		Log.d(OVERLAY_VIEW, "Destroy overlay view");
		if (console.getConsoleListener() == this) {
			console.setConsoleListener(null);
		}
		cancelEntryRemoval();
	}

	//endregion

	//region DataSource

	@Override
	public ConsoleLogEntry getEntry(int position) {
		return entries.get(entries.getHeadIndex() + position);
	}

	@Override
	public int getEntryCount() {
		return entries.realLength();
	}

	//endregion

	//region LunarConsoleListener

	@Override
	public void onAddEntry(Console console, ConsoleLogEntry entry, boolean filtered) {
		entries.add(entry); // cycle array will handle entries trim
		reloadData();

		testEvent(TEST_EVENT_OVERLAY_ADD_ITEM, entries);

		scheduleEntryRemoval();
	}

	@Override
	public void onRemoveEntries(Console console, int start, int length) {
		// just do nothing
	}

	@Override
	public void onChangeEntries(Console console) {
		// just do nothing
	}

	@Override
	public void onClearEntries(Console console) {
		cancelEntryRemoval();
		entries.clear();
		reloadData();
	}

	//endregion
}

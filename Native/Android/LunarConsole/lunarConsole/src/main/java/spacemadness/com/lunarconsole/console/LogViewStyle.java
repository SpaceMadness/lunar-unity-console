package spacemadness.com.lunarconsole.console;

import static spacemadness.com.lunarconsole.console.ConsoleLogType.*;

public final class LogViewStyle {
	private final LogEntryStyle[] styles = new LogEntryStyle[COUNT];

	public LogViewStyle(
		LogEntryStyle exception,
		LogEntryStyle error,
		LogEntryStyle warning,
		LogEntryStyle debug
	) {
		styles[ERROR] = error;
		styles[ASSERT] = error;
		styles[WARNING] = warning;
		styles[LOG] = debug;
		styles[EXCEPTION] = exception;
	}

	public LogEntryStyle getEntryStyle(int type) {
		return styles[type];
	}
}

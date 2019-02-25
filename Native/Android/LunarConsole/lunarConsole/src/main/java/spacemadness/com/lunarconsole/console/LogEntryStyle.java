package spacemadness.com.lunarconsole.console;

public final class LogEntryStyle {
	public final int iconId;
	public final int textColor;
	public final int backgroundColor;

	LogEntryStyle(int textColor, int backgroundColor) {
		this(-1, textColor, backgroundColor);
	}

	LogEntryStyle(int iconId, int textColor, int backgroundColor) {
		this.iconId = iconId;
		this.textColor = textColor;
		this.backgroundColor = backgroundColor;
	}
}

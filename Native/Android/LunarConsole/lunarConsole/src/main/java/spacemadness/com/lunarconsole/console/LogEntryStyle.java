package spacemadness.com.lunarconsole.console;

import spacemadness.com.lunarconsole.utils.ColorUtils;

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

	public boolean hasIcon() {
		return iconId != -1;
	}

	public boolean hasBackground() {
		return ColorUtils.getAlpha(backgroundColor) > 0; // should be non-transparent
	}
}

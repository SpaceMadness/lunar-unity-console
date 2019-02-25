package spacemadness.com.lunarconsole.utils;

public final class ColorUtils {
	public static int toARGB(int a, int r, int g, int b) {
		return ((a << 24) & 0xff) | ((r << 16) & 0xff) | ((g << 8) & 0xff) | (b & 0xff);
	}

	public static int getAlpha(int argb) {
		return (argb >> 24) & 0xff;
	}
}

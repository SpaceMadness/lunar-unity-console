package spacemadness.com.lunarconsole.utils;

public final class ColorUtils {
	public static int toARGB(int a, int r, int g, int b) {
		return ((a & 0xff) << 24) | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
	}

	public static int getRed(int argb) {
		return (argb >> 16) & 0xff;
	}
	public static int getGreen(int argb) {
		return (argb >> 8) & 0xff;
	}
	public static int getBlue(int argb) {
		return argb & 0xff;
	}
	public static int getAlpha(int argb) {
		return (argb >> 24) & 0xff;
	}
}

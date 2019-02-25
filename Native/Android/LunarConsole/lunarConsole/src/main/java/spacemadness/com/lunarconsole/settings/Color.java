package spacemadness.com.lunarconsole.settings;

import spacemadness.com.lunarconsole.utils.ColorUtils;

public class Color {
	public int r;
	public int g;
	public int b;
	public int a;

	//region Equality

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Color color = (Color) o;

		if (r != color.r) return false;
		if (g != color.g) return false;
		if (b != color.b) return false;
		return a == color.a;
	}

	@Override public int hashCode() {
		int result = r;
		result = 31 * result + g;
		result = 31 * result + b;
		result = 31 * result + a;
		return result;
	}

	public int toARGB() {
		return ColorUtils.toARGB(a, r, g, b);
	}

	//endregion
}

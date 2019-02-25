package spacemadness.com.lunarconsole.settings;

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
		return ((a << 24) & 0xff) | ((r << 16) & 0xff) | ((g << 8) & 0xff) | (b & 0xff);
	}

	//endregion
}

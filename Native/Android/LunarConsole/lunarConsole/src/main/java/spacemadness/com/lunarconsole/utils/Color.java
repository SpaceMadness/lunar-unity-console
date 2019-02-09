package spacemadness.com.lunarconsole.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class Color {
	public final int r;
	public final int g;
	public final int b;
	public final int a;

	public Color(int r, int g, int b, int a) throws IllegalArgumentException {
		this.r = checkRange(r);
		this.g = checkRange(g);
		this.b = checkRange(b);
		this.a = checkRange(a);
	}

	public static Color fromJson(String json) throws JSONException {
		return fromJson(new JSONObject(json));
	}

	public static Color fromJson(JSONObject json) throws JSONException {
		int r = json.getInt("r");
		int g = json.getInt("g");
		int b = json.getInt("b");
		int a = json.getInt("a");
		return new Color(r, g, b, a);
	}

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

	@Override
	public String toString() {
		return StringUtils.TryFormat("%s: r=%d, g=%d, b=%d a=%d", getClass().getSimpleName(), r, g, b, a);
	}

	private static int checkRange(int value) throws IllegalArgumentException {
		if (value < 0 || value > 255) {
			throw new IllegalArgumentException("Out of range: " + value);
		}
		return value;
	}
}

package spacemadness.com.lunarconsole.settings;

import junit.framework.TestCase;

import org.json.JSONException;

public class ColorSettingTest extends TestCase {
	public void testFromJson() throws JSONException {
		String json = "{\"r\":10,\"g\":20,\"b\":30,\"a\":40}";
		ColorSetting expected = new ColorSetting(10, 20, 30, 40);
		ColorSetting actual = ColorSetting.fromJson(json);
		assertEquals(expected, actual);
	}

	public void testIllegalArgs() {
		checkColor(256, 0, 0, 0);
		checkColor(0, 256, 0, 0);
		checkColor(0, 0, 256, 0);
		checkColor(0, 0, 0, 256);

		checkColor(-1, 0, 0, 0);
		checkColor(0, -1, 0, 0);
		checkColor(0, 0, -1, 0);
		checkColor(0, 0, 0, -1);

	}

	private static void checkColor(int r, int g, int b, int a) {
		try {
			new ColorSetting(r, g, b, a);
			throw new AssertionError("Was supposed to throw an exception");
		} catch (IllegalArgumentException ignored) {
		}
	}
}
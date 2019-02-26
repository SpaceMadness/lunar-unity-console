package spacemadness.com.lunarconsole.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class ColorUtilsTest {

	@Test
	public void toARGB() {
		int a = 255;
		int r = 200;
		int g = 150;
		int b = 50;
		int argb = ColorUtils.toARGB(a, r, g, b);
		assertEquals(a, ColorUtils.getAlpha(argb));
		assertEquals(r, ColorUtils.getRed(argb));
		assertEquals(g, ColorUtils.getGreen(argb));
		assertEquals(b, ColorUtils.getBlue(argb));
	}
}
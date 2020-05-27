//
//  ColorUtilsTest.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2015-2020 Alex Lementuev, SpaceMadness.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//


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
//
//  Color.java
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


package spacemadness.com.lunarconsole.settings;

import spacemadness.com.lunarconsole.utils.ColorUtils;
import spacemadness.com.lunarconsole.utils.StringUtils;

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

	@Override public String toString() {
		return StringUtils.format("[r: %d, g:%d, b: %d, a:%d]", r, g, b, a);
	}
}

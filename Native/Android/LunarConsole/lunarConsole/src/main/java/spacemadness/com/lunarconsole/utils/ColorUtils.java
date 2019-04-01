//
//  ColorUtils.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2019 Alex Lementuev, SpaceMadness.
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

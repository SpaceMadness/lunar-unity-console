//
//  DefaultColorFactory.java
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


package spacemadness.com.lunarconsole.console;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.utils.StringUtils;

import static spacemadness.com.lunarconsole.utils.StringUtils.parseInt;

public class DefaultColorFactory implements ColorFactory {
    private final Context context;
    private final Map<String, Integer> colorLookup;

    public DefaultColorFactory(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context is null");
        }
        this.context = context;
        this.colorLookup = createColorLookup();
    }

    private static Map<String, Integer> createColorLookup() {
        Map<String, Integer> lookup = new HashMap<>();
        lookup.put("aqua", R.color.lunar_console_color_rich_text_aqua);
        lookup.put("black", R.color.lunar_console_color_rich_text_black);
        lookup.put("blue", R.color.lunar_console_color_rich_text_blue);
        lookup.put("brown", R.color.lunar_console_color_rich_text_brown);
        lookup.put("cyan", R.color.lunar_console_color_rich_text_cyan);
        lookup.put("darkblue", R.color.lunar_console_color_rich_text_darkblue);
        lookup.put("fuchsia", R.color.lunar_console_color_rich_text_fuchsia);
        lookup.put("green", R.color.lunar_console_color_rich_text_green);
        lookup.put("grey", R.color.lunar_console_color_rich_text_grey);
        lookup.put("lightblue", R.color.lunar_console_color_rich_text_lightblue);
        lookup.put("lime", R.color.lunar_console_color_rich_text_lime);
        lookup.put("magenta", R.color.lunar_console_color_rich_text_magenta);
        lookup.put("maroon", R.color.lunar_console_color_rich_text_maroon);
        lookup.put("navy", R.color.lunar_console_color_rich_text_navy);
        lookup.put("olive", R.color.lunar_console_color_rich_text_olive);
        lookup.put("orange", R.color.lunar_console_color_rich_text_orange);
        lookup.put("purple", R.color.lunar_console_color_rich_text_purple);
        lookup.put("red", R.color.lunar_console_color_rich_text_red);
        lookup.put("silver", R.color.lunar_console_color_rich_text_silver);
        lookup.put("teal", R.color.lunar_console_color_rich_text_teal);
        lookup.put("white", R.color.lunar_console_color_rich_text_white);
        lookup.put("yellow", R.color.lunar_console_color_rich_text_yellow);
        return lookup;
    }

    @Override
    public int fromValue(String value) {
        if (StringUtils.IsNullOrEmpty(value)) {
            return getColor(R.color.lunar_console_color_rich_text_error);
        }

        Integer predefinedColorId = colorLookup.get(value.toLowerCase());
        if (predefinedColorId != null) {
            return getColor(predefinedColorId);
        }

        if (value.startsWith("#") && value.length() > 1) {
            final String hexValue = value.substring(1);
            try {
                final long hex = Long.parseLong(hexValue, 16);
                if (hexValue.length() > 6) { // #RRGGBBAA
                    return (int) (hex >> 8) | 0xff000000;
                }
                return (int) hex | 0xff000000;
            } catch (NumberFormatException ignored) {
            }
        }

        return getColor(R.color.lunar_console_color_rich_text_error);
    }

    private Integer getColor(int colorId) {
        return context.getResources().getColor(colorId);
    }
}

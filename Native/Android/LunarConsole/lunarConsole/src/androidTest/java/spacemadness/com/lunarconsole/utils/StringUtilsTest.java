//
//  StringUtilsTest.java
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

import junit.framework.TestCase;

public class StringUtilsTest extends TestCase {
    public void testLength() throws Exception {
        assertEquals(5, StringUtils.length("12345"));
        assertEquals(0, StringUtils.length(""));
        assertEquals(0, StringUtils.length(null));
    }

    public void testContains() throws Exception {
        assertTrue(StringUtils.contains("12345", "34"));
        assertFalse(StringUtils.contains("12345", null));
        assertFalse(StringUtils.contains(null, "34"));
        assertFalse(StringUtils.contains(null, null));
    }

    public void testContainsIgnoreCase() throws Exception {
        assertTrue(StringUtils.containsIgnoreCase("TEST", "es"));
        assertTrue(StringUtils.containsIgnoreCase("TEST", "test"));
        assertFalse(StringUtils.containsIgnoreCase("TEST", "test!"));
        assertFalse(StringUtils.containsIgnoreCase("TEST", null));
        assertFalse(StringUtils.containsIgnoreCase(null, "es"));
        assertFalse(StringUtils.containsIgnoreCase(null, null));
    }

    public void testHasPrefix() throws Exception {
        assertTrue(StringUtils.hasPrefix("12345", "123"));
        assertTrue(StringUtils.hasPrefix("12345", "12345"));
        assertFalse(StringUtils.hasPrefix("12345", "12345!"));
        assertFalse(StringUtils.hasPrefix("12345", null));
        assertFalse(StringUtils.hasPrefix(null, "12345!"));
        assertFalse(StringUtils.hasPrefix(null, null));
    }

    public void testCamelCaseToWords() throws Exception {
        String string = "enableExceptionWarning";
        assertEquals("Enable Exception Warning", StringUtils.camelCaseToWords(string));

        string = "word";
        assertEquals("Word", StringUtils.camelCaseToWords(string));
    }
}
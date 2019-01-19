//
//  StringUtilsTest.java
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

import junit.framework.TestCase;

import static spacemadness.com.lunarconsole.utils.StringUtils.*;

public class StringUtilsTest extends TestCase
{
    public void testLength() throws Exception
    {
        assertEquals(5, length("12345"));
        assertEquals(0, length(""));
        assertEquals(0, length(null));
    }

    public void testContains() throws Exception
    {
        assertTrue(contains("12345", "34"));
        assertFalse(contains("12345", null));
        assertFalse(contains(null, "34"));
        assertFalse(contains(null, null));
    }

    public void testContainsIgnoreCase() throws Exception
    {
        assertTrue(containsIgnoreCase("TEST", "es"));
        assertTrue(containsIgnoreCase("TEST", "test"));
        assertFalse(containsIgnoreCase("TEST", "test!"));
        assertFalse(containsIgnoreCase("TEST", null));
        assertFalse(containsIgnoreCase(null, "es"));
        assertFalse(containsIgnoreCase(null, null));
    }

    public void testHasPrefix() throws Exception
    {
        assertTrue(hasPrefix("12345", "123"));
        assertTrue(hasPrefix("12345", "12345"));
        assertFalse(hasPrefix("12345", "12345!"));
        assertFalse(hasPrefix("12345", null));
        assertFalse(hasPrefix(null, "12345!"));
        assertFalse(hasPrefix(null, null));
    }

    public void testCamelCaseToWords() throws Exception
    {
        String string = "enableExceptionWarning";
        assertEquals("Enable Exception Warning", StringUtils.camelCaseToWords(string));

        string = "word";
        assertEquals("Word", StringUtils.camelCaseToWords(string));
    }
}
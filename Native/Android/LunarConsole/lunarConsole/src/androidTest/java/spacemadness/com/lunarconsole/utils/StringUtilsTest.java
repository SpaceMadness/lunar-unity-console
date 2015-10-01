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
}
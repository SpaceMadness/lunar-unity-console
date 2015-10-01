package spacemadness.com.lunarconsole.console;

import junit.framework.TestCase;

public class LimitSizeListTest extends TestCase
{
    public void testAddElements()
    {
        TestList list = new TestList(10);
        list.addObject("1");
        list.addObject("2");
        list.addObject("3");

        listAssertObjects(list, "1", "2", "3");
    }

    public void testAddElementsOverCapacity()
    {
        TestList list = new TestList(3);
        
        assertEquals(0, list.totalCount());
        assertFalse(list.willOverflow());
        assertFalse(list.isOverfloating());

        list.addObject("1");
        assertEquals(1, list.totalCount());
        assertFalse(list.willOverflow());
        assertFalse(list.isOverfloating());

        list.addObject("2");
        assertEquals(2, list.totalCount());
        assertFalse(list.willOverflow());
        assertFalse(list.isOverfloating());

        list.addObject("3");
        assertEquals(3, list.totalCount());
        assertTrue(list.willOverflow());
        assertFalse(list.isOverfloating());

        list.addObject("4");
        assertEquals(4, list.totalCount());
        assertTrue(list.willOverflow());
        assertTrue(list.isOverfloating());

        listAssertObjects(list, "2", "3", "4");
    }

    public void testAddElementsWayOverCapacity()
    {
        TestList list = new TestList(3);
        assertEquals(0, list.totalCount());
        assertFalse(list.willOverflow());
        assertFalse(list.isOverfloating());

        list.addObject("1");
        assertEquals(1, list.totalCount());
        assertFalse(list.willOverflow());
        assertFalse(list.isOverfloating());

        list.addObject("2");
        assertEquals(2, list.totalCount());
        assertFalse(list.willOverflow());
        assertFalse(list.isOverfloating());

        list.addObject("3");
        assertEquals(3, list.totalCount());
        assertTrue(list.willOverflow());
        assertFalse(list.isOverfloating());

        list.addObject("4");
        assertEquals(4, list.totalCount());
        assertTrue(list.willOverflow());
        assertTrue(list.isOverfloating());

        list.addObject("5");
        assertEquals(5, list.totalCount());
        assertTrue(list.willOverflow());
        assertTrue(list.isOverfloating());

        list.addObject("6");
        assertEquals(6, list.totalCount());
        assertTrue(list.willOverflow());
        assertTrue(list.isOverfloating());

        list.addObject("7");
        assertEquals(7, list.totalCount());
        assertTrue(list.willOverflow());
        assertTrue(list.isOverfloating());

        listAssertObjects(list, "5", "6", "7");
    }

    public void testTrimHead()
    {
        TestList list = new TestList(3);
        list.addObject("1");
        list.addObject("2");
        list.addObject("3");
        list.addObject("4");

        assertTrue(list.willOverflow());
        assertTrue(list.isOverfloating());

        list.trimHead(1);
        listAssertObjects(list, "3", "4");
        assertFalse(list.willOverflow());
        assertFalse(list.isOverfloating());

        list.trimHead(1);
        listAssertObjects(list, "4");
        assertFalse(list.willOverflow());
        assertFalse(list.isOverfloating());

        list.trimHead(1);
        listAssertObjects(list);
        assertFalse(list.willOverflow());
        assertFalse(list.isOverfloating());
    }

    private void listAssertObjects(TestList list, String... expected)
    {
        assertEquals(expected.length, list.count());
        for (int i = 0; i < expected.length; ++i)
        {
            assertEquals(expected[i], list.objectAtIndex(i));
        }
    }

    private static class TestList extends LimitSizeList<String>
    {
        public TestList(int capacity)
        {
            super(String.class, capacity);
        }
    }
}
package spacemadness.com.lunarconsole.utils;

import spacemadness.com.lunarconsole.TestCaseEx;

public class LUSortedListTest extends TestCaseEx
{
    private LUSortedList<String> _list;
    
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        _list = new LUSortedList<>();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Testing

    public void testSortedAdd()
    {
        _list.addObject("3");
        _list.addObject("2");
        _list.addObject("1");
        _list.addObject("4");

        assertList("1", "2", "3", "4");
    }

    public void testDuplicateItems()
    {
        _list.addObject("3");
        _list.addObject("3");
        _list.addObject("2");
        _list.addObject("1");
        _list.addObject("1");
        _list.addObject("4");

        assertList("1", "2", "3", "4");
    }

    public void testRemoveItems()
    {
        _list.addObject("3");
        _list.addObject("2");
        _list.addObject("1");
        _list.addObject("4");

        _list.removeObject("2");

        assertList("1", "3", "4");
    }

    public void testIndexOfItem()
    {
        _list.addObject("3");
        _list.addObject("3");
        _list.addObject("2");
        _list.addObject("1");
        _list.addObject("1");
        _list.addObject("4");

        assertEquals(0, _list.indexOfObject("1"));
        assertEquals(1, _list.indexOfObject("2"));
        assertEquals(2, _list.indexOfObject("3"));
        assertEquals(3, _list.indexOfObject("4"));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Helpers

    private void assertList(String ...expected)
    {
        assertEquals(expected.length, _list.count());
        for (int i = 0; i < expected.length; ++i)
        {
            assertEquals(expected[i], _list.objectAtIndex(i));
        }
    }
}
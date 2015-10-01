package spacemadness.com.lunarconsole.utils;

import junit.framework.TestCase;

public class CycleArrayTest extends TestCase
{
    public void testAddElements()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);
        array.Add("1");

        assertEquals(3, array.Capacity());
        assertEquals(1, array.Length());
        assertEquals(1, array.RealLength());
        AssertArray(array, "1");
    }

    public void testAddElements2()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);
        array.Add("1");
        array.Add("2");

        assertEquals(3, array.Capacity());
        assertEquals(2, array.Length());
        assertEquals(2, array.RealLength());
        AssertArray(array, "1", "2");
    }

    public void testAddElements3()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);
        array.Add("1");
        array.Add("2");
        array.Add("3");

        assertEquals(3, array.Capacity());
        assertEquals(3, array.Length());
        assertEquals(3, array.RealLength());
        AssertArray(array, "1", "2", "3");
    }

    public void testAddElements4()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);
        array.Add("1");
        array.Add("2");
        array.Add("3");
        array.Add("4");

        assertEquals(3, array.Capacity());
        assertEquals(4, array.Length());
        assertEquals(3, array.RealLength());
        AssertArray(array, "2", "3", "4");
    }

    public void testAddElements5()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);
        array.Add("1");
        array.Add("2");
        array.Add("3");
        array.Add("4");
        array.Add("5");

        assertEquals(3, array.Capacity());
        assertEquals(5, array.Length());
        assertEquals(3, array.RealLength());
        AssertArray(array, "3", "4", "5");
    }

    public void testAddElements6()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);
        array.Add("1");
        array.Add("2");
        array.Add("3");
        array.Add("4");
        array.Add("5");
        array.Add("6");

        assertEquals(3, array.Capacity());
        assertEquals(6, array.Length());
        assertEquals(3, array.RealLength());
        AssertArray(array, "4", "5", "6");
    }

    public void testAddElements7()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);
        array.Add("1");
        array.Add("2");
        array.Add("3");
        array.Add("4");
        array.Add("5");
        array.Add("6");
        array.Add("7");

        assertEquals(3, array.Capacity());
        assertEquals(7, array.Length());
        assertEquals(3, array.RealLength());
        AssertArray(array, "5", "6", "7");
    }

    public void testAddElements8()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);
        array.Add("1");
        array.Add("2");
        array.Add("3");
        array.Add("4");
        array.Add("5");
        array.Add("6");
        array.Add("7");
        array.Add("8");

        assertEquals(3, array.Capacity());
        assertEquals(8, array.Length());
        assertEquals(3, array.RealLength());
        AssertArray(array, "6", "7", "8");
    }

    public void testAddElements9()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);
        array.Add("1");
        array.Add("2");
        array.Add("3");
        array.Add("4");
        array.Add("5");
        array.Add("6");
        array.Add("7");
        array.Add("8");
        array.Add("9");

        assertEquals(3, array.Capacity());
        assertEquals(9, array.Length());
        assertEquals(3, array.RealLength());
        AssertArray(array, "7", "8", "9");
    }

    public void testGrowCapacity()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 5);
        array.Add("1");
        array.Add("2");
        array.Add("3");

        array.Capacity(10);

        assertEquals(10, array.Capacity());
        assertEquals(3, array.Length());
        assertEquals(3, array.RealLength());
        AssertArray(array, "1", "2", "3");
    }

    public void testGrowCapacityForAFullArray()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 5);
        array.Add("1");
        array.Add("2");
        array.Add("3");
        array.Add("4");
        array.Add("5");

        array.Capacity(10);

        assertEquals(10, array.Capacity());
        assertEquals(5, array.Length());
        assertEquals(5, array.RealLength());
        AssertArray(array, "1", "2", "3", "4", "5");
    }

    public void testGrowCapacityForOverflowedArrayWithOneExtraElement()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);
        array.Add("1");
        array.Add("2");
        array.Add("3");
        array.Add("4");

        array.Capacity(10);

        assertEquals(10, array.Capacity());
        assertEquals(4, array.Length());
        assertEquals(3, array.RealLength());
        AssertArray(array, "2", "3", "4");
    }

    public void testGrowCapacityForOverflowedArrayWithTwoExtraElements()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);
        array.Add("1");
        array.Add("2");
        array.Add("3");
        array.Add("4");
        array.Add("5");

        array.Capacity(10);

        assertEquals(10, array.Capacity());
        assertEquals(5, array.Length());
        assertEquals(3, array.RealLength());
        AssertArray(array, "3", "4", "5");
    }

    public void testGrowCapacityForOverflowedArrayWithThreeExtraElements()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);
        array.Add("1");
        array.Add("2");
        array.Add("3");
        array.Add("4");
        array.Add("5");
        array.Add("6");

        array.Capacity(10);

        assertEquals(10, array.Capacity());
        assertEquals(6, array.Length());
        assertEquals(3, array.RealLength());
        AssertArray(array, "4", "5", "6");
    }

    public void testGrowCapacityForOverflowedArrayWithFourExtraElements()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);
        array.Add("1");
        array.Add("2");
        array.Add("3");
        array.Add("4");
        array.Add("5");
        array.Add("6");
        array.Add("7");

        array.Capacity(10);

        assertEquals(10, array.Capacity());
        assertEquals(7, array.Length());
        assertEquals(3, array.RealLength());
        AssertArray(array, "5", "6", "7");
    }

    public void testGrowCapacityAndAddMoreElements()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);
        array.Add("1");
        array.Add("2");
        array.Add("3");
        array.Add("4");
        array.Add("5");
        array.Add("6");
        array.Add("7");

        array.Capacity(5);
        assertEquals(5, array.Capacity());
        assertEquals(7, array.Length());
        assertEquals(3, array.RealLength());
        AssertArray(array, "5", "6", "7");

        array.Add("8");
        array.Add("9");

        assertEquals(5, array.Capacity());
        assertEquals(9, array.Length());
        assertEquals(5, array.RealLength());
        AssertArray(array, "5", "6", "7", "8", "9");
    }

    public void testGrowCapacityBiggerArray()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 7);
        for (int i = 1; i <= 7; ++i)
        {
            array.Add(Integer.toString(i));
        }

        array.Capacity(9);
        AssertArray(array, "1", "2","3", "4", "5", "6", "7");

        array.Add("8");
        array.Add("9");

        assertEquals(9, array.Capacity());
        assertEquals(9, array.Length());
        assertEquals(9, array.RealLength());
        AssertArray(array, "1", "2","3", "4", "5", "6", "7", "8", "9");
    }

    public void testGrowCapacityAndOverflowMultipleTimes()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);
        for (int i = 0; i < 10; ++i)
        {
            array.Add(Integer.toString(i + 1));
        }

        array.Capacity(5);
        AssertArray(array, "8", "9", "10");

        array.Add("11");
        array.Add("12");

        assertEquals(5, array.Capacity());
        assertEquals(12, array.Length());
        assertEquals(5, array.RealLength());
        AssertArray(array, "8", "9", "10", "11", "12");
    }

    public void testTrimLength()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 5);
        array.Add("1");
        array.Add("2");
        array.Add("3");
        array.Add("4");
        array.Add("5");

        array.TrimToLength(3);

        AssertArray(array, "1", "2", "3");
        assertEquals(0, array.HeadIndex());
        assertEquals(3, array.Length());
        assertEquals(3, array.RealLength());

        array.Add("6");
        array.Add("7");

        AssertArray(array, "1", "2", "3", "6", "7");
        assertEquals(0, array.HeadIndex());
        assertEquals(5, array.Length());
        assertEquals(5, array.RealLength());

        array.Add("8");
        array.Add("9");

        AssertArray(array, "3", "6", "7", "8", "9");
        assertEquals(2, array.HeadIndex());
        assertEquals(7, array.Length());
        assertEquals(5, array.RealLength());

        array.TrimToLength(4);

        AssertArray(array, "3", "6");
        assertEquals(2, array.HeadIndex());
        assertEquals(4, array.Length());
        assertEquals(2, array.RealLength());

        array.Add("10");
        array.Add("11");

        AssertArray(array, "3", "6", "10", "11");
        assertEquals(2, array.HeadIndex());
        assertEquals(6, array.Length());
        assertEquals(4, array.RealLength());

        array.TrimToLength(2);

        AssertArray(array);
        assertEquals(2, array.HeadIndex());
        assertEquals(2, array.Length());
        assertEquals(0, array.RealLength());

        array.Add("12");
        array.Add("13");
        array.Add("14");
        array.Add("15");
        array.Add("16");
        array.Add("17");
        array.Add("18");

        AssertArray(array, "14", "15", "16", "17", "18");
        assertEquals(4, array.HeadIndex());
        assertEquals(9, array.Length());
        assertEquals(5, array.RealLength());
    }

    public void testTrimHeadIndex()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 5);
        array.Add("1");
        array.Add("2");
        array.Add("3");
        array.Add("4");
        array.Add("5");

        array.TrimToHeadIndex(2);

        AssertArray(array, "3", "4", "5");
        assertEquals(2, array.HeadIndex());
        assertEquals(5, array.Length());
        assertEquals(3, array.RealLength());

        array.Add("6");
        array.Add("7");

        AssertArray(array, "3", "4", "5", "6", "7");
        assertEquals(2, array.HeadIndex());
        assertEquals(7, array.Length());
        assertEquals(5, array.RealLength());

        array.Add("8");
        array.Add("9");

        AssertArray(array, "5", "6", "7", "8", "9");
        assertEquals(4, array.HeadIndex());
        assertEquals(9, array.Length());
        assertEquals(5, array.RealLength());

        array.TrimToHeadIndex(7);

        AssertArray(array, "8", "9");
        assertEquals(7, array.HeadIndex());
        assertEquals(9, array.Length());
        assertEquals(2, array.RealLength());

        array.Add("10");
        array.Add("11");

        AssertArray(array, "8", "9", "10", "11");
        assertEquals(7, array.HeadIndex());
        assertEquals(11, array.Length());
        assertEquals(4, array.RealLength());

        array.TrimToHeadIndex(11);

        AssertArray(array);
        assertEquals(11, array.HeadIndex());
        assertEquals(11, array.Length());
        assertEquals(0, array.RealLength());

        array.Add("12");
        array.Add("13");
        array.Add("14");
        array.Add("15");
        array.Add("16");
        array.Add("17");
        array.Add("18");

        AssertArray(array, "14", "15", "16", "17", "18");
        assertEquals(13, array.HeadIndex());
        assertEquals(18, array.Length());
        assertEquals(5, array.RealLength());
    }

    public void testContains() throws Exception
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);
        array.Add("1");
        array.Add("2");
        array.Add("3");

        assertTrue(array.contains("1"));
        assertTrue(array.contains("2"));
        assertTrue(array.contains("3"));

        array.Add("4");
        assertFalse(array.contains("1"));
        assertTrue(array.contains("2"));
        assertTrue(array.contains("3"));
        assertTrue(array.contains("4"));

        array.Add("5");
        assertFalse(array.contains("1"));
        assertFalse(array.contains("2"));
        assertTrue(array.contains("3"));
        assertTrue(array.contains("4"));
        assertTrue(array.contains("5"));

        array.Add("6");
        assertFalse(array.contains("1"));
        assertFalse(array.contains("2"));
        assertFalse(array.contains("3"));
        assertTrue(array.contains("4"));
        assertTrue(array.contains("5"));
        assertTrue(array.contains("6"));

        array.Add("7");
        assertFalse(array.contains("1"));
        assertFalse(array.contains("2"));
        assertFalse(array.contains("3"));
        assertFalse(array.contains("4"));
        assertTrue(array.contains("5"));
        assertTrue(array.contains("6"));
        assertTrue(array.contains("7"));
    }

    public void testRemovedItem() throws Exception
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);

        assertNull(array.Add("1"));
        assertNull(array.Add("2"));
        assertNull(array.Add("3"));

        assertEquals("1", array.Add("4"));
        assertEquals("2", array.Add("5"));
        assertEquals("3", array.Add("6"));

        assertEquals("4", array.Add("7"));
        assertEquals("5", array.Add("8"));
        assertEquals("6", array.Add("9"));
    }

    public void testIterator() throws Exception
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);

        array.Add("1");
        array.Add("2");
        array.Add("3");
        assertArrayIterator(array, "1", "2", "3");

        array.Add("4");
        assertArrayIterator(array, "2", "3", "4");

        array.Add("5");
        assertArrayIterator(array, "3", "4", "5");

        array.Add("6");
        assertArrayIterator(array, "4", "5", "6");

        array.Add("7");
        assertArrayIterator(array, "5", "6", "7");
    }

    private <T> void AssertArray(CycleArray<T> actual, T... expected)
    {
        assertEquals(expected.length, actual.RealLength());
        for (int i = 0, j = actual.HeadIndex(); i < expected.length; ++i, ++j)
        {
            assertEquals(expected[i], actual.get(j));
        }
    }

    private <T> void assertArrayIterator(CycleArray<T> actual, T... expected)
    {
        int index = 0;
        for (T e : actual)
        {
            assertEquals(expected[index++], e);
        }
        assertEquals(expected.length, index);
    }
}
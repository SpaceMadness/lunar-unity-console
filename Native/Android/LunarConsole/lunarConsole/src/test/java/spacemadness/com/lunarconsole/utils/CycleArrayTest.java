//
//  CycleArrayTest.java
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

public class CycleArrayTest extends TestCase
{
    public void testAddElements()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);
        array.add("1");

        assertEquals(3, array.getCapacity());
        assertEquals(1, array.length());
        assertEquals(1, array.realLength());
        AssertArray(array, "1");
    }

    public void testAddElements2()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);
        array.add("1");
        array.add("2");

        assertEquals(3, array.getCapacity());
        assertEquals(2, array.length());
        assertEquals(2, array.realLength());
        AssertArray(array, "1", "2");
    }

    public void testAddElements3()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);
        array.add("1");
        array.add("2");
        array.add("3");

        assertEquals(3, array.getCapacity());
        assertEquals(3, array.length());
        assertEquals(3, array.realLength());
        AssertArray(array, "1", "2", "3");
    }

    public void testAddElements4()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);
        array.add("1");
        array.add("2");
        array.add("3");
        array.add("4");

        assertEquals(3, array.getCapacity());
        assertEquals(4, array.length());
        assertEquals(3, array.realLength());
        AssertArray(array, "2", "3", "4");
    }

    public void testAddElements5()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);
        array.add("1");
        array.add("2");
        array.add("3");
        array.add("4");
        array.add("5");

        assertEquals(3, array.getCapacity());
        assertEquals(5, array.length());
        assertEquals(3, array.realLength());
        AssertArray(array, "3", "4", "5");
    }

    public void testAddElements6()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);
        array.add("1");
        array.add("2");
        array.add("3");
        array.add("4");
        array.add("5");
        array.add("6");

        assertEquals(3, array.getCapacity());
        assertEquals(6, array.length());
        assertEquals(3, array.realLength());
        AssertArray(array, "4", "5", "6");
    }

    public void testAddElements7()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);
        array.add("1");
        array.add("2");
        array.add("3");
        array.add("4");
        array.add("5");
        array.add("6");
        array.add("7");

        assertEquals(3, array.getCapacity());
        assertEquals(7, array.length());
        assertEquals(3, array.realLength());
        AssertArray(array, "5", "6", "7");
    }

    public void testAddElements8()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);
        array.add("1");
        array.add("2");
        array.add("3");
        array.add("4");
        array.add("5");
        array.add("6");
        array.add("7");
        array.add("8");

        assertEquals(3, array.getCapacity());
        assertEquals(8, array.length());
        assertEquals(3, array.realLength());
        AssertArray(array, "6", "7", "8");
    }

    public void testAddElements9()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);
        array.add("1");
        array.add("2");
        array.add("3");
        array.add("4");
        array.add("5");
        array.add("6");
        array.add("7");
        array.add("8");
        array.add("9");

        assertEquals(3, array.getCapacity());
        assertEquals(9, array.length());
        assertEquals(3, array.realLength());
        AssertArray(array, "7", "8", "9");
    }

    public void testGrowCapacity()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 5);
        array.add("1");
        array.add("2");
        array.add("3");

        array.setCapacity(10);

        assertEquals(10, array.getCapacity());
        assertEquals(3, array.length());
        assertEquals(3, array.realLength());
        AssertArray(array, "1", "2", "3");
    }

    public void testGrowCapacityForAFullArray()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 5);
        array.add("1");
        array.add("2");
        array.add("3");
        array.add("4");
        array.add("5");

        array.setCapacity(10);

        assertEquals(10, array.getCapacity());
        assertEquals(5, array.length());
        assertEquals(5, array.realLength());
        AssertArray(array, "1", "2", "3", "4", "5");
    }

    public void testGrowCapacityForOverflowedArrayWithOneExtraElement()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);
        array.add("1");
        array.add("2");
        array.add("3");
        array.add("4");

        array.setCapacity(10);

        assertEquals(10, array.getCapacity());
        assertEquals(4, array.length());
        assertEquals(3, array.realLength());
        AssertArray(array, "2", "3", "4");
    }

    public void testGrowCapacityForOverflowedArrayWithTwoExtraElements()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);
        array.add("1");
        array.add("2");
        array.add("3");
        array.add("4");
        array.add("5");

        array.setCapacity(10);

        assertEquals(10, array.getCapacity());
        assertEquals(5, array.length());
        assertEquals(3, array.realLength());
        AssertArray(array, "3", "4", "5");
    }

    public void testGrowCapacityForOverflowedArrayWithThreeExtraElements()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);
        array.add("1");
        array.add("2");
        array.add("3");
        array.add("4");
        array.add("5");
        array.add("6");

        array.setCapacity(10);

        assertEquals(10, array.getCapacity());
        assertEquals(6, array.length());
        assertEquals(3, array.realLength());
        AssertArray(array, "4", "5", "6");
    }

    public void testGrowCapacityForOverflowedArrayWithFourExtraElements()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);
        array.add("1");
        array.add("2");
        array.add("3");
        array.add("4");
        array.add("5");
        array.add("6");
        array.add("7");

        array.setCapacity(10);

        assertEquals(10, array.getCapacity());
        assertEquals(7, array.length());
        assertEquals(3, array.realLength());
        AssertArray(array, "5", "6", "7");
    }

    public void testGrowCapacityAndAddMoreElements()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);
        array.add("1");
        array.add("2");
        array.add("3");
        array.add("4");
        array.add("5");
        array.add("6");
        array.add("7");

        array.setCapacity(5);
        assertEquals(5, array.getCapacity());
        assertEquals(7, array.length());
        assertEquals(3, array.realLength());
        AssertArray(array, "5", "6", "7");

        array.add("8");
        array.add("9");

        assertEquals(5, array.getCapacity());
        assertEquals(9, array.length());
        assertEquals(5, array.realLength());
        AssertArray(array, "5", "6", "7", "8", "9");
    }

    public void testGrowCapacityBiggerArray()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 7);
        for (int i = 1; i <= 7; ++i)
        {
            array.add(Integer.toString(i));
        }

        array.setCapacity(9);
        AssertArray(array, "1", "2","3", "4", "5", "6", "7");

        array.add("8");
        array.add("9");

        assertEquals(9, array.getCapacity());
        assertEquals(9, array.length());
        assertEquals(9, array.realLength());
        AssertArray(array, "1", "2","3", "4", "5", "6", "7", "8", "9");
    }

    public void testGrowCapacityAndOverflowMultipleTimes()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);
        for (int i = 0; i < 10; ++i)
        {
            array.add(Integer.toString(i + 1));
        }

        array.setCapacity(5);
        AssertArray(array, "8", "9", "10");

        array.add("11");
        array.add("12");

        assertEquals(5, array.getCapacity());
        assertEquals(12, array.length());
        assertEquals(5, array.realLength());
        AssertArray(array, "8", "9", "10", "11", "12");
    }

    public void testTrimLength()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 5);
        array.add("1");
        array.add("2");
        array.add("3");
        array.add("4");
        array.add("5");

        array.trimToLength(3);

        AssertArray(array, "1", "2", "3");
        assertEquals(0, array.getHeadIndex());
        assertEquals(3, array.length());
        assertEquals(3, array.realLength());

        array.add("6");
        array.add("7");

        AssertArray(array, "1", "2", "3", "6", "7");
        assertEquals(0, array.getHeadIndex());
        assertEquals(5, array.length());
        assertEquals(5, array.realLength());

        array.add("8");
        array.add("9");

        AssertArray(array, "3", "6", "7", "8", "9");
        assertEquals(2, array.getHeadIndex());
        assertEquals(7, array.length());
        assertEquals(5, array.realLength());

        array.trimToLength(4);

        AssertArray(array, "3", "6");
        assertEquals(2, array.getHeadIndex());
        assertEquals(4, array.length());
        assertEquals(2, array.realLength());

        array.add("10");
        array.add("11");

        AssertArray(array, "3", "6", "10", "11");
        assertEquals(2, array.getHeadIndex());
        assertEquals(6, array.length());
        assertEquals(4, array.realLength());

        array.trimToLength(2);

        AssertArray(array);
        assertEquals(2, array.getHeadIndex());
        assertEquals(2, array.length());
        assertEquals(0, array.realLength());

        array.add("12");
        array.add("13");
        array.add("14");
        array.add("15");
        array.add("16");
        array.add("17");
        array.add("18");

        AssertArray(array, "14", "15", "16", "17", "18");
        assertEquals(4, array.getHeadIndex());
        assertEquals(9, array.length());
        assertEquals(5, array.realLength());
    }

    public void testTrimHeadIndex()
    {
        CycleArray<String> array = new CycleArray<>(String.class, 5);
        array.add("1");
        array.add("2");
        array.add("3");
        array.add("4");
        array.add("5");

        array.trimToHeadIndex(2);

        AssertArray(array, "3", "4", "5");
        assertEquals(2, array.getHeadIndex());
        assertEquals(5, array.length());
        assertEquals(3, array.realLength());

        array.add("6");
        array.add("7");

        AssertArray(array, "3", "4", "5", "6", "7");
        assertEquals(2, array.getHeadIndex());
        assertEquals(7, array.length());
        assertEquals(5, array.realLength());

        array.add("8");
        array.add("9");

        AssertArray(array, "5", "6", "7", "8", "9");
        assertEquals(4, array.getHeadIndex());
        assertEquals(9, array.length());
        assertEquals(5, array.realLength());

        array.trimToHeadIndex(7);

        AssertArray(array, "8", "9");
        assertEquals(7, array.getHeadIndex());
        assertEquals(9, array.length());
        assertEquals(2, array.realLength());

        array.add("10");
        array.add("11");

        AssertArray(array, "8", "9", "10", "11");
        assertEquals(7, array.getHeadIndex());
        assertEquals(11, array.length());
        assertEquals(4, array.realLength());

        array.trimToHeadIndex(11);

        AssertArray(array);
        assertEquals(11, array.getHeadIndex());
        assertEquals(11, array.length());
        assertEquals(0, array.realLength());

        array.add("12");
        array.add("13");
        array.add("14");
        array.add("15");
        array.add("16");
        array.add("17");
        array.add("18");

        AssertArray(array, "14", "15", "16", "17", "18");
        assertEquals(13, array.getHeadIndex());
        assertEquals(18, array.length());
        assertEquals(5, array.realLength());
    }

    public void testContains() throws Exception
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);
        array.add("1");
        array.add("2");
        array.add("3");

        assertTrue(array.contains("1"));
        assertTrue(array.contains("2"));
        assertTrue(array.contains("3"));

        array.add("4");
        assertFalse(array.contains("1"));
        assertTrue(array.contains("2"));
        assertTrue(array.contains("3"));
        assertTrue(array.contains("4"));

        array.add("5");
        assertFalse(array.contains("1"));
        assertFalse(array.contains("2"));
        assertTrue(array.contains("3"));
        assertTrue(array.contains("4"));
        assertTrue(array.contains("5"));

        array.add("6");
        assertFalse(array.contains("1"));
        assertFalse(array.contains("2"));
        assertFalse(array.contains("3"));
        assertTrue(array.contains("4"));
        assertTrue(array.contains("5"));
        assertTrue(array.contains("6"));

        array.add("7");
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

        assertNull(array.add("1"));
        assertNull(array.add("2"));
        assertNull(array.add("3"));

        assertEquals("1", array.add("4"));
        assertEquals("2", array.add("5"));
        assertEquals("3", array.add("6"));

        assertEquals("4", array.add("7"));
        assertEquals("5", array.add("8"));
        assertEquals("6", array.add("9"));
    }

    public void testIterator() throws Exception
    {
        CycleArray<String> array = new CycleArray<>(String.class, 3);

        array.add("1");
        array.add("2");
        array.add("3");
        assertArrayIterator(array, "1", "2", "3");

        array.add("4");
        assertArrayIterator(array, "2", "3", "4");

        array.add("5");
        assertArrayIterator(array, "3", "4", "5");

        array.add("6");
        assertArrayIterator(array, "4", "5", "6");

        array.add("7");
        assertArrayIterator(array, "5", "6", "7");
    }

    private <T> void AssertArray(CycleArray<T> actual, T... expected)
    {
        assertEquals(expected.length, actual.realLength());
        for (int i = 0, j = actual.getHeadIndex(); i < expected.length; ++i, ++j)
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
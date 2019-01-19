//
//  LimitSizeListTest.java
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

    public void testTrimElements()
    {
        TestList list = new TestList(3, 2);
        assertEquals(0, list.count());
        assertEquals(0, list.totalCount());
        assertEquals(0, list.trimmedCount());
        assertFalse(list.isTrimmed());

        list.addObject("1");
        assertEquals(1, list.count());
        assertEquals(1, list.totalCount());
        assertEquals(0, list.trimmedCount());
        assertFalse(list.isTrimmed());

        list.addObject("2");
        assertEquals(2, list.count());
        assertEquals(2, list.totalCount());
        assertEquals(0, list.trimmedCount());
        assertFalse(list.isTrimmed());

        list.addObject("3");
        assertEquals(3, list.count());
        assertEquals(3, list.totalCount());
        assertEquals(0, list.trimmedCount());
        assertFalse(list.isTrimmed());

        list.addObject("4");
        assertEquals(2, list.count());
        assertEquals(4, list.totalCount());
        assertEquals(2, list.trimmedCount());
        assertTrue(list.isTrimmed());

        listAssertObjects(list, "3", "4");

        list.addObject("5");
        assertEquals(3, list.count());
        assertEquals(5, list.totalCount());
        assertEquals(2, list.trimmedCount());
        assertTrue(list.isTrimmed());

        listAssertObjects(list, "3", "4", "5");

        list.addObject("6");
        assertEquals(2, list.count());
        assertEquals(6, list.totalCount());
        assertEquals(4, list.trimmedCount());
        assertTrue(list.isTrimmed());

        listAssertObjects(list, "5", "6");

        list.addObject("7");
        assertEquals(3, list.count());
        assertEquals(7, list.totalCount());
        assertEquals(4, list.trimmedCount());
        assertTrue(list.isTrimmed());

        listAssertObjects(list, "5", "6", "7");
    }

    public void testTrimElementsAndClear()
    {
        TestList list = new TestList(3, 2);
        list.addObject("1");
        list.addObject("2");
        list.addObject("3");
        list.addObject("4");

        list.clear();
        assertEquals(0, list.count());
        assertEquals(0, list.totalCount());
        assertEquals(0, list.trimmedCount());
        assertFalse(list.isTrimmed());

        list.addObject("5");
        assertEquals(1, list.count());
        assertEquals(1, list.totalCount());
        assertEquals(0, list.trimmedCount());
        assertFalse(list.isTrimmed());

        listAssertObjects(list, "5");

        list.addObject("6");
        assertEquals(2, list.count());
        assertEquals(2, list.totalCount());
        assertEquals(0, list.trimmedCount());
        assertFalse(list.isTrimmed());

        listAssertObjects(list, "5", "6");

        list.addObject("7");
        assertEquals(3, list.count());
        assertEquals(3, list.totalCount());
        assertEquals(0, list.trimmedCount());
        assertFalse(list.isTrimmed());

        listAssertObjects(list, "5", "6", "7");

        list.addObject("8");
        assertEquals(2, list.count());
        assertEquals(4, list.totalCount());
        assertEquals(2, list.trimmedCount());
        assertTrue(list.isTrimmed());

        listAssertObjects(list, "7", "8");
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
            this(capacity, 1);
        }

        public TestList(int capacity, int trimSize)
        {
            super(String.class, capacity, trimSize);
        }
    }
}
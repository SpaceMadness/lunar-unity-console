//
//  LimitSizeListTest.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2015 Alex Lementuev, SpaceMadness.
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
//
//  SortedListTest.java
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

import org.junit.Test;

import spacemadness.com.lunarconsole.TestCase;

import static org.junit.Assert.*;

public class SortedListTest extends TestCase {
	private SortedList<String> _list;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		_list = new SortedList<>();
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	// Testing

	@Test
	public void testSortedAdd() {
		_list.addObject("3");
		_list.addObject("2");
		_list.addObject("1");
		_list.addObject("4");

		assertList("1", "2", "3", "4");
	}

	@Test
	public void testDuplicateItems() {
		_list.addObject("3");
		_list.addObject("3");
		_list.addObject("2");
		_list.addObject("1");
		_list.addObject("1");
		_list.addObject("4");

		assertList("1", "2", "3", "4");
	}

	@Test
	public void testRemoveItems() {
		_list.addObject("3");
		_list.addObject("2");
		_list.addObject("1");
		_list.addObject("4");

		_list.removeObject("2");

		assertList("1", "3", "4");
	}

	@Test
	public void testIndexOfItem() {
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

	private void assertList(String... expected) {
		assertEquals(expected.length, _list.count());
		for (int i = 0; i < expected.length; ++i) {
			assertEquals(expected[i], _list.objectAtIndex(i));
		}
	}
}
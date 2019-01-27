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
	private SortedList<String> list;

	//region Setup

	@Override
	public void setUp() throws Exception {
		super.setUp();
		list = new SortedList<>();
	}

	//endregion

	//region Testing

	@Test
	public void testSortedAdd() {
		list.addObject("3");
		list.addObject("2");
		list.addObject("1");
		list.addObject("4");

		assertList("1", "2", "3", "4");
	}

	@Test
	public void testDuplicateItems() {
		list.addObject("3");
		list.addObject("3");
		list.addObject("2");
		list.addObject("1");
		list.addObject("1");
		list.addObject("4");

		assertList("1", "2", "3", "4");
	}

	@Test
	public void testRemoveItems() {
		list.addObject("3");
		list.addObject("2");
		list.addObject("1");
		list.addObject("4");

		list.removeObject("2");

		assertList("1", "3", "4");
	}

	@Test
	public void testIndexOfItem() {
		list.addObject("3");
		list.addObject("3");
		list.addObject("2");
		list.addObject("1");
		list.addObject("1");
		list.addObject("4");

		assertEquals(0, list.indexOfObject("1"));
		assertEquals(1, list.indexOfObject("2"));
		assertEquals(2, list.indexOfObject("3"));
		assertEquals(3, list.indexOfObject("4"));
	}

	//endregion

	//region Helpers

	private void assertList(String... expected) {
		assertEquals(expected.length, list.count());
		for (int i = 0; i < expected.length; ++i) {
			assertEquals(expected[i], list.objectAtIndex(i));
		}
	}

	//endregion
}
package spacemadness.com.lunarconsole.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static spacemadness.com.lunarconsole.utils.Collections.*;

public class CollectionsTest {
	@Test
	public void testEmptyMap() {
		List<Integer> list = new ArrayList<>();
		List<String> result = map(list, new Map<Integer, String>() {
			@Override
			public String map(Integer integer) {
				return integer.toString();
			}
		});
		assertTrue(result.isEmpty());
	}

	@Test
	public void testMap() {
		List<Integer> list = Arrays.asList(1, 2, 3, 4);

		List<String> expected = Arrays.asList("1", "2", "3", "4");
		List<String> actual = map(list, new Map<Integer, String>() {
			@Override
			public String map(Integer integer) {
				return integer.toString();
			}
		});
		assertThat(actual, is(expected));
	}
}
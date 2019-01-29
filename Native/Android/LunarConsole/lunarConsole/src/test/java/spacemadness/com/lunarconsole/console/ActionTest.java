package spacemadness.com.lunarconsole.console;

import org.junit.Test;

import static org.junit.Assert.*;

public class ActionTest {
	@Test
	public void testSimpleName() {
		Action action = new Action(100, "My Action");
		assertEquals("My Action", action.getName());
		assertEquals("", action.getGroupName());
	}

	@Test
	public void testComplexName() {
		Action action = new Action(100, "My Group\\My Action");
		assertEquals("My Action", action.getName());
		assertEquals("My Group", action.getGroupName());
	}

	@Test
	public void testVeryComplexName() {
		Action action = new Action(100, "My Domain\\My Group\\My Action");
		assertEquals("My Action", action.getName());
		assertEquals("My Domain/My Group", action.getGroupName());
	}
}
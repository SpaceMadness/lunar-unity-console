package spacemadness.com.lunarconsole.data;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import spacemadness.com.lunarconsole.ImmediateDispatchQueue;
import spacemadness.com.lunarconsole.TestCase;
import spacemadness.com.lunarconsole.console.Action;
import spacemadness.com.lunarconsole.core.Observer;
import spacemadness.com.lunarconsole.utils.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ConsoleActionRepositoryTest extends TestCase {
	private MockConsoleActionRepository repository;
	private List<String> actualActions;

	@Override
	public void setUp() throws Exception {
		super.setUp();

		repository = new MockConsoleActionRepository();
		actualActions = new ArrayList<>();
	}

	@Test
	public void testAddRemoveActions() {

		int id1 = repository.addAction("a1").actionId();
		int id2 = repository.addAction("a2").actionId();
		int id3 = repository.addAction("a3").actionId();

		assertActions("a1", "a2", "a3");

		repository.removeAction(id2);
		assertActions("a1", "a3");

		repository.removeAction(id1);
		assertActions("a3");

		repository.removeAction(id1);
		assertActions("a3");

		repository.removeAction(id3);
		assertActions();

		repository.removeAction(id3);
		assertActions();
	}

	private void assertActions(String... expected) {
		assertThat(Arrays.asList(expected), is(actualActions));
	}

	private class MockConsoleActionRepository extends ConsoleActionRepository {
		private int nextActionId;

		MockConsoleActionRepository() {
			super(new ImmediateDispatchQueue());
			getActions().observe(new Observer<List<Action>>() {
				@Override
				public void onChanged(List<Action> actions) {
					actualActions = Collections.map(actions, new Collections.Map<Action, String>() {
						@Override
						public String map(Action action) {
							return action.getName();
						}
					});
				}
			});
		}

		Action addAction(String actionName) {
			return addAction(++nextActionId, actionName);
		}
	}
}
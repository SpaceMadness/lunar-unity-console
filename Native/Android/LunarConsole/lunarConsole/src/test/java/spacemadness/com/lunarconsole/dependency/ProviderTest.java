package spacemadness.com.lunarconsole.dependency;

import junit.framework.TestCase;

public class ProviderTest extends TestCase {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Provider.clear();
	}

	public void testProvider() {
		MyProviderImpl providable = new MyProviderImpl();

		Provider.register(MyProvider.class, providable);
		assertSame(providable, Provider.of(MyProvider.class));
	}

	public void testMissingProvider() {
		try {
			Provider.of(MyProvider.class);
			fail("Should throw MissingProvidableException");
		} catch (IllegalArgumentException ignored) {
		}
	}

	private interface MyProvider extends ProviderDependency {
	}

	private static class MyProviderImpl implements MyProvider {
	}
}
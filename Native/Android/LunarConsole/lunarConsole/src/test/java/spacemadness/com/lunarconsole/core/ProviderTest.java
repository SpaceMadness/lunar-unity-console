package spacemadness.com.lunarconsole.core;

import junit.framework.TestCase;

public class ProviderTest extends TestCase {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Provider.clear();
	}

	public void testProvider() {
		MyProvidableImpl providable = new MyProvidableImpl();

		Provider.register(MyProvidable.class, providable);
		assertSame(providable, Provider.of(MyProvidable.class));
	}

	public void testMissingProvider() {
		try {
			Provider.of(MyProvidable.class);
			fail("Should throw MissingProvidableException");
		} catch (MissingProvidableException ignored) {}
	}

	private interface MyProvidable extends Providable {
	}

	private static class MyProvidableImpl implements MyProvidable {
	}
}
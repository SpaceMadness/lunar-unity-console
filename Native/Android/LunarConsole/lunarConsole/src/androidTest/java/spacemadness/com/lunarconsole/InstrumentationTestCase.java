package spacemadness.com.lunarconsole;

import java.io.IOException;

public class InstrumentationTestCase extends android.test.InstrumentationTestCase {
	protected String readTextAsset(String path) {
		try {
			return TestUtils.readTextAsset(getInstrumentation().getContext(), path);
		} catch (IOException e) {
			throw new AssertionError("Unable to read text asset: " + path, e);
		}
	}
}

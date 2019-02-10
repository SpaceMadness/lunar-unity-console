package spacemadness.com.lunarconsole;

import java.io.IOException;

public class InstrumentationTestCase extends android.test.InstrumentationTestCase {
	protected String readTextAsset(String path) throws IOException {
		return TestUtils.readTextAsset(getInstrumentation().getContext(), path);
	}
}
